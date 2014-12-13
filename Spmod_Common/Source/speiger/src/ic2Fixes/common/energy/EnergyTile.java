package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;

public class EnergyTile
{
	final TileEntity entity;
	final List<TileEntity> position;
	final List<EnergyNode> nodes = new ArrayList<EnergyNode>();
	final double maxCurrent;
	
	EnergyTile(LocalEnergyNet par1, TileEntity tile)
	{
		entity = tile;
		if(tile instanceof IMetaDelegated)
		{
			position = new ArrayList(((IMetaDelegated)tile).getSubTiles());
			if(position.isEmpty())
			{
				throw new RuntimeException("Tile entity " + tile + " must return at least 1 sub tile for IMetaDelegate.getSubTiles()."); 
			}
		}
		else
		{
			position = Arrays.asList(new TileEntity[]{tile});
		}
		
		if((tile instanceof IEnergySource))
		{
			this.nodes.add(new EnergyNode(par1, this, EnergyNodeType.Source));
			maxCurrent = ((IEnergyConductor)tile).getConductorBreakdownEnergy();
		}
		else
		{
			this.maxCurrent = 1.7976931348623157E+308D;
		}
	}
	
	void addExtraNode(EnergyNode node)
	{
		node.setExtraNode(true);
		nodes.add(node);
	}
	
	boolean removeExtraNode(EnergyNode node)
	{
		boolean canBeRemoved = false;
		if(node.isExtraNode())
		{
			canBeRemoved = true;
		}
		else
		{
			for(EnergyNode otherNode : nodes)
			{
				if((otherNode != node) && (otherNode.nodeType == node.nodeType) && (otherNode.isExtraNode()))
				{
					otherNode.setExtraNode(false);
					canBeRemoved = true;
					
					break;
				}
			}
		}
		
		if(canBeRemoved)
		{
			nodes.remove(node);
			return true;
		}
		return false;
	}
	
	TileEntity getSubEntityAt(ChunkCoordinates coords)
	{
		for(TileEntity te : position)
		{
			if((te.xCoord == coords.posX) && (te.yCoord == coords.posY) && (te.zCoord == coords.posZ))
			{
				return te;
			}
		}
		return null;
	}
	
	Iterable<EnergyNodeStats> getStats()
	{
		List<EnergyNodeStats> ret = new ArrayList<EnergyNodeStats>(nodes.size());
		
		for(EnergyNode node : nodes)
		{
			ret.add(node.getStats());
		}
		
		return ret;
	}
}
