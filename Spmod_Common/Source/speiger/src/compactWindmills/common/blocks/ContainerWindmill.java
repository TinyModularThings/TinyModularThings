package speiger.src.compactWindmills.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class ContainerWindmill extends AdvContainer
{
	public ContainerWindmill(InventoryPlayer par1, WindMill par2)
	{
		this.addSlotToContainer(new WindMillSlot(par2, 0, 80, 26));
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
			
			if (par2 == 0)
			{
				if (!mergeItemStack(itemstack1, 1, 37, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 != 0)
			{
				if (((WindMillSlot) inventorySlots.get(0)).isItemValid(itemstack1))
				{
					if (!mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if (par2 >= 1 && par2 < 28)
				{
					if (!mergeItemStack(itemstack1, 28, 37, false))
					{
						return null;
					}
				}
				else if (par2 >= 28 && par2 < 37 && !mergeItemStack(itemstack1, 1, 27, false))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 1, 28, false))
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
