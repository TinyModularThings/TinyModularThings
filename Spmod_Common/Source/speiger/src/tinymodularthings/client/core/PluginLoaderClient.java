package speiger.src.tinymodularthings.client.core;

import java.util.List;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.PluginLoader;

public class PluginLoaderClient extends PluginLoader
{

	@Override
	public void loadPlugins()
	{
		super.loadPlugins();
		List<IPlugin> plugins = getPlugins("Nei", "Waila");
		for(IPlugin plugin : plugins)
		{
			initPlugin(plugin);
		}
	}
	
}
