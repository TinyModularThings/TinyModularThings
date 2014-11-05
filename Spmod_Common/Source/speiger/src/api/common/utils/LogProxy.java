package speiger.src.api.common.utils;

import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;

/**
 * 
 * @author Speiger
 * 
 */
public class LogProxy
{
	String mod;
	SpmodMod mods;
	boolean log = true;
	
	public LogProxy(SpmodMod modID)
	{
		mod = "[" + modID.getName() + "] ";
		mods = modID;
	}
	
	public boolean isRegistered()
	{
		if (SpmodModRegistry.isModRegistered(mods))
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
		System.out.println(mod + par1);
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
		
		System.out.println(mod + par1);
	}
}
