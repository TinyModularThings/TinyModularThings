package speiger.src.tinymodularthings.common.plugins.IC2.core;

import ic2.api.info.IC2Classic;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import cpw.mods.fml.common.Loader;

public class IC2Registry
{
	public static void init()
	{
		if(Loader.isModLoaded("factorization"))
		{
			IC2IridiumPlugin.init();
		}
		if(IC2Classic.isIc2ClassicLoaded())
		{
			PressureRecipeList.getInstance().addRecipe(new RedefinedIron());
		}
	}
}
