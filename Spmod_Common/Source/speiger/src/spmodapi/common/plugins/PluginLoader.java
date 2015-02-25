package speiger.src.spmodapi.common.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.recipes.helper.RecipeOverrider;

public class PluginLoader
{
	static Map<String, IPlugin> loadedPlugins = new HashMap<String, IPlugin>();
	
	public void loadModAdditions()
	{
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return;
		}
		
		List<IPlugin> plugins = getPlugins("BC", "Forestry", "IC2", "MFR", "RailCraft");
		for(IPlugin plug : plugins)
		{
			initPlugin(plug);
		}
		
		try
		{
			RecipeOverrider.loadTransmutationRecipes();
		}
		catch(Exception e)
		{
			
		}


	}
	
	public boolean isPluginLoaded(String plugin)
	{
		return loadedPlugins.containsKey(plugin);
	}
	
	public IPlugin getPlugin(String plugin)
	{
		return loadedPlugins.get(plugin);
	}
	
	public void initPlugin(IPlugin plugin)
	{
		initPluginForge(plugin.getForgeClasses());
		plugin.init();
	}
	
	public void initPluginForge(Object par1)
	{
		if(par1 == null)
		{
			return;
		}
		Object[] array = null;
		if(par1 instanceof Object[])
		{
			array = (Object[])par1;
		}
		else
		{
			array = new Object[] {par1};
		}
		for(Object obj : array)
		{
			MinecraftForge.EVENT_BUS.register(obj);
		}
	}
	
	public List<IPlugin> getPlugins(String... par1)
	{
		ArrayList<IPlugin> plugin = new ArrayList<IPlugin>();
		for(String key : par1)
		{
			try
			{
				SpmodAPI.log.print("Load Current Plugin: " + key);
				Class clz = PluginLoader.class.getClassLoader().loadClass("speiger.src.spmodapi.common.plugins." + key + ".Plugin" + key);
				if(clz != null)
				{
					IPlugin plug = (IPlugin)clz.newInstance();
					if(plug.canLoad())
					{
						SpmodAPI.log.print("Loaded Current Plugin: " + key);
						plugin.add(plug);
						loadedPlugins.put(key, plug);
					}
					else
					{
						SpmodAPI.log.print("Current Plugin is Dissabled: " + key);
					}
				}
			}
			catch(Exception e)
			{
				SpmodAPI.log.print("Could not Load Current Plugin: " + key);
			}
		}
		return plugin;
	}
}
