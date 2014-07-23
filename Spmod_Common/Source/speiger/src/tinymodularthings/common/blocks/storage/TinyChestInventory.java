package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class TinyChestInventory extends AdvContainer
{
	
	int size;
	
	int[][] SlotX = new int[][] { {}, { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
	
	public TinyChestInventory(InventoryPlayer par1, ISharedInventory par2)
	{
		size = par2.getIInventory().getSizeInventory();
		
		for (int i = 0; i < par2.getIInventory().getSizeInventory(); i++)
		{
			addSpmodSlotToContainer(new Slot(par2.getIInventory(), i, SlotX[par2.getIInventory().getSizeInventory()][i], 39));
		}
		
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			addSlotToContainer(new Slot(par1, var3, 8 + var3 * 18, 142));
		}
		
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
