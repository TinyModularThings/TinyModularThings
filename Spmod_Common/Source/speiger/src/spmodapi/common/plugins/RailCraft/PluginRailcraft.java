package speiger.src.spmodapi.common.plugins.RailCraft;

import mods.railcraft.api.fuel.FuelManager;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;

public class PluginRailcraft
{
	public static void init()
	{
		FuelManager.addBoilerFuel(APIUtils.animalGas, 20000);
	}
}
