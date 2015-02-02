package speiger.src.tinymodularthings.common.plugins.Forestry;

import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.tinymodularthings.common.plugins.Forestry.core.ForestryRegistry;
import cpw.mods.fml.common.Loader;

public class PluginForestry implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("Forestry");
	}
	
	@Override
	public void init()
	{
		ForestryRegistry.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
