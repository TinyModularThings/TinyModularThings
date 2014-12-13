package speiger.src.ic2Fixes.common.energy;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;

import java.util.ArrayList;
import java.util.List;


public class EnergyNode
{
	final int uid;
	final EnergyTile tile;
	final EnergyNodeType nodeType;
	private final EnergyNode parent;
	private boolean isExtraNode = false;
	private int tier;
	private double amount;
	private double voltage;
	private double currentIn;
	private double currentOut;
	private EnergyGrid grid;

	List<EnergyNodeLink> links = new ArrayList<EnergyNodeLink>();
	
	private final MutableEnergyNodeStats lastNodeStats = new MutableEnergyNodeStats();

	EnergyNode(LocalEnergyNet par1, EnergyTile par2, EnergyNodeType par3)
	{
		if(par1 == null) throw new NullPointerException("The energyNet parameter must not be null.");
		if(par2 == null) throw new NullPointerException("The tile parameter must not be null.");
		assert ((par3 != EnergyNodeType.Conductor) || (par2.entity instanceof IEnergyConductor));
		assert ((par3 != EnergyNodeType.Sink) || (par2.entity instanceof IEnergySink));
		assert ((par3 != EnergyNodeType.Source) || (par2.entity instanceof IEnergySource));
		
		this.uid = LocalEnergyNet.getNextEnergyNetUID();
		this.tile = par2;
		this.nodeType = par3;
		this.parent = null;
	}
	
	EnergyNode(EnergyNode par1)
	{
		this.uid = par1.uid;
		this.tile = par1.tile;
		this.nodeType = par1.nodeType;
		this.parent = par1;
		setGrid(par1.getGrid());
		
		assert ((this.nodeType != EnergyNodeType.Conductor) || ((this.tile.entity instanceof IEnergyConductor)));
		assert ((this.nodeType != EnergyNodeType.Sink) || ((this.tile.entity instanceof IEnergySink)));
		assert ((this.nodeType != EnergyNodeType.Source) || ((this.tile.entity instanceof IEnergySource)));
		
		for(EnergyNodeLink link : par1.links)
		{
			assert (link.getNeighbor(par1).links.contains(link));
			
			this.links.add(new EnergyNodeLink(link));
		}
	}
	
	double getInnerLoss()
	{
		switch(EnergyNodeType.values()[nodeType.ordinal()])
		{
			case Sink: return 0.4D;
			case Source: return 0.4D;
			case Conductor: return ((IEnergyConductor)this.tile.entity).getConductionLoss();	
		}
		throw new RuntimeException("invalid nodetype: " + this.nodeType);
	}

	boolean isExtraNode()
	{
		return getTop().isExtraNode;
	}
	
	void setExtraNode(boolean isExtraNode)
	{
		if(this.nodeType == EnergyNodeType.Conductor)throw new IllegalStateException("A conductor can't be an extra node.");
		
		getTop().isExtraNode = isExtraNode;
	}
	
	int getTier()
	{
		return getTop().tier;
	}
	
	void setTier(int tier)
	{
		if((tier < 0) || (Double.isNaN(tier)))
		{
			tier = 0;
		}
		else if(tier > 20)
		{
			tier = 20;
		}
		getTop().tier = tier;
	}
	
	double getAmount()
	{
		return getTop().amount;
	}
	
	void setAmount(double amount)
	{
		getTop().amount = amount;
	}
	
	double getVoltage()
	{
		return getTop().voltage;
	}
	
	void setVoltage(double voltage)
	{
		getTop().voltage = voltage;
	}
	
	double getSourceResistance()
	{
		assert (this.nodeType == EnergyNodeType.Source);
		
		double ret = EnergyNet.instance.getPowerFromTier(getTier()) * 0.0625D;
		assert ((ret > 0.0D) && (!Double.isInfinite(ret)) && (!Double.isNaN(ret)));
		
		return ret;
	}
	
	double getSinkResistance()
	{
		assert (this.nodeType == EnergyNodeType.Sink);
		
		double ret = EnergyNet.instance.getPowerFromTier(getTier()) * 1.0D;
		assert ((ret > 0.0) && (!Double.isInfinite(ret)) && (!Double.isNaN(ret)));
		
		return ret;
	}
	
	double getMaxCurrent()
	{
		return this.tile.maxCurrent;
	}
	
	void resetCurrents()
	{
		getTop().currentIn = 0.0D;
		getTop().currentOut = 0.0D;
	}
	
	void addCurrent(double current)
	{
		if(current >= 0.0D)
		{
			getTop().currentIn += current;
		}
		else
		{
			getTop().currentOut += -currentIn;
		}
	}
	
	public String toString()
	{
		String type = null;
		
		switch(EnergyNodeType.values()[nodeType.ordinal()])
		{
			case Conductor: type = "C";
			case Sink: type = "A";
			case Source: type = "E";
		}
		return tile.entity.getClass().getSimpleName().replace("TileEntity", "") + "|" + tier + "|" + this.uid;
	}
	
	EnergyNode getTop()
	{
		if(this.parent != null)
		{
			return parent.getTop();
		}
		return this;
	}
	
	EnergyNodeLink getConnectionTo(EnergyNode node)
	{
		for(EnergyNodeLink link : links)
		{
			if(link.getNeighbor(this) == node) return link;
		}
		return null;
	}
	
	EnergyNodeStats getStats()
	{
		return this.lastNodeStats;
	}
	
	void updateStats()
	{
		if(LocalEnergyNet.useLinearTransfering)
		{
			this.lastNodeStats.set(currentIn * voltage, currentOut * voltage, voltage);
		}
		else
		{
			this.lastNodeStats.set(currentIn, currentOut, voltage);
		}
	}
	
	EnergyGrid getGrid()
	{
		return getTop().grid;
	}
	
	void setGrid(EnergyGrid par1)
	{
		this.grid = par1;
	}
}
