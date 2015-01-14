package speiger.src.spmodapi.client.plugins;

import speiger.src.spmodapi.common.plugins.PluginLoader;
import speiger.src.spmodapi.common.plugins.nei.PluginNei;
import cpw.mods.fml.common.Loader;

public class PluginLoaderClient extends PluginLoader
{

	@Override
	public void loadModAdditions()
	{
		super.loadModAdditions();
		if(Loader.isModLoaded("NotEnoughItems"))
		{
			PluginNei.init();
		}
	}
	
}
