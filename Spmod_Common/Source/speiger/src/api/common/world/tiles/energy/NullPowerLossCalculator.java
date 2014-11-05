package speiger.src.api.common.world.tiles.energy;

import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PerditionCalculator;

public class NullPowerLossCalculator extends PerditionCalculator
{
	
	@Override
	public float applyPerdition(PowerHandler powerHandler, float current, long ticksPassed)
	{
		float energy = current;
		if (energy <= 0)
		{
			energy = 0;
		}
		energy -= 0.000001;
		// FMLLog.getLogger().info("Test");
		return energy;
	}
	
}
