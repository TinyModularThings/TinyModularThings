package speiger.src.api.util;

public interface SpmodMod
{
	
	/**
	 *  Mod Name
	 * 	Has to be stable
	 **/
	public String getName();
	
	/** Language PackFolder **/
	public String getLangFolder();
	
	/** Logger. Not Required **/
	public LogProxy getLogger();
	
}
