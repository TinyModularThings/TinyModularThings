package speiger.src.spmodapi.common.plugins;

import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.plugins.BC.BCAddon;
import speiger.src.spmodapi.common.plugins.IC2.IC2Addon;
import speiger.src.spmodapi.common.plugins.forestry.AddonForestry;
import speiger.src.spmodapi.common.plugins.minefactoryReloaded.MineFactoryReloadedAddon;
import speiger.src.spmodapi.common.recipes.helper.RecipeOverrider;
import speiger.src.spmodapi.common.util.data.AccessConfig;
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
		AccessConfig config = new AccessConfig(SpmodConfig.getInstance());
		config.loadLaterData();
		
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
		
	}
	
}
