package speiger.src.spmodapi.common.plugins.IC2;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.plugins.IC2.core.IC2Addon;

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
		IC2Addon.init();
	}

	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
