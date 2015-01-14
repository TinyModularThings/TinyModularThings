package speiger.src.spmodapi.common.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class AdvInventory extends AdvTile implements IInventory
{
	public ItemStack[] inv;
	int maxStack = 64;
	
	public AdvInventory(int size)
	{
		inv = new ItemStack[size];
	}
	
	public AdvInventory setMaxStackSize(int size)
	{
		maxStack = size;
		return this;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return maxStack;
	}
	
	@Override
	public void openChest()
	{
		
	}
	
	@Override
	public void closeChest()
	{
		
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	
}
