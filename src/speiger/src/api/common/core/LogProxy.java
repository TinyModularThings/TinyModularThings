package speiger.src.api.common.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class LogProxy
{
	Logger mod;
	SpmodMod mods;
	boolean log = true;
	
	public LogProxy(SpmodMod modID)
	{
		mod = LogManager.getLogger(modID.getModName());
		mods = modID;
	}
	
	public boolean isRegistered()
	{
		if (SpmodModRegistry.isAddonRegistered(mods))
		{
			return true;
		}
		return false;
	}
	
	public void disable()
	{
		log = false;
	}
	
	public void print(String par1)
	{
		if (!isRegistered())
		{
			return;
		}
		if (!log)
		{
			return;
		}
		mod.info(par1);
	}
	
	public void print(Object par1)
	{
		if (!isRegistered())
		{
			return;
		}
		if (!log)
		{
			return;
		}
		
		mod.info(par1);
	}
}
