package speiger.src.tinymodularthings.common.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.registry.GameRegistry;

public class TicketUtils
{
	
	private static ItemStack ticket = GameRegistry.findItemStack("Railcraft", "routing.ticket", 1);
	
	public static boolean isTicket(ItemStack stack)
	{
		ItemStack stacks = GameRegistry.findItemStack("Railcraft", "routing.ticket", 1);
		ItemStack newStack = GameRegistry.findItemStack("Railcraft", "routing.ticket.gold", 1);
		if (stack != null && stacks != null && newStack != null)
		{
			if (stack.itemID == stacks.itemID || stack.itemID == newStack.itemID)
			{
				return true;
			}
		}
		return false;
	}
	
	public static ItemStack copyTicket(ItemStack source)
	{
		if (source == null)
		{
			return null;
		}
		if (isTicket(source))
		{
			ItemStack ticket = getTicket();
			NBTTagCompound nbt = source.getTagCompound();
			if (nbt != null)
			{
				ticket.setTagCompound((NBTTagCompound) nbt.copy());
			}
			return ticket;
		}
		return null;
	}
	
	private static ItemStack getTicket()
	{
		if (ticket != null)
		{
			return ticket;
		}
		return null;
	}
	
	public static boolean setTicketData(ItemStack ticket, String dest, String title, String owner)
	{
		if ((ticket == null) || (!isTicket(ticket)))
		{
			return false;
		}
		if (dest.length() > 32)
		{
			return false;
		}
		if ((owner == null) || (owner.equals("")))
		{
			return false;
		}
		NBTTagCompound data = getItemData(ticket);
		data.setString("dest", dest);
		data.setString("title", title);
		data.setString("owner", owner);
		return true;
	}
	
	public static String getDestination(ItemStack ticket)
	{
		if ((ticket == null) || (!(isTicket(ticket))))
		{
			return "";
		}
		NBTTagCompound nbt = ticket.getTagCompound();
		if (nbt == null)
		{
			return "";
		}
		return nbt.getString("dest");
	}
	
	private static NBTTagCompound getItemData(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
		{
			nbt = new NBTTagCompound("tag");
			stack.setTagCompound(nbt);
		}
		return nbt;
	}
	
}
