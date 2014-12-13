package speiger.src.ic2Fixes.common.energy;

public class EnergyNodeStats
{
	protected double energyIn;
	protected double energyOut;
	protected double voltage;
	
	public EnergyNodeStats(double energyIn, double energyOut, double voltage)
	{
		this.energyIn = energyIn;
		this.energyOut = energyOut;
		this.voltage = voltage;
	}
	
	public double getEnergyIn()
	{
		return energyIn;
	}
	
	public double getEnergyOut()
	{
		return energyOut;
	}
	
	public double getVoltage()
	{
		return voltage;
	}
}
