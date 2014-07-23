package speiger.src.spmodapi.common.modHelper;

import speiger.src.spmodapi.common.modHelper.BC.BCAddon;
import speiger.src.spmodapi.common.modHelper.IC2.IC2Addon;
import speiger.src.spmodapi.common.modHelper.forestry.AddonForestry;
import speiger.src.spmodapi.common.modHelper.minefactoryReloaded.MineFactoryReloadedAddon;
import speiger.src.spmodapi.common.recipes.helper.RecipeOverrider;
import cpw.mods.fml.common.Loader;

public class ModHelperLoader
{
	public static void loadModAdditions()
	{
		if (Loader.isModLoaded("Forestry"))
		{
			AddonForestry.loadForestryStuff();
		}
		if (Loader.isModLoaded("MFReloaded"))
		{
			MineFactoryReloadedAddon.loadMFR();
		}
		if (Loader.isModLoaded("IC2"))
		{
			IC2Addon.LoadIC2();
		}
		if (Loader.isModLoaded("BuildCraft|Core"))
		{
			BCAddon.loadBC();
		}
		
		try
		{
			RecipeOverrider.loadTransmutationRecipes();
		}
		catch (Exception e)
		{
			
		}
	}
	
}
