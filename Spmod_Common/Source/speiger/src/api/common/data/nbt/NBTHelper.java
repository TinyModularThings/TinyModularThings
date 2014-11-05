package speiger.src.api.common.data.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NBTHelper
{
	public static NBTTagCompound getTinyChestTagCompound(NBTTagCompound nbt)
	{
		NBTTagCompound end = nbt.getCompoundTag("TinyModularThings");
		
		if (end == null)
		{
			end = new NBTTagCompound("TinyModularThings");
		}
		return end;
	}
	
	public static NBTTagCompound getTinyChestTagCompound(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		
		return getTinyChestTagCompound(nbt);
	}
	
	public static String getPlayerNBTStringFromMode(int mode)
	{
		switch (mode)
		{
			case 0:
				return "player.placement";
			default:
				return "";
		}
	}
	
	public static void setTinyChestData(NBTTagCompound data, NBTTagCompound nbt)
	{
		nbt.setCompoundTag("TinyModularThings", data);
	}
	
	public static boolean nbtCheck(ItemStack par1, String string)
	{
		if (par1.hasTagCompound())
		{
			NBTTagCompound data = par1.getTagCompound();
			if (data.hasKey(string))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void removeTag(ItemStack par1, String tag)
	{
		if (par1.hasTagCompound())
		{
			par1.getTagCompound().removeTag(tag);
		}
	}

	public static NBTTagCompound getTag(ItemStack stack, String par1)
	{
		if(!nbtCheck(stack, par1))
		{
			stack.setTagInfo(par1, new NBTTagCompound());
		}
		return stack.getTagCompound().getCompoundTag(par1);
	}
	
}
