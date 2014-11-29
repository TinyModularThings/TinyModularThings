package speiger.src.api.common.data.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemAData implements IStackInfo
{
	Item item;
	int amount;
	int meta;
	
	public ItemAData(Item par1, int par2)
	{
		item = par1;
		amount = par2;
		meta = 0;
	}
	
	public ItemAData(Item par1, int par2, int par3)
	{
		item = par1;
		amount = par2;
		meta = par3;
	}
	
	@Override
	public ItemStack getResult()
	{
		return new ItemStack(item, amount, meta);
	}

	@Override
	public int hashCode()
	{
		return item.itemID + meta;
	}

	@Override
	public boolean equals(Object par1)
	{
		if(par1 == null)
		{
			return false;
		}
		if(par1 instanceof ItemAData)
		{
			ItemAData data = (ItemAData)par1;
			if(data.item == item && data.meta == meta && data.amount == amount)
			{
				return true;
			}
		}
		else if(par1 instanceof ResultData)
		{
			ResultData data = (ResultData)par1;
			if(data.item == item && data.meta == meta && data.stackSize == amount)
			{
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
}
