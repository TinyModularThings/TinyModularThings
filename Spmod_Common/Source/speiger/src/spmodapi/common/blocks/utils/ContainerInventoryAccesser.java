package speiger.src.spmodapi.common.blocks.utils;

import ic2.api.item.IElectricItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.world.items.IBCBattery;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SlotAdjust;
import speiger.src.spmodapi.common.util.slot.SlotItemAdjustSlot;

public class ContainerInventoryAccesser extends AdvContainer
{
	public ContainerInventoryAccesser(InventoryPlayer par1, InventoryAccesser par2)
	{
		super(par2);
		for(int i = 0;i<4;i++)
		{
			this.addSpmodSlotToContainer(new SlotItemAdjustSlot(par2, i, 150, 0+i*18, new ItemData(APIItems.redstoneCable)));
		}

		this.addSpmodSlotToContainer(new SlotAdjust(par2, 4, 150, 90, IBCBattery.class, IElectricItem.class));
		
		for(int i = 0;i<9;i++)
		{
			this.addSpmodSlotToContainer(new Slot(par1, i, 0+i*18, 158));
		}
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < 5)
            {
                if (!this.mergeItemStack(itemstack1, 5, 14, true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 5, false))
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
