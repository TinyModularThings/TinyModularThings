package speiger.src.api.common.plugins;

import cpw.mods.fml.common.FMLLog;
import speiger.src.api.common.core.SpmodMod;
import speiger.src.api.common.utils.misc.DataMap;

public class PluginManager
{
	private static DataMap<IPluginLoader> pluginLoaders = new DataMap<IPluginLoader>();
	
	/**
	 * @param path is the Path where he should search for
	 * @param name is the ID
	 * @param mod is the Modowner. (Simply for logging)
	 * @return the Created Plugin
	 */
	public static IPluginLoader createPluginLoader(String path, String name, SpmodMod mod)
	{
		IPluginLoader loader = null;
		try
		{
			loader = (IPluginLoader)PluginManager.class.getClassLoader().loadClass("speiger.src.spmodapi.common.utils.misc.PluginLoader").getConstructor(String.class, SpmodMod.class).newInstance(path, mod);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(loader != null)
		{
			pluginLoaders.addData(name, loader);
		}
		return loader;
	}
	
	public static IPluginLoader getPluginLoader(String name)
	{
		return pluginLoaders.getData(name);
	}
	
	public static void removePluginLoader(String name)
	{
		pluginLoaders.remove(name);
	}
	
}
