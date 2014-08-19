package speiger.src.api.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.hopper.IHopperInventory;

public class FilterSlot extends Slot
{

	public FilterSlot(IFilteredInventory par1, int par2, int par3, int par4)
	{
		super((IInventory)par1, par2, par3, par4);
		
	}

	@Override
	public ItemStack decrStackSize(int par1)
	{
		return null;
	}

	@Override
	public ItemStack getStack()
	{
		return ((IFilteredInventory)this.inventory).getFilteredItem(this.getSlotIndex());
	}

	@Override
	public void putStack(ItemStack par1ItemStack)
	{
		((IFilteredInventory)inventory).setFilteredItem(getSlotIndex(), par1ItemStack);
		this.onSlotChanged();
	}

	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	
	
	
	
}
