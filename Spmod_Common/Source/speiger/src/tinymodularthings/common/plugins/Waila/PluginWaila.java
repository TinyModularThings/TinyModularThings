package speiger.src.tinymodularthings.common.plugins.Waila;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.Waila.core.WailaRegistry;
import cpw.mods.fml.common.Loader;

public class PluginWaila implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("Waila");
	}
	
	@Override
	public void init()
	{
		WailaRegistry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
