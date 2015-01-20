package speiger.src.api.common.data.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemData implements IStackInfo
{
	Item item;
	int meta;
	
	public ItemData(ItemStack par2)
	{
		this(par2.getItem(), par2.getItemDamage());
	}
	
	public ItemData(Item par1, int par2)
	{
		item = par1;
		meta = par2;
	}
	
	public ItemData(int par1, int par2)
	{
		this(Item.itemsList[par1], par2);
	}
	
	public ItemData(Item par1)
	{
		this(par1, 0);
	}

	@Override
	public boolean equals(Object arg0)
	{
		if(arg0 == null)
		{
			return false;
		}
		if(!(arg0 instanceof ItemData))
		{
			return false;
		}
		ItemData par1 = (ItemData)arg0;
		if(par1.item != item)
		{
			return false;
		}
		if(par1.meta != meta)
		{
			return false;
		}
		return true;
	}
	@Override
	public int hashCode()
	{
		return item.itemID + meta;
	}

	@Override
	public ItemStack getResult()
	{
		return new ItemStack(item, 1, meta);
	}
	
	
}
