package speiger.src.api.common.core;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.base.Strings;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;

public class SpmodModRegistry
{
	private static HashMap<String, SpmodMod> addons = new HashMap<String, SpmodMod>();
	private static LogProxy defaultProxy;
	
	static
	{
		SpmodMod baseMod = new SpmodMod(){
			@Override
			public LogProxy getLogger()
			{
				return defaultProxy;
			}
			@Override
			public String getModName()
			{
				return "Spmod Mod";
			}
		};
		addons.put("Spmod Mod", baseMod);
		defaultProxy = new LogProxy(baseMod);
	}
	
	public static void registerMod(SpmodMod par1)
	{
		if (par1.getClass().isAnnotationPresent(Mod.class) && !Strings.isNullOrEmpty(par1.getModName()))
		{
			addons.put(par1.getModName(), par1);
		}
		else
		{
			FMLLog.getLogger().info(par1.getModName() + " Is not a valid SpmodMod, He will not registered");
		}
	}
	
	public static boolean isAddonRegistered(SpmodMod par1)
	{
		if(par1 == null)
		{
			return false;
		}
		return isAddonRegistered(par1.getModName());
	}
	
	public static boolean isAddonRegistered(String par1)
	{
		return addons.containsKey(par1);
	}
	
	public static boolean areAddonsEqual(SpmodMod par1, SpmodMod par2)
	{
		if(!isAddonRegistered(par1) || !isAddonRegistered(par2))
		{
			return false;
		}
		return par1.getModName().equals(par2.getModName());
	}
	
	public static SpmodMod getModFromName(String par1)
	{
		return addons.get(par1);
	}
	
	public static Collection<SpmodMod> getRegisteredMods()
	{
		return addons.values();
	}
	
	public static LogProxy getDefaultLogger(SpmodMod par1)
	{
		if(!isAddonRegistered(par1))
		{
			return null;
		}
		return defaultProxy;
	}
	
	public static void log(SpmodMod mod, String par2)
	{
		mod.getLogger().print(par2);
	}
	
	public static void log(SpmodMod mod, Object par2)
	{
		mod.getLogger().print(par2);
	}
}
