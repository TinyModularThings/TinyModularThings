package speiger.src.api.common.registry.helpers;

import speiger.src.api.common.utils.LogProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public interface SpmodMod
{
	
	/**
	 * Mod Name Has to be stable
	 **/
	public String getName();
	
	/** Logger. Not Required **/
	public LogProxy getLogger();
	
}
