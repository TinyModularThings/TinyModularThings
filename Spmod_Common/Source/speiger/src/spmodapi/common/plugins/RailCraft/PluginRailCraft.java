package speiger.src.spmodapi.common.plugins.RailCraft;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.plugins.RailCraft.core.RailCraftAddon;

public class PluginRailCraft implements IPlugin
{
	
	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("Railcraft");
	}
	
	@Override
	public void init()
	{
		RailCraftAddon.init();
	}
	
	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
