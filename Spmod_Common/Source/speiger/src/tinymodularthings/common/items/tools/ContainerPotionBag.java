package speiger.src.tinymodularthings.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.utils.slot.SlotPotion;

public class ContainerPotionBag extends AdvContainer
{
	IInventory inv;
	public ContainerPotionBag(InventoryPlayer par1, IInventory par2)
	{
		inv = par2;
		
		for(int x = 0;x<4;x++)
		{
			for(int y = 0;y<9;y++)
			{
				this.addSpmodSlotToContainer(new SlotPotion(par2, y + x * 9, 8 + y * 18, 18 + x * 18));
			}
		}
		
		this.setInventory(par1, 8, 140);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	

	@Override
	public void onContainerClosed(EntityPlayer par1)
	{
		super.onContainerClosed(par1);
		inv.closeChest();
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 >= 36)
            {
            	if(itemstack1.itemID == Item.glassBottle.itemID || itemstack1.getItem() instanceof ItemPotion)
            	{
                    if (!this.mergeItemStack(itemstack1, 0, 36, false))
                    {
                        return null;
                    }
            	}
            }
            else if (!this.mergeItemStack(itemstack1, 36, 72, true))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
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