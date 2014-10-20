package speiger.src.spmodapi.common.util.proxy;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumChatFormatting;
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
	
	public static ItemStack getItemStackWithInfo(ItemStack par1, List<String> par2)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		for(String par3 : par2)
		{
			list.appendTag(new NBTTagString("info", EnumChatFormatting.YELLOW+par3));
		}
		nbt.setTag("Lore", list);
		par1.setTagInfo("display", nbt);
		return par1;
	}
	
}
