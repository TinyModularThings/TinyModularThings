package speiger.src.spmodapi.common.util.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BasicItemInventory implements IInventory
{
	public ItemStack[] inv = new ItemStack[0];
	
	public BasicItemInventory(int size)
	{
		inv = new ItemStack[size];
	}
	
	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}
	
	public ItemStack getStackInSlot(int par1)
	{
		return this.inv[par1];
	}
	
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inv[par1] != null)
		{
			ItemStack itemstack;
			
			if (this.inv[par1].stackSize <= par2)
			{
				itemstack = this.inv[par1];
				this.inv[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inv[par1].splitStack(par2);
				
				if (this.inv[par1].stackSize == 0)
				{
					this.inv[par1] = null;
				}
				
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.inv[par1] != null)
		{
			ItemStack itemstack = this.inv[par1];
			this.inv[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inv[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public String getInvName()
	{
		return "Basic Item Inventory";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return true;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	@Override
	public void onInventoryChanged()
	{
		
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
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
