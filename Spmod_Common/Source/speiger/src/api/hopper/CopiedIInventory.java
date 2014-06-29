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
	
	public int getSizeInventory()
	{
		return this.inventory.length;
	}
	
	public ItemStack getStackInSlot(int i)
	{
		return this.inventory[i];
	}
	
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}
	
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return this.inventory[i];
	}
	
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
	}
	
	public String getInvName()
	{
		return "Copied Inventory";
	}
	
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	public int getInventoryStackLimit()
	{
		return 0;
	}
	
	public void onInventoryChanged()
	{
	}
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}
	
	public void openChest()
	{
	}
	
	public void closeChest()
	{
	}
	
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}
}