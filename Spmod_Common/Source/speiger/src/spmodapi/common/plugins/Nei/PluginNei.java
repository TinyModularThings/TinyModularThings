package speiger.src.spmodapi.common.plugins.Nei;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.plugins.Nei.core.NeiAddon;

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
		NeiAddon.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
