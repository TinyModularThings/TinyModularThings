package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.tile.IEnergyTile;
import ic2.api.energy.tile.IMetaDelegate;
import ic2.core.IC2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;

public class LocalEnergyNet
{
	public static boolean useLinearTransfering = false;
	public static final double nonConductorResistance = 0.2D;
	public static final double sourceResistanceFactor = 0.0625D;
	public static final double sinkResistanceFactor = 1.0D;
	public static final double sourceCurrent = 17.0D;
	public static final boolean enableCache = true;
	private static int nextEnergyGridUid = 0;
	private static int nextEnergyNodeUid = 0;
	
	protected final Set<EnergyGrid> grids = new HashSet<EnergyGrid>();
	protected List<EnergyChange> changes = new ArrayList<EnergyChange>();
	
	private final Map<ChunkCoordinates, EnergyTile> registeredTiles = new HashMap<ChunkCoordinates, EnergyTile>();
	private Map<TileEntity, Integer> pendingAdds = new WeakHashMap<TileEntity, Integer>();
	private final Set<EnergyTile> removedTiles = new HashSet<EnergyTile>();
	private boolean looked = false;
	
	protected void addTileEntity(TileEntity te)
	{
		addTileEntity(te, 0);
	}
	
	protected void addTileEntity(TileEntity te, int retry)
	{
		if(!(te instanceof IEnergyTile))
		{
			logWarn("EnergyNet.addTileEntity: " + te + " doesn't implement IEnergyTile, aborting");
			return;
		}
		
		if(te.isInvalid())
		{
			logWarn("EnergyNet.addTileEntity: " + te + " is invalid (TileEntity.isInvalid()), aborting");
			return;
		}
		
		if(te.getWorldObj() != DimensionManager.getWorld(te.getWorldObj().provider.dimensionId))
		{
			logDebug("EnergyNet.addTileEntity: " + te + " is in an unloaded world, aborting");
			return;
		}
		
		if(this.looked)
		{
			logDebug("EnergyNet.addTileEntity: adding " + te + " while locked, postponing.");
			this.pendingAdds.put(te, Integer.valueOf(retry));
			return;
		}
		
		EnergyTile tile = new EnergyTile(this, te);
		
		for(ListIterator<TileEntity> it = tile.position.listIterator();it.hasNext();)
		{
			TileEntity pos = it.next();
			ChunkCoordinates coords = new ChunkCoordinates(pos.xCoord, pos.yCoord, pos.zCoord);
			EnergyTile conflicting = this.registeredTiles.get(coords);
			if(conflicting != null)
			{
				if(te == conflicting.entity)
				{
					logDebug("EnergyNet.addTileEntity: " + pos + " (" + te + ") is already added using the same position, aborting");
				}
				else if(retry < 2)
				{
					this.pendingAdds.put(te, Integer.valueOf(retry + 1));
				}
				else if((conflicting.entity.isInvalid()) || (GlobalEnergyNet.replaceConflicting()))
				{
					IC2.log.info("EnergyNet.addTileEntity: " + pos + " (" + te + ") is conflicting with " + conflicting.entity + " (invalid=" + conflicting.entity.isInvalid() + ") using the same position, which is abandoned (prev. te not removed), replacing");
					removeTileEntity(conflicting.entity);
					conflicting = null;
				}
			}
		}
		
	}
	
	protected void removeTileEntity(TileEntity te)
	{
		if(looked)
			throw new IllegalStateException("removeTileEntity isn't allowed from this context");
		
		if(!(te instanceof IEnergyTile))
		{
			logWarn("EnergyNet.removeTileEntity: " + te + " doesn't implement IEnergyTile, aborting");
			return;
		}
		List<TileEntity> positions;
		if((te instanceof IMetaDelegate))
		{
			positions = ((IMetaDelegate)te).getSubTiles();
		}
		else
		{
			positions = Arrays.asList(new TileEntity[] {te });
		}
		
		boolean wasPending = this.pendingAdds.remove(te) != null;
		
		boolean removed = false;
		
		for(TileEntity pos : positions)
		{
			ChunkCoordinates coords = new ChunkCoordinates(pos.xCoord, pos.yCoord, pos.zCoord);
			EnergyTile tile = this.registeredTiles.get(coords);
			
			if(tile == null)
			{
				if(!wasPending)
					logDebug("EnergyNet.removeTileEntity: " + pos + " (" + te + ") wasn't found (added), skipping");
			}
			else if(tile.entity != te)
			{
				logWarn("EnergyNet.removeTileEntity: " + pos + " (" + te + ") doesn't match the registered te " + tile.entity + ", skipping");
			}
			else
			{
				if(!removed)
				{
					assert (new HashSet(positions).equals(new HashSet(tile.position)));
					
					removeTileFromGrids(tile);
					removed = true;
					this.removedTiles.add(tile);
				}
				
				this.registeredTiles.remove(tile);
				
				if(te.getWorldObj().blockExists(pos.xCoord, pos.yCoord, pos.zCoord))
				{
					for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
					{
						int x = pos.xCoord + dir.offsetX;
						int y = pos.yCoord + dir.offsetY;
						int z = pos.zCoord + dir.offsetZ;
						
						if(te.getWorldObj().blockExists(x, y, z))
						{
							te.getWorldObj().notifyBlockOfNeighborChange(x, y, z, 0);
						}
					}
				}
			}
		}
	}
	
	protected void addChange(EnergyNode node, ForgeDirection dir, double amount, double voltage)
	{
		this.changes.add(new EnergyChange(node, dir, amount, voltage));
	}
	
	private void removeTileFromGrids(EnergyTile par1)
	{
		for(EnergyNode node : par1.nodes)
		{
			node.getGrid().remove(node);
		}
	}
	
	
	public TileEntity getTileEntity(int x, int y, int z)
	{
		EnergyTile ret = registeredTiles.get(new ChunkCoordinates(x, y, z));
		if(ret == null)
		{
			return null;
		}
		return ret.entity;
	}
	
	public TileEntity getNeighbor(TileEntity te, ForgeDirection dir)
	{
		return getTileEntity(te.xCoord+dir.offsetX, te.yCoord + dir.offsetY, te.zCoord + dir.offsetZ);
	}
	
	public long getTotalEnergyEmitted(TileEntity tileEntity)
	{
		EnergyTile tile = this.registeredTiles.get(new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
		if(tile == null)
		{
			return 0L;
		}
		long ret = 0L;
		
		Iterable<EnergyNodeStats> stats = tile.getStats();
		
		for(EnergyNodeStats stat : stats)
		{
			ret += stat.getEnergyOut();
		}
		
		return ret;
	}
	
	protected static int getNextEnergyGridUID()
	{
		return nextEnergyGridUid++;
	}
	
	public static int getNextEnergyNetUID()
	{
		return nextEnergyNodeUid++;
	}
	
	private void logDebug(String msg)
	{
		IC2.log.info(msg);
	}
	
	private void logWarn(String msg)
	{
		IC2.log.warning(msg);
	}

	public long getTotalSunkenEnergy(TileEntity tileEntity)
	{
		EnergyTile tile = this.registeredTiles.get(new ChunkCoordinates(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
		if(tile == null)
		{
			return 0L;
		}
		long ret = 0L;
		
		Iterable<EnergyNodeStats> stats = tile.getStats();
		
		for(EnergyNodeStats stat : stats)
		{
			ret += stat.getEnergyIn();
		}
		
		return ret;
	}
}
