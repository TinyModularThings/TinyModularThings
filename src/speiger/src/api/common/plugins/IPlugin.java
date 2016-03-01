package speiger.src.api.common.plugins;

import cpw.mods.fml.relauncher.Side;

public interface IPlugin
{
	public boolean isSideSupported(Side par1);
	
	public boolean canLoad();
	
	public void preInit();
	
	public void init();
	
	public void postInit();
	
	public String getPluginName();
}
