package speiger.src.api.common.registry.helpers;

public interface IPlugin
{
	/**
	 * Just a check if the Plugin can Load.
	 * False can just ignores it.
	 */
	public boolean canLoad();
	
	/**
	 * Initing Function
	 */
	public void init();
	
	public Object getForgeClasses();
	
}
