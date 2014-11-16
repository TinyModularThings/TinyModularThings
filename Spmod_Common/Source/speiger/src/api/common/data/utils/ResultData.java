package speiger.src.api.common.data.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Speiger
 * @Info: This class is for Recipes it matches data simply.
 */
public class ResultData implements IStackInfo, INBTInfo
{
	Item item;
	int meta;
	NBTTagCompound nbt;
	
	public ResultData(ItemStack data)
	{
		item = data.getItem();
		meta = data.getItemDamage();
		nbt = data.getTagCompound();
	}

	@Override
	public ItemStack getResult()
	{
		return new ItemStack(item, 1, meta);
	}

	@Override
	public boolean equals(Object par1)
	{
		if(par1 == null)
		{
			return false;
		}
		if(par1 instanceof INBTInfo)
		{
			NBTTagCompound data = ((INBTInfo)par1).getNBTData();
			if(data == null && nbt == null) ;
			else if(data != null && nbt != null)
			{
				if(!nbt.equals(data))
				{
					return false;
				}
			}
			else
			{
				return false;
			}
			
		}
		
		if(par1 instanceof ResultData)
		{
			ResultData data = (ResultData)par1;
			return data.item.itemID == item.itemID && data.meta == meta;
		}
		else if(par1 instanceof ItemData)
		{
			ItemData data = (ItemData)par1;
			return data.item.itemID == item.itemID && data.meta == meta;
		}
		else if(par1 instanceof BlockData)
		{
			BlockData data = (BlockData)par1;
			return data.block.blockID == item.itemID && data.meta == meta;
		}
		else if(par1 instanceof IStackInfo)
		{
			return par1.equals(this);
		}
		
		return false;
	}

	@Override
	public int hashCode()
	{
		return item.itemID+meta;
	}

	@Override
	public NBTTagCompound getNBTData()
	{
		return nbt;
	}
	
	
	
}
