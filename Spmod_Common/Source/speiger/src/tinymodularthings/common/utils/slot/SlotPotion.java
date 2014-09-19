package speiger.src.tinymodularthings.common.utils.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class SlotPotion extends Slot
{

	public SlotPotion(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1)
	{
		if(par1 != null)
		{
			if(par1.getItem() instanceof ItemPotion)
			{
				return true;
			}
			else if(par1.itemID == Item.glassBottle.itemID)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSlotStackLimit()
	{
		return 64;
	}
	
	
	
	
	
}
