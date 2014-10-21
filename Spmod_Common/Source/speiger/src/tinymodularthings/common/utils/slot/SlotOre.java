package speiger.src.tinymodularthings.common.utils.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SlotOre extends Slot
{

	public SlotOre(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}

	@Override
	public boolean isItemValid(ItemStack par1)
	{
		return OreDictionary.getOreID(par1) != -1;
	}
	
	

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	
	
	
}
