package speiger.src.tinymodularthings.common.plugins;

import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.config.TinyItemsConfig;
import speiger.src.tinymodularthings.common.plugins.BC.BCRegistry;
import speiger.src.tinymodularthings.common.plugins.Railcraft.PluginRailcraft;
import speiger.src.tinymodularthings.common.plugins.forestry.ForestryPlugin;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;

public class ModularModLoader
{
	public static void LoadAddons()
	{
		if (Loader.isModLoaded("BuildCraft|Core"))
		{
			BCRegistry.load();
		}
		
		if (Loader.isModLoaded("Forestry"))
		{
			ForestryPlugin.initForestryStuff();
		}
		
		if(Loader.isModLoaded("Railcraft"))
		{
			PluginRailcraft.init();
		}
		
		try
		{
			// RecipeOverrider.loadTransmutationRecipes();
		}
		catch (Exception e)
		{
			
		}
	}

	public static void LoadAddonsSave()
	{
		try
		{
			LoadAddons();
		}
		catch(Exception e)
		{
		}
	}
}
