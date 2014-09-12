package speiger.src.tinymodularthings.common.upgrades.hoppers.guis;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class FilterInventory extends AdvContainer
{
	int[][] SlotX = { new int[0], { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
	int size;
	
	public FilterInventory(InventoryPlayer par1, IInventory par2, HopperType par3)
	{
		size = par2.getSizeInventory();
		for (int i = 0; i < size; i++)
		{
			this.addSpmodSlotToContainer(new FilterSlot(par2, par3, i, SlotX[size][i], 39));
		}
		this.setInventory(par1);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par2);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (par2 < size)
			{
				if (!mergeItemStack(itemstack1, size, 36 + size, true))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 0, size, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		
		return itemstack;
	}
	
}
