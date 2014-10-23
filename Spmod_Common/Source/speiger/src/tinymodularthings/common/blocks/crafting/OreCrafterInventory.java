package speiger.src.tinymodularthings.common.blocks.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.utils.slot.SlotOre;
import cpw.mods.fml.common.network.PacketDispatcher;

public class OreCrafterInventory extends AdvContainer
{
	TileEntity tile;
	public OreCrafterInventory(InventoryPlayer par1, IInventory par2)
	{
		tile = (TileEntity)par2;
		int slot = 0;
		for(int i = 0;i<10;i++)
		{
			this.addSpmodSlotToContainer(new Slot(par2, slot++, 10 + i * 18, 20));
		}
		for(int i = 0;i<3;i++)
		{
			this.addSpmodSlotToContainer(new Slot(par2, slot++, 20 + i * 18, 48));
		}
		this.addSpmodSlotToContainer(new SlotOre(par2, slot++, 93, 48));
		for(int i = 0;i<3;i++)
		{
			this.addSpmodSlotToContainer(new Slot(par2, slot++, 130 + i * 18, 48));
		}
		for(int z = 0;z<3;z++)
		{
			for(int i = 0;i<10;i++)
			{
				this.addSpmodSlotToContainer(new SlotOre(par2, slot++, 12 + i * 18, 75 + z * 18));
			}
		}
		
		this.setInventory(par1, 24, 143);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if(par1 < 10 && par1 >= 0)
		{
			((OreCrafter)tile).slotClick(par1);			
			PacketDispatcher.sendPacketToServer(SpmodPacketHelper.getHelper().createNBTPacket(tile, TinyModularThings.instance).InjectNumber(par1).finishPacket());
			return null;
		}
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 >= 14 && par2 < 17)
            {
                if (!this.mergeItemStack(itemstack1, 47, 83, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 47)
            {
            	if(slot.isItemValid(itemstack1))
            	{
                	if(!this.mergeItemStack(itemstack1, 10, 13, false))
                	{
                		return null;
                	}
            	}
            }
            else if (par2 >= 47 && par2 < 74)
            {
                if (!this.mergeItemStack(itemstack1, 74, 83, false))
                {
                    return null;
                }
            }
            else if (par2 >= 74 && par2 < 83)
            {
                if (!this.mergeItemStack(itemstack1, 47, 74, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 47, 83, false))
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
