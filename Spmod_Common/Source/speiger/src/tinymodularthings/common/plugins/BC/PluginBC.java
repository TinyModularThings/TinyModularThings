package speiger.src.tinymodularthings.common.plugins.BC;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.BC.core.BCRegistry;

public class PluginBC implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("BuildCraft|Core");
	}
	
	@Override
	public void init()
	{
		BCRegistry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
