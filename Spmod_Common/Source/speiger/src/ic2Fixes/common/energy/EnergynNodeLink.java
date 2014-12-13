package speiger.src.ic2Fixes.common.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;

class EnergyNodeLink
{
	EnergyNode nodeA;
	EnergyNode nodeB;
	ForgeDirection dirFromA;
	ForgeDirection dirFromB;
	double loss;
	List<EnergyNode> skippedNodes = new ArrayList<EnergyNode>();
	
	EnergyNodeLink(EnergyNode nodeA1, EnergyNode nodeB1, double loss1)
	{
		this(nodeA1, nodeB1, loss1, null, null);
//		calculateDirections();
	}
	
	EnergyNodeLink(EnergyNodeLink link)
	{
		this(link.nodeA, link.nodeB, link.loss, link.dirFromA, link.dirFromB);
		
		skippedNodes.addAll(link.skippedNodes);
	}
	
	private EnergyNodeLink(EnergyNode nodeA1, EnergyNode nodeB1, double loss1, ForgeDirection dirFromA, ForgeDirection dirFromB)
	{
		assert (nodeA1 != nodeB1);
		
		this.nodeA = nodeA1;
		this.nodeB = nodeB1;
		this.loss = loss1;
		this.dirFromA = dirFromA;
		this.dirFromB = dirFromB;
	}
	
	EnergyNode getNeighbor(EnergyNode par1)
	{
		if(this.nodeA == par1)
		{
			return this.nodeB;
		}
		return this.nodeA;
	}
	
	EnergyNode getNeighbor(int uid)
	{
		if(this.nodeA.uid == uid)
		{
			return this.nodeB;
		}
		return this.nodeA;
	}
	
	void replaceNode(EnergyNode oldNode, EnergyNode newNode)
	{
		if(this.nodeA == oldNode)
		{
			this.nodeA = newNode;
		}
		else if(this.nodeB == oldNode)
		{
			this.nodeB = newNode;
		}
		else
		{
			 throw new IllegalArgumentException("Node " + oldNode + " isn't in " + this + ".");
		}
	}
	
	ForgeDirection getDirFrom(EnergyNode node)
	{
		if(this.nodeA == node)
		{
			return this.dirFromA;
		}
		if(this.nodeB == node)
		{
			return this.dirFromB;
		}
		return null;
	}
	
	void updateCurrent()
	{
		assert (!Double.isNaN(this.nodeA.getVoltage()));
	}
}
