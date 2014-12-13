package speiger.src.ic2Fixes.common.energy;

public class MutableEnergyNodeStats extends EnergyNodeStats
{

	public MutableEnergyNodeStats()
	{
		super(0.0D, 0.0D, 0.0D);
	}
	
	protected void set(double energyIn, double energyOut, double voltage)
	{
		this.energyIn = energyIn;
		this.energyOut = energyOut;
		this.voltage = voltage;
	}
	
}
