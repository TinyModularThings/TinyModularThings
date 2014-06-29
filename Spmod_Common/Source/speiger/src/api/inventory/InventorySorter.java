package speiger.src.api.inventory;

import net.minecraft.item.ItemStack;

public class InventorySorter
{
	/**
	 * This Class is a inventory Sorter. You just put a inventory in
	 * and call the functions. In that time the Real Inventory will be not Changed.
	 * If you are finish you override your inventory with the Function FinishSorting. Which is returning the Sorted Inventory
	 * The class is only a helper!
	 */
	
	ItemStack[] inventory;
	
	public InventorySorter(ItemStack[] sort)
	{
		inventory = sort;
	}
	
	public void sortEverythingUp(boolean nbt)
	{
		for(int i = 0;i<inventory.length-1;i++)
		{
			for(int z = i;i<inventory.length;i++)
			{
				this.transferItem(i, z, nbt);
			}
		}
	}
	
	public void sortEverythingDown(boolean nbt)
	{
		for(int i = inventory.length; i>1;i--)
		{
			for(int z = i;z>0;z--)
			{
				this.transferItem(i, z, nbt);
			}
		}
	}
	
	public void sortEverythingUp()
	{
		for(int i = 0;i<inventory.length-1;i++)
		{
			for(int z = i;i<inventory.length;i++)
			{
				this.transferItem(i, z);
			}
		}
	}
	
	public void sortEverythingDown()
	{
		for(int i = inventory.length; i>1;i--)
		{
			for(int z = i;z>0;z--)
			{
				this.transferItem(i, z);
			}
		}
	}
	
	public void transferItem(int slot0, int slot1)
	{
		this.transferItem(slot0, slot1, false);
	}
	
	public void transferItem(int slot0, int slot1, boolean nbt)
	{
		if(inventory[slot0] != null)
		{
			if(inventory[slot1] == null)
			{
				inventory[slot1] = inventory[slot0].copy();
				inventory[slot0] = null;
			}
			else if(inventory[slot1] != null && inventory[slot1].isItemEqual(inventory[slot0]) && (!nbt || (nbt && ItemStack.areItemStackTagsEqual(inventory[slot0], inventory[slot1]))))
			{
				if(inventory[slot1].stackSize + inventory[slot0].stackSize <= inventory[slot1].getMaxStackSize())
				{
					inventory[slot1].stackSize += inventory[slot0].stackSize;
					inventory[slot0] = null;
				}
				else
				{
					int stacksize = (inventory[slot1].stackSize + inventory[slot0].stackSize) - 64;
					inventory[slot1].stackSize = 64;
					inventory[slot0].stackSize = stacksize;
				}
			}
		}
	}
	
	public ItemStack[] finishSorting()
	{
		return inventory;
	}
}
