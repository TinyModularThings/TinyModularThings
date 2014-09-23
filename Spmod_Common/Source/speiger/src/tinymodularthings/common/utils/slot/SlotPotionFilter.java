package speiger.src.tinymodularthings.common.utils.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;


public class SlotPotionFilter extends SlotPotion
{

	public SlotPotionFilter(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1)
	{
		return par1 != null && par1.getItem() instanceof ItemPotion;
	}
	
	
	
}
