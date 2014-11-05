package speiger.src.api.common.registry.helpers;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.base.Strings;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;

public class SpmodModRegistry
{
	private static HashMap<String, SpmodMod> addons = new HashMap<String, SpmodMod>();
	
	public static void registerMod(SpmodMod par1)
	{
		if (par1.getClass().isAnnotationPresent(Mod.class) && !Strings.isNullOrEmpty(par1.getName()))
		{
			addons.put(par1.getName(), par1);
		}
		else
		{
			FMLLog.getLogger().info(par1.getName() + " Is not a valid SpmodMod, He will not registered");
		}
	}
	
	public static boolean isModRegistered(SpmodMod par1)
	{
		return addons.containsValue(par1);
	}
	
	public static boolean areModsEqual(SpmodMod par1, SpmodMod par2)
	{
		if (!isModRegistered(par1) || !isModRegistered(par2))
			return false;
		
		return par1.getName().equalsIgnoreCase(par2.getName());
	}
	
	public static SpmodMod getModFromName(String name)
	{
		return addons.get(name);
	}
	
	public static Collection<SpmodMod> getRegisteredMods()
	{
		return addons.values();
	}
}
