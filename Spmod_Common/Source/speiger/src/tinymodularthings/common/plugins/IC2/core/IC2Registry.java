package speiger.src.tinymodularthings.common.plugins.IC2.core;

import cpw.mods.fml.common.Loader;

public class IC2Registry
{
	public static void init()
	{
		if(Loader.isModLoaded("factorization"))
		{
			IC2IridiumPlugin.init();
		}
	}
}
