package speiger.src.api.common.registry.helpers;

import speiger.src.api.common.utils.LogProxy;

public interface SpmodMod
{
	
	/**
	 * Mod Name Has to be stable
	 **/
	public String getName();
	
	/** Logger. Not Required **/
	public LogProxy getLogger();
	
}
