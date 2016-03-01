package speiger.src.api.common.core;


public interface SpmodMod
{	
	
	/**
	 * @return Logging name
	 */
	public String getModName();
	
	/**
	 * @return custom Logger
	 * @Default can be found in the registry
	 */
	public LogProxy getLogger();
	
}
