package speiger.src.tinymodularthings.common.plugins.Nei;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.Nei.core.NeiRegistry;

public class PluginNei implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("NotEnoughItems");
	}
	
	@Override
	public void init()
	{
		NeiRegistry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
