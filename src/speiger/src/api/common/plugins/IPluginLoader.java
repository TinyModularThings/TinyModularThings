package speiger.src.api.common.plugins;

import java.io.File;

public interface IPluginLoader
{
	/**
	 * @param pathNames based on the path you use for Creation
	 */
	public void loadPlugin(String...pathNames);
	
	public boolean containsPlugin(String pluginName);
	
	public IPlugin getPlugin(String pluginName);
	
	public void removePlugin(String name);
	
	public void preInit();
	
	public void init();
	
	public void postInit();
}
