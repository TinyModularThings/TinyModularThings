package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidContainerRegistry;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InventoryBucketFiller extends AdvContainer
{
	BucketFillerBasic tile;
	public InventoryBucketFiller(InventoryPlayer par1, BucketFillerBasic par2)
	{
		tile = par2;
		this.addSlotToContainer(new Slot(par2, 0, 52, 41));
		this.addSlotToContainer(new Slot(par2, 1, 104, 41));
        int i;

        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1, i, 8 + i * 18, 142));
        }
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) crafters.get(i);
			tile.onSendingGuiInfo(this, icrafting);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		tile.onReciveGuiInfo(par1, par2);
	}
	
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 != 1 && par2 != 0)
            {
            	
            	if(FluidContainerRegistry.isBucket(itemstack1) || FluidContainerRegistry.isContainer(itemstack1))
            	{
            		if(tile.drain)
            		{
            			if(FluidContainerRegistry.isFilledContainer(itemstack1))
            			{
            				if(!this.mergeItemStack(itemstack1, 0, 1, true))
            				{
            					return null;
            				}
            			}
            		}
            		else
            		{
            			if(FluidContainerRegistry.isEmptyContainer(itemstack1))
            			{
            				if(!this.mergeItemStack(itemstack1, 0, 1, true))
            				{
            					return null;
            				}
            			}
            		}
            	}
                else if (par2 >= 2 && par2 < 29)
                {
                    if (!this.mergeItemStack(itemstack1, 29, 38, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 29 && par2 < 38 && !this.mergeItemStack(itemstack1, 2, 29, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 2, 38, false))
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
