package speiger.src.ic2Fixes.common.energy;

import ic2.api.Direction;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;

public class LocalEnergyNet
{
	private final World world;
	public static final double minConductionLoss = 0.0001D;
	private static final Direction[] directions = Direction.values();
	private final HashMap<IEnergySource, List<EnergyPath>> energySourceToEnergyPathMap = new HashMap<IEnergySource, List<EnergyPath>>();
	private final HashMap<EntityLivingBase, Integer> entityLivingToShockEnergyMap = new HashMap<EntityLivingBase, Integer>();
	private final HashMap<ChunkCoordinates, IEnergyTile> registeredTiles = new HashMap<ChunkCoordinates, IEnergyTile>();
	
	public LocalEnergyNet(World world)
	{
		this.world = world;
	}
	
	public void addTileEntity(TileEntity te)
	{
		if((!(te instanceof IEnergyTile)))
		{
			return;
		}
		
		ChunkCoordinates coords = getCoordsFromTileEntity(te);
		if(registeredTiles.containsKey(coords))
		{
			return;
		}
		
		registeredTiles.put(coords, (IEnergyTile)te);
		if(te instanceof IEnergyAcceptor)
		{
			List<EnergyPath> reverseEnergyPaths = discover(te, true, 2147483647);
			
			for(EnergyPath reverseEnergyPath : reverseEnergyPaths)
			{
				IEnergySource energySource = (IEnergySource)reverseEnergyPath.target;
				
				if((this.energySourceToEnergyPathMap.containsKey(energySource)) && (energySource.getOfferedEnergy() > reverseEnergyPath.loss))
				{
					this.energySourceToEnergyPathMap.remove(energySource);
				}
			}
		}
		for(ChunkCoordinates updates : registeredTiles.keySet())
		{
			world.notifyBlockOfNeighborChange(updates.posX, updates.posY, updates.posZ, 0);
		}
	}
	
	protected void removeTileEntity(TileEntity te)
	{
		ChunkCoordinates coords = getCoordsFromTileEntity(te);
		if((!(te instanceof IEnergyTile)) || (!registeredTiles.containsKey(coords)))
		{
			boolean alreadyRemoved = !registeredTiles.containsKey(coords);
			IC2.log.warning("removing " + te + " from the EnergyNet failed, already removed: " + alreadyRemoved);
			return;
		}
		
		registeredTiles.remove(coords);
		
		if(te instanceof IEnergyTile)
		{
			List<EnergyPath> reverseEnergyPaths = discover(te, true, 2147483647);
			for(EnergyPath reverseEnergyPath : reverseEnergyPaths)
			{
				IEnergySource energySource = (IEnergySource)reverseEnergyPath.target;
				if((this.energySourceToEnergyPathMap.containsKey(energySource)) && (energySource.getOfferedEnergy() > reverseEnergyPath.loss))
				{
					if(te instanceof IEnergyConductor)
					{
						this.energySourceToEnergyPathMap.remove(energySource);
					}
					else
					{
						for(Iterator<EnergyPath> it = ((List<EnergyPath>)this.energySourceToEnergyPathMap.get(energySource)).iterator();it.hasNext();)
						{
							if(it.next().target == te)
							{
								it.remove();
							}
						}
					}
				}
			}
		}
		if(te instanceof IEnergySource)
		{
			this.energySourceToEnergyPathMap.remove(te);
		}
		
		ArrayList<ChunkCoordinates> remove = new ArrayList<ChunkCoordinates>();
		for(ChunkCoordinates checks : registeredTiles.keySet())
		{
			TileEntity tile = world.getBlockTileEntity(checks.posX, checks.posY, checks.posZ);
			if(tile == null || !(tile instanceof IEnergyTile))
			{
				remove.add(checks);
			}
		}
		if(remove.size() > 0)
		{
			for(ChunkCoordinates coord : remove)
			{
				if(registeredTiles.containsKey(coord))
				{
					registeredTiles.remove(coord);
				}
			}
			remove.clear();
		}
		for(ChunkCoordinates updates : registeredTiles.keySet())
		{
			world.notifyBlockOfNeighborChange(updates.posX, updates.posY, updates.posZ, 0);
		}
		
	}
	
	private double emitEnergyFrom(IEnergySource par1, int amount)
	{
		if(!this.registeredTiles.containsKey(getCoordsFromTileEntity((TileEntity)par1)))
		{
			IC2.log.warning("EnergyNet.emitEnergyFrom: " + par1 + " is not added to the enet");
			return amount;
		}
		if(!this.energySourceToEnergyPathMap.containsKey(par1))
		{
			this.energySourceToEnergyPathMap.put(par1, discover((TileEntity)par1, false, (int)par1.getOfferedEnergy()));
		}
		List<EnergyPath> activeEnergyPaths = new Vector<EnergyPath>();
		double totalInvLoss = 0.0D;
		for(EnergyPath energyPath : (List<EnergyPath>)this.energySourceToEnergyPathMap.get(par1))
		{
			assert ((energyPath.target instanceof IEnergySink));
			
			IEnergySink energySink = (IEnergySink)energyPath.target;
			if((energySink.demandedEnergyUnits() > 0.0D) && (energyPath.loss < amount))
			{
				totalInvLoss += 1.0D / energyPath.loss;
				activeEnergyPaths.add(energyPath);
			}
		}
		Collections.shuffle(activeEnergyPaths);
		
		for(int i = activeEnergyPaths.size() - amount;i > 0;i--)
		{
			EnergyPath removedEnergyPath = activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
			totalInvLoss -= 1.0D / removedEnergyPath.loss;
		}
		
		Map<EnergyPath, Integer> suppliedEnergyPaths = new HashMap<EnergyPath, Integer>();
		new Vector();
		
		while(!(activeEnergyPaths.isEmpty()) && (amount > 0))
		{
			int energyConsumed = 0;
			double newTotalInvLoss = 0.0D;
			
			List<EnergyPath> currentActiveEnergyPaths = activeEnergyPaths;
			activeEnergyPaths = new Vector<EnergyPath>();
			
			activeEnergyPaths.iterator();
			
			for(EnergyPath energyPath : currentActiveEnergyPaths)
			{
				IEnergySink energySink = (IEnergySink)energyPath.target;
				
				int energyProvided = (int)Math.floor(Math.round(amount / totalInvLoss / energyPath.loss * 100000.0D) / 100000.0D);
				int energyLoss = (int)Math.floor(energyPath.loss);
				
				if(energyProvided > energyLoss)
				{
					double energyReturned = energySink.injectEnergyUnits(energyPath.targetDirection.toForgeDirection(), energyProvided - energyLoss);
					
					if(energyReturned == 0.0D)
					{
						activeEnergyPaths.add(energyPath);
						newTotalInvLoss += 1.0D / energyPath.loss;
					}
					else if(energyReturned >= energyProvided - energyLoss)
					{
						energyReturned = energyProvided - energyLoss;
						
						IC2.log.warning("API ERROR: " + energySink + " didn't implement demandsEnergy() properly, no energy from injectEnergy accepted although demandsEnergy() returned true.");
					}
					
					energyConsumed += energyProvided - energyReturned;
					
					int energyInjected = energyProvided - energyLoss - (int)energyReturned;
					
					if(!suppliedEnergyPaths.containsKey(energyPath))
					{
						suppliedEnergyPaths.put(energyPath, Integer.valueOf(energyInjected));
					}
					else
					{
						suppliedEnergyPaths.put(energyPath, Integer.valueOf(energyInjected + ((Integer)suppliedEnergyPaths.get(energyPath)).intValue()));
					}
				}
				else
				{
					activeEnergyPaths.add(energyPath);
					newTotalInvLoss += 1.0D / energyPath.loss;
				}
			}
			
			if((energyConsumed == 0) && (!activeEnergyPaths.isEmpty()))
			{
				EnergyPath removedEnergyPath = (EnergyPath)activeEnergyPaths.remove(activeEnergyPaths.size() - 1);
				newTotalInvLoss -= 1.0D / removedEnergyPath.loss;
			}
			
			totalInvLoss = newTotalInvLoss;
			amount -= energyConsumed;
		}
		int energyInjected;
		for(Map.Entry<EnergyPath, Integer> entry : suppliedEnergyPaths.entrySet())
		{
			EnergyPath energyPath = entry.getKey();
			energyInjected = entry.getValue().intValue();
			
			energyPath.totalEnergyConducted += energyInjected;
			if(energyInjected > energyPath.minInsulationEnergyAbsorption)
			{
				List<EntityLivingBase> entitiesNearEnergyPath = this.world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(energyPath.minX - 1, energyPath.minY - 1, energyPath.minZ - 1, energyPath.maxX + 2, energyPath.maxY + 2, energyPath.maxZ + 2));
				
				for(EntityLivingBase entityLiving : entitiesNearEnergyPath)
				{
					int maxShockEnergy = 0;
					
					for(IEnergyConductor energyConductor : energyPath.conductors)
					{
						TileEntity te = (TileEntity)energyConductor;
						if(entityLiving.boundingBox.intersectsWith(AxisAlignedBB.getBoundingBox(te.xCoord - 1, te.yCoord - 1, te.zCoord - 1, te.xCoord + 2, te.yCoord + 2, te.zCoord + 2)))
						{
							int shockEnergy = energyInjected - energyConductor.getInsulationEnergyAbsorption();
							if(shockEnergy > maxShockEnergy)
							{
								maxShockEnergy = shockEnergy;
							}
							if(energyConductor.getInsulationEnergyAbsorption() == energyPath.minInsulationEnergyAbsorption)
							{
								break;
							}
						}
					}
					if(this.entityLivingToShockEnergyMap.containsKey(entityLiving))
					{
						this.entityLivingToShockEnergyMap.put(entityLiving, Integer.valueOf(this.entityLivingToShockEnergyMap.get(entityLiving) + maxShockEnergy));
					}
					else
					{
						this.entityLivingToShockEnergyMap.put(entityLiving, Integer.valueOf(maxShockEnergy));
					}
				}
				
				if(energyInjected >= energyPath.minInsulationBreakdownEnergy)
				{
					for(IEnergyConductor energyConductor : energyPath.conductors)
					{
						if(energyInjected >= energyConductor.getInsulationBreakdownEnergy())
						{
							energyConductor.removeInsulation();
							if(energyConductor.getInsulationEnergyAbsorption() < energyPath.minInsulationEnergyAbsorption)
							{
								energyPath.minInsulationEnergyAbsorption = energyConductor.getInsulationEnergyAbsorption();
							}
						}
					}
				}
			}
			
			if(energyInjected >= energyPath.minConductorBreakdownEnergy)
			{
				for(IEnergyConductor energyConductor : energyPath.conductors)
				{
					if(energyInjected >= energyConductor.getConductorBreakdownEnergy())
					{
						energyConductor.removeConductor();
					}
				}
			}
		}
		return amount;
	}
	
	private List<EnergyPath> discover(TileEntity emitter, boolean reverse, int lossLimit)
	{
		Map<TileEntity, EnergyBlockLink> reachedTileEntities = new HashMap<TileEntity, EnergyBlockLink>();
		LinkedList<TileEntity> tileEntitiesToCheck = new LinkedList<TileEntity>();
		tileEntitiesToCheck.add(emitter);
		double currentLoss;
		
		while(!tileEntitiesToCheck.isEmpty())
		{
			TileEntity currentTileEntity = tileEntitiesToCheck.remove();
			if(!currentTileEntity.isInvalid())
			{
				currentLoss = 0.0D;
				
				if(currentTileEntity != emitter)
				{
					currentLoss = reachedTileEntities.get(currentTileEntity).loss;
				}
				List<EnergyTarget> validReceivers = getValidRecivers(currentTileEntity, reverse);
				
				for(EnergyTarget validReceiver : validReceivers)
				{
					if(validReceiver.tileEntity != emitter)
					{
						double additionalLoss = 0.0D;
						
						if((validReceiver.tileEntity instanceof IEnergyConductor))
						{
							additionalLoss = ((IEnergyConductor)validReceiver.tileEntity).getConductionLoss();
							if(additionalLoss < minConductionLoss)
							{
								additionalLoss = minConductionLoss;
							}
						}
						else if((!reachedTileEntities.containsKey(validReceiver.tileEntity)) || (((EnergyBlockLink)reachedTileEntities.get(validReceiver.tileEntity)).loss > currentLoss + additionalLoss))
						{
							reachedTileEntities.put(validReceiver.tileEntity, new EnergyBlockLink(validReceiver.direction, currentLoss + additionalLoss));
							
							if(validReceiver.tileEntity instanceof IEnergyConductor)
							{
								tileEntitiesToCheck.remove(validReceiver.tileEntity);
								tileEntitiesToCheck.add(validReceiver.tileEntity);
							}
						}
					}
				}
			}
		}
		List<EnergyPath> energyPaths = new LinkedList<EnergyPath>();
		for(Map.Entry<TileEntity, EnergyBlockLink> entry : reachedTileEntities.entrySet())
		{
			TileEntity tileEntity = entry.getKey();
			if(((!reverse) && ((tileEntity instanceof IEnergySink))) || ((reverse) && ((tileEntity instanceof IEnergySource))))
			{
				EnergyBlockLink energyBlockLink = entry.getValue();
				EnergyPath energyPath = new EnergyPath();
				
				if(energyBlockLink.loss > 0.1D)
				{
					energyPath.loss = energyBlockLink.loss;
				}
				else
				{
					energyPath.loss = 0.1D;
				}
				
				energyPath.target = tileEntity;
				energyPath.targetDirection = energyBlockLink.direction;
				
				if((!reverse) && (emitter instanceof IEnergySource))
				{
					boolean run = true;
					while(run)
					{
						tileEntity = energyBlockLink.direction.applyToTileEntity(tileEntity);
						if(tileEntity == emitter)
						{
							run = false;
							return energyPaths;
						}
						if(!(tileEntity instanceof IEnergyConductor))
						{
							break;
						}
						IEnergyConductor energyConductor = (IEnergyConductor)tileEntity;
						
						if(tileEntity.xCoord < energyPath.minX)
							energyPath.minX = tileEntity.xCoord;
						if(tileEntity.yCoord < energyPath.minY)
							energyPath.minY = tileEntity.yCoord;
						if(tileEntity.zCoord < energyPath.minZ)
							energyPath.minZ = tileEntity.zCoord;
						if(tileEntity.xCoord > energyPath.maxX)
							energyPath.maxX = tileEntity.xCoord;
						if(tileEntity.yCoord > energyPath.maxY)
							energyPath.maxY = tileEntity.yCoord;
						if(tileEntity.zCoord > energyPath.maxZ)
							energyPath.maxZ = tileEntity.zCoord;
						
						energyPath.conductors.add(energyConductor);
						
						if(energyConductor.getInsulationEnergyAbsorption() < energyPath.minInsulationEnergyAbsorption)
							energyPath.minInsulationEnergyAbsorption = energyConductor.getInsulationEnergyAbsorption();
						if(energyConductor.getInsulationBreakdownEnergy() < energyPath.minInsulationBreakdownEnergy)
							energyPath.minInsulationBreakdownEnergy = energyConductor.getInsulationBreakdownEnergy();
						if(energyConductor.getConductorBreakdownEnergy() < energyPath.minConductorBreakdownEnergy)
							energyPath.minConductorBreakdownEnergy = energyConductor.getConductorBreakdownEnergy();
						
						energyBlockLink = reachedTileEntities.get(tileEntity);
						
						if(energyBlockLink == null)
						{
							IC2.platform.displayError("An energy network pathfinding entry is corrupted.\nThis could happen due to incorrect Minecraft behavior or a bug.\n\n(Technical information: energyBlockLink, tile entities below)\nE: " + emitter + " (" + emitter.xCoord + "," + emitter.yCoord + "," + emitter.zCoord + ")\n" + "C: " + tileEntity + " (" + tileEntity.xCoord + "," + tileEntity.yCoord + "," + tileEntity.zCoord + ")\n" + "R: " + energyPath.target + " (" + energyPath.target.xCoord + "," + energyPath.target.yCoord + "," + energyPath.target.zCoord + ")");
						}
					}
					
					if(tileEntity != null)
					{
						System.out.println("EnergyNet: EnergyBlockLink corrupted (" + energyPath.target + " [" + energyPath.target.xCoord + " " + energyPath.target.yCoord + " " + energyPath.target.zCoord + "] -> " + tileEntity + " [" + tileEntity.xCoord + " " + tileEntity.yCoord + " " + tileEntity.zCoord + "] -> " + emitter + " [" + emitter.xCoord + " " + emitter.yCoord + " " + emitter.zCoord + "])");
					}
				}
				else
				{
					energyPaths.add(energyPath);
				}
			}
		}
		return energyPaths;
	}
	
	private List<EnergyTarget> getValidRecivers(TileEntity emitter, boolean reverse)
	{
		List<EnergyTarget> validReceivers = new LinkedList<EnergyTarget>();
		for(Direction direction : directions)
		{
			TileEntity target = direction.applyToTileEntity(emitter);
			if((target instanceof IEnergyTile) && registeredTiles.containsKey(getCoordsFromTileEntity(target)))
			{
				Direction inverseDirection = direction.getInverse();
				
				if(((!reverse) && (emitter instanceof IEnergyEmitter)) && (((IEnergyEmitter)emitter).emitsEnergyTo(target, direction.toForgeDirection())) || ((reverse) && ((emitter instanceof IEnergyAcceptor)) && (((IEnergyAcceptor)emitter).acceptsEnergyFrom(target, direction.toForgeDirection()))))
				{
					if(((!reverse) && ((target instanceof IEnergyAcceptor)) && (((IEnergyAcceptor)target).acceptsEnergyFrom(emitter, inverseDirection.toForgeDirection()))) || ((reverse) && ((target instanceof IEnergyEmitter)) && (((IEnergyEmitter)target).emitsEnergyTo(emitter, inverseDirection.toForgeDirection()))))
					{
						validReceivers.add(new EnergyTarget(target, inverseDirection));
					}
				}
			}
		}
		
		return validReceivers;
	}
	
	private ChunkCoordinates getCoordsFromTileEntity(TileEntity tile)
	{
		return new ChunkCoordinates(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public TileEntity getTileEntity(int x, int y, int z)
	{
		IEnergyTile ret = registeredTiles.get(new ChunkCoordinates(x, y, z));
		if(ret == null)
		{
			return null;
		}
		return (TileEntity)ret;
	}
	
	public TileEntity getNeighbor(TileEntity te, ForgeDirection dir)
	{
		return getTileEntity(te.xCoord + dir.offsetX, te.yCoord + dir.offsetY, te.zCoord + dir.offsetZ);
	}
	
	public long getTotalEnergyEmitted(TileEntity te)
	{
		long ret = 0L;
		
		if(te instanceof IEnergyConductor)
		{
			List<EnergyPath> reverseEnergyPaths = discover(te, true, 2147483647);
			for(EnergyPath reverseEnergyPath : reverseEnergyPaths)
			{
				IEnergySource energySource = (IEnergySource)reverseEnergyPath.target;
				if((this.energySourceToEnergyPathMap.containsKey(energySource)) && (energySource.getOfferedEnergy() > reverseEnergyPath.loss))
				{
					for(EnergyPath energyPath : (List<EnergyPath>)this.energySourceToEnergyPathMap.get(energySource))
					{
						if((te instanceof IEnergyConductor) && (energyPath.conductors.contains(te)))
						{
							ret += energyPath.totalEnergyConducted;
						}
					}
				}
			}
		}
		if((te instanceof IEnergySource) && (this.energySourceToEnergyPathMap.containsKey(te)))
		{
			for(EnergyPath energyPath : (List<EnergyPath>)this.energySourceToEnergyPathMap.get(te))
			{
				ret += energyPath.totalEnergyConducted;
			}
		}
		FMLLog.getLogger().info("Requested:" +ret);
		return ret;
	}
	
	public long getTotalSunkenEnergy(TileEntity tileEntity)
	{
		long ret = 0L;
		if(((tileEntity instanceof IEnergyConductor)) || ((tileEntity instanceof IEnergySink)))
		{
			List<EnergyPath> reverseEnergyPaths = discover(tileEntity, true, 2147483647);
			for(EnergyPath reverseEnergyPath : reverseEnergyPaths)
			{
				IEnergySource energySource = (IEnergySource)reverseEnergyPath.target;
				if((this.energySourceToEnergyPathMap.containsKey(energySource)) && (energySource.getOfferedEnergy() > reverseEnergyPath.loss))
				{
					for(EnergyPath energyPath : (List<EnergyPath>)this.energySourceToEnergyPathMap.get(energySource))
					{
						if((((tileEntity instanceof IEnergySink)) && (energyPath.target == tileEntity)) || (((tileEntity instanceof IEnergyConductor)) && (energyPath.conductors.contains(tileEntity))))
						{
							ret += energyPath.totalEnergyConducted;
						}
					}
				}
			}
		}
		
		return ret;
	}
	
	public void onTickEnd()
	{
		IC2.platform.profilerEndStartSection("Energy Extraction");
		for(IEnergyTile tiles : registeredTiles.values())
		{
			if(tiles instanceof IEnergySource)
			{
				IEnergySource source = (IEnergySource)tiles;
				double removed = source.getOfferedEnergy() - this.emitEnergyFrom(source, (int)source.getOfferedEnergy()) ;
				if(removed > 0.0D)
				{
					source.drawEnergy(removed);
				}
			}
		}
		IC2.platform.profilerEndSection();
	}
	
	public void onTickStart()
	{
		IC2.platform.profilerStartSection("Shocking");
		for(Map.Entry<EntityLivingBase, Integer> entry : entityLivingToShockEnergyMap.entrySet())
		{
			EntityLivingBase target = entry.getKey();
			int damage = (entry.getValue().intValue() + 63) / 64;
			
			if(target.isEntityAlive())
			{
				target.attackEntityFrom(IC2DamageSource.electricity, damage);
			}
		}
		
		entityLivingToShockEnergyMap.clear();
		
		IC2.platform.profilerEndSection();
	}
	
}
