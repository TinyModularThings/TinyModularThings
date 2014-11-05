package speiger.src.spmodapi.common.util.proxy;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumChatFormatting;

public class LangProxy
{
	public static ChatMessageComponent getText(String data)
	{
		return new ChatMessageComponent().addText(data);
	}
	
	public static ChatMessageComponent getText(String data, EnumChatFormatting color)
	{
		return getText(data).setColor(color);
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
