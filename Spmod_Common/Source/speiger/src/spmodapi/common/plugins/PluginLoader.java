package speiger.src.spmodapi.common.plugins;

import speiger.src.spmodapi.common.plugins.BC.BCAddon;
import speiger.src.spmodapi.common.plugins.IC2.IC2Addon;
import speiger.src.spmodapi.common.plugins.RailCraft.PluginRailcraft;
import speiger.src.spmodapi.common.plugins.forestry.AddonForestry;
import speiger.src.spmodapi.common.plugins.minefactoryReloaded.MineFactoryReloadedAddon;
import speiger.src.spmodapi.common.recipes.helper.RecipeOverrider;
import cpw.mods.fml.common.Loader;

public class PluginLoader
{
	public void loadModAdditions()
	{
		try
		{
			RecipeOverrider.loadTransmutationRecipes();
			
		}
		catch (Exception e)
		{
			
		}
		
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
		if(Loader.isModLoaded("Railcraft"))
		{
			PluginRailcraft.init();
		}

	}
	
	
	
	
	
}
