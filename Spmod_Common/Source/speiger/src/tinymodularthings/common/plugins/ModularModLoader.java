package speiger.src.tinymodularthings.common.plugins;

import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.config.TinyItemsConfig;
import speiger.src.tinymodularthings.common.plugins.BC.BCRegistry;
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
			try
			{
				if (TinyConfig.pipesEnabled)
				{
					TinyItemsConfig.onPipeLoad();
				}
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Loading failed: " + e);
				for (StackTraceElement el : e.getStackTrace())
				{
					FMLLog.getLogger().info("" + el);
				}
			}
		}
		
		if (Loader.isModLoaded("Forestry"))
		{
			ForestryPlugin.initForestryStuff();
		}
		
		try
		{
			// RecipeOverrider.loadTransmutationRecipes();
		}
		catch (Exception e)
		{
			
		}
	}
}
