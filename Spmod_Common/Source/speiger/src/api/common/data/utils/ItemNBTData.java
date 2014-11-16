package speiger.src.api.common.data.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


/**
 * 
 * @author Speiger
 * @Info This class will be handled as ItemData. Yeah no NBTComparing.
 * Why? simply that will be handled in the Result Class. So he is
 * Comparing the Items/Blocks and then the NBTData. Less Code
 * but same result.
 * 
 */
public class ItemNBTData extends ItemData implements INBTInfo
{
	NBTTagCompound nbt;
	public ItemNBTData(ItemStack par1)
	{
		super(par1);
		nbt = par1.getTagCompound();
	}

	@Override
	public NBTTagCompound getNBTData()
	{
		return nbt;
	}
	
}
