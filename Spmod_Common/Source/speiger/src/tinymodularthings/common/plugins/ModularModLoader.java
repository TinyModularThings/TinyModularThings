package speiger.src.tinymodularthings.common.plugins;

import speiger.src.tinymodularthings.common.config.TinyItemsConfig;
import speiger.src.tinymodularthings.common.plugins.BC.BCRegistry;
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
				TinyItemsConfig.onPipeLoad();
			}
			catch (Exception e)
			{
				FMLLog.getLogger().info("Loading failed: "+e);
				for(StackTraceElement el : e.getStackTrace())
				{
					FMLLog.getLogger().info(""+el);
				}
			}
		}
		
		try
		{
//			RecipeOverrider.loadTransmutationRecipes();
		}
		catch (Exception e)
		{
			
		}
	}
}
