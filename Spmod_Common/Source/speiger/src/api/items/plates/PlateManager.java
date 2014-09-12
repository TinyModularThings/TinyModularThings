package speiger.src.api.items.plates;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

public class PlateManager
{
	public static PlateInterface plates;
	
	public static class PlateInformation
	{
		public Item item;
		public int metadata;
		public String identity;
		public String displayName;
		
		public void setDisplayName(String displayName)
		{
			this.displayName = displayName;
		}
		
		public String getDisplayName()
		{
			return displayName;
		}
		
		public void setItem(Item item)
		{
			this.item = item;
		}
		
		public Item getItem()
		{
			return item;
		}
		
		public void setIdentity(String identity)
		{
			this.identity = identity;
		}
		
		public String getIdentity()
		{
			return identity;
		}
		
		public void setMetadata(int metadata)
		{
			this.metadata = metadata;
		}
		
		public int getMetadata()
		{
			return metadata;
		}
		
		public ItemStack getItemStack()
		{
			return new ItemStack(item, 1, metadata);
		}
		
		public ItemStack getItemStack(int qty)
		{
			return new ItemStack(item, qty, metadata);
		}
		
		public ItemStack getStackWithIdentity()
		{
			ItemStack stack = new ItemStack(item, 1, metadata);
			NBTTagString text = new NBTTagString("Identity", this.identity);
			stack.setTagInfo("Identity", text);
			return stack;
		}
		
		public ItemStack getStackWithIdentity(int qty)
		{
			ItemStack stack = new ItemStack(item, qty, metadata);
			NBTTagString text = new NBTTagString("Identity", this.identity);
			stack.setTagInfo("Identity", text);
			return stack;
		}
		
	}
}
