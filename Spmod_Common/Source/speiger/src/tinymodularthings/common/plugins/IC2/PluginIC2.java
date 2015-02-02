package speiger.src.tinymodularthings.common.plugins.IC2;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.IC2.core.IC2Registry;
import cpw.mods.fml.common.Loader;

public class PluginIC2 implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("IC2");
	}
	
	@Override
	public void init()
	{
		IC2Registry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
