package speiger.src.tinymodularthings.common.plugins.Railcraft;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.Railcraft.core.RailcraftRegistry;

public class PluginRailcraft implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("Railcraft");
	}
	
	@Override
	public void init()
	{
		RailcraftRegistry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
