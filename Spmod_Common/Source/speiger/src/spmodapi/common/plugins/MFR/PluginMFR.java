package speiger.src.spmodapi.common.plugins.MFR;

import cpw.mods.fml.common.Loader;
import speiger.src.api.common.registry.helpers.IPlugin;
import speiger.src.spmodapi.common.plugins.MFR.core.MFRAddon;

public class PluginMFR implements IPlugin
{

	@Override
	public boolean canLoad()
	{
		return Loader.isModLoaded("MFReloaded");
	}

	@Override
	public void init()
	{
		MFRAddon.init();
	}

	@Override
	public Object getForgeClasses()
	{
		return null;
	}
	
}
