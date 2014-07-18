package speiger.src.spmodapi.common.util.proxy;

import java.util.List;

import speiger.src.api.items.InfoStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;

public class LangProxy
{
	public static String getModeString(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "mode", par1);
	}
	
	public static String getInfo(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "info", par1);
	}
	
	public static String getAmount(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "amount", par1);
	}
	
	public static String getStoredNothing(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "stored.nothing", par1);
	}
	
	public static String UFluid(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "tank.stored.unknowen", par1);
	}
	
	public static String getStored(SpmodMod par1)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), "tank.stored", par1);
	}
	
	public static String getSlot(SpmodMod par1, boolean par2)
	{
		return LanguageRegister.getLanguageName(new InfoStack(), par2 ? "slots" : "slot", par1);
	}
	
	public static int getLongestString(List<String> par1)
	{
		int length = 0;
		for (String cu : par1)
		{
			length = Math.max(length, getHalfString(cu));
		}
		return length;
	}
	
	public static int getHalfString(String par1)
	{
		return par1.length() * 2;
	}
	
}
