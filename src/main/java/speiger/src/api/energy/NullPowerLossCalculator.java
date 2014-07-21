package speiger.src.api.energy;

import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PerditionCalculator;

public class NullPowerLossCalculator extends PerditionCalculator
{
	
	@Override
	public double applyPerdition(PowerHandler powerHandler, double current, long ticksPassed)
	{
		double energy = current;
		if (energy <= 0)
		{
			energy = 0;
		}
		energy -= 0.000001;
		// FMLLog.getLogger().info("Test");
		return energy;
	}
	
}
