package speiger.src.api.nbt;

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
		if(par1.hasTagCompound())
		{
			NBTTagCompound data = par1.getTagCompound();
			if(data.hasKey(string))
			{
				return true;
			}
		}
		return false;
	}
	
}
