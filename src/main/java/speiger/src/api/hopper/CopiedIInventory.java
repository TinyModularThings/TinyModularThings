package speiger.src.api.hopper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CopiedIInventory implements IInventory
{
	ItemStack[] inventory;
	
	public CopiedIInventory(IInventory par1)
	{
		ItemStack[] array = new ItemStack[par1.getSizeInventory()];
		for (int i = 0; i < par1.getSizeInventory(); i++)
		{
			if (par1.getStackInSlot(i) != null)
			{
				array[i] = par1.getStackInSlot(i).copy();
			}
		}
		this.inventory = array;
	}
	
	public CopiedIInventory(ItemStack[] par1)
	{
		this.inventory = ((ItemStack[]) par1.clone());
	}
	
	@Override
	public int getSizeInventory()
	{
		return this.inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return this.inventory[i];
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
	}
	
	@Override
	public String getInventoryName()
	{
		return "Copied Inventory";
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 0;
	}
	
	@Override
	public void markDirty()
	{
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}
	
	@Override
	public void openInventory()
	{
	}
	
	@Override
	public void closeInventory()
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}
}