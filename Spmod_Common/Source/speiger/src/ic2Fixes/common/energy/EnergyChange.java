package speiger.src.ic2Fixes.common.energy;

import net.minecraftforge.common.ForgeDirection;

public class EnergyChange
{
	EnergyNode node;
	final ForgeDirection dir;
	private double amount;
	private double voltage;
	
	EnergyChange(EnergyNode node, ForgeDirection dir, double amount, double voltage)
	{
		this.node = node;
		this.dir = dir;
		setAmount(amount);
		setVoltage(voltage);
	}
	
	@Override
	public String toString()
	{
		return this.node + "@" + this.dir + " " + amount + " EU / " + voltage + " V";
	}
	
	double getAmount()
	{
		return this.amount;
	}
	
	void setAmount(double amount)
	{
		double intAmount = Math.rint(amount);
		if(Math.abs(amount - intAmount) < 0.001D)
			amount = intAmount;
		
		assert ((!Double.isInfinite(amount)) && (!Double.isNaN(amount)));
		
		this.amount = amount;
	}
	
	double getVoltage()
	{
		return this.voltage;
	}
	
	private void setVoltage(double voltage)
	{
		double intVoltage = Math.rint(this.amount);
		if(Math.abs(voltage - intVoltage) < 0.001D)
			voltage = intVoltage;
		
		assert ((!Double.isInfinite(voltage)) && (!Double.isNaN(voltage)));
		
		this.voltage = voltage;
		
	}
	
}
