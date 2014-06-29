package speiger.src.api.language;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import cpw.mods.fml.common.FMLLog;

/**
 * 
 * @author Speiger
 * 
 */
public class LanguageLoader
{
	
	private SpmodMod mod;
	
	// Starting part
	public LanguageLoader(SpmodMod par1)
	{
		if (SpmodModRegistry.isModRegistered(par1))
		{
			mod = par1;
		}
	}
	
	// If game is already started
	
	public void registerLanguage(EnumLanguage par1)
	{
		
		if (mod == null || mod.getLangFolder() == null || mod.getLangFolder() == "" || mod.getLangFolder().length() <= 5)
		{
			return;
		}
		
		Properties prop = new Properties();
		InputStream input = getClass().getResourceAsStream(mod.getLangFolder() + "/" + par1.getLanguage() + ".lang");
		
		FMLLog.getLogger().info("Register Language: " + par1);
		
		if (input == null)
		{
			return;
		}
		
		try
		{
			prop.load(input);
		}
		catch (IOException e)
		{
			FMLLog.getLogger().info("[Speiger API] something work wrong while language loading. Reason: " + e.getMessage());
		}
		
		HashMap<String, HashMap<String, String>> list = null;
		
		if (LanguageBuilder.getLanguagePackFromMod(mod) != null)
		{
			list = LanguageBuilder.getLanguagePackFromMod(mod);
		}
		else
		{
			list = new HashMap<String, HashMap<String, String>>();
		}
		
		HashMap<String, String> lang = null;
		
		if (list.get(par1.getLanguage()) != null)
		{
			lang = list.get(par1.getLanguage());
		}
		else
		{
			lang = new HashMap<String, String>();
		}
		
		Iterator iter = prop.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<Object, Object> cu = (Entry<Object, Object>) iter.next();
			lang.put((String) cu.getKey(), (String) cu.getValue());
		}
		
		LanguageBuilder.registerLanguage(mod, par1.getLanguage(), lang);
		
	}
	
	// Loads all Languages of the Mod
	
	public void registerAllAviableLanguages()
	{
		EnumLanguage[] languages = EnumLanguage.values();
		for (int i = 0; i < languages.length; i++)
		{
			EnumLanguage current = languages[i];
			registerLanguage(current);
		}
	}
	
}
