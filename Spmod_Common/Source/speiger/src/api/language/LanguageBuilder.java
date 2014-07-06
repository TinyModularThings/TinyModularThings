package speiger.src.api.language;

import java.util.HashMap;

import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

/**
 * 
 * @author Speiger
 * 
 */
public class LanguageBuilder
{
	private static HashMap<SpmodMod, HashMap<String, HashMap<String, String>>> languages = new HashMap<SpmodMod, HashMap<String, HashMap<String, String>>>();
	
	public static void registerLanguage(SpmodMod par1, String lang, HashMap<String, String> par2)
	{
		if (!SpmodModRegistry.isModRegistered(par1))
		{
			return;
		}
		
		HashMap<String, String> list = getLanguage(par1, lang);
		list.putAll(par2);
		HashMap<String, HashMap<String, String>> langs = new HashMap<String, HashMap<String, String>>();
		langs.put(lang, list);
		languages.put(par1, langs);
	}
	
	public static HashMap<String, HashMap<String, String>> getLanguagePackFromMod(SpmodMod par1)
	{
		HashMap<String, HashMap<String, String>> lang = null;
		
		if (languages.get(par1) != null)
		{
			lang = languages.get(par1);
		}
		else
		{
			lang = new HashMap<String, HashMap<String, String>>();
		}
		
		return lang;
		
	}
	
	public static HashMap<String, String> getLanguage(SpmodMod par1, String par2)
	{
		HashMap<String, HashMap<String, String>> lang = getLanguagePackFromMod(par1);
		
		HashMap<String, String> cuLang = null;
		
		if (lang.get(par2) != null)
		{
			cuLang = lang.get(par2);
		}
		else
		{
			cuLang = new HashMap<String, String>();
		}
		
		return cuLang;
	}
	
	public static HashMap<String, String> getCurrentLanguage(SpmodMod par1)
	{
		return getLanguage(par1, FMLCommonHandler.instance().getCurrentLanguage());
	}
	
	public static HashMap<String, String> getDefaultLanguage(SpmodMod par1)
	{
		return getLanguage(par1, EnumLanguage.Englisch.getLanguage());
	}
	
}
