package speiger.src.spmodapi.client.plugins;

import java.util.List;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.plugins.PluginLoader;

public class PluginLoaderClient extends PluginLoader
{

	@Override
	public void loadModAdditions()
	{
		super.loadModAdditions();
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return;
		}
		List<IPlugin> plugins = getPlugins("Nei");
		for(IPlugin plugin : plugins)
		{
			initPlugin(plugin);
		}
	}
	
}
