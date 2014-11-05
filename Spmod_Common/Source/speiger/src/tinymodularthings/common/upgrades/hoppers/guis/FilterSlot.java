package speiger.src.tinymodularthings.common.upgrades.hoppers.guis;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import speiger.src.api.common.world.items.IBCBattery;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class FilterSlot extends Slot
{
	HopperType type;
	
	public FilterSlot(IInventory par1iInventory, HopperType par1, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
		type = par1;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if (par1ItemStack == null)
		{
			return false;
		}
		switch (type)
		{
			case Energy:
				return par1ItemStack.getItem() instanceof IBCBattery;
			case Fluids:
				return FluidContainerRegistry.isContainer(par1ItemStack) && FluidContainerRegistry.isFilledContainer(par1ItemStack);
			case Items:
				return true;
			default:
				return false;
				
		}
	}
	
}
