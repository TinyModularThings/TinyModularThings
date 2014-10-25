package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.blocks.crafting.CraftingStation.CraftingInventory;

public class CraftingStationInventory extends AdvContainer
{
	World world;
	CraftingInventory inv;
	public CraftingStationInventory(InventoryPlayer par1, CraftingStation par2)
	{
		world = par2.getWorldObj();
		inv = par2.getInventoryFromPlayer(par1.player);
		
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				this.addSpmodSlotToContainer(new SlotCrafting(par1.player, inv, inv, 9+x+y*3, 110 + x * 18, 18 + y * 18));
				this.addSpmodSlotToContainer(new Slot(this.inv, y + x * 3, 10 + y * 18, 17 + x * 18));
			}
		}
		this.setInventory(par1);
		this.onCraftMatrixChanged(inv);
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		inv.leaveContainer();
		super.onContainerClosed(par1EntityPlayer);
	}


	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		ArrayList<IRecipe> results = new ArrayList<IRecipe>();
		for(IRecipe par1 : recipes)
		{
			if(par1.matches(inv, world))
			{
				results.add(par1);
			}
		}
		if(results.size() > 9)
		{
			Collections.shuffle(results);
		}
		
		for(int i = 0;i<9;i++)
		{
			if(i < results.size())
			{
				inv.setInventorySlotContents(9+i, results.get(i).getCraftingResult(inv));
			}
			else
			{
				inv.setInventorySlotContents(9+0, null);
			}
		}
		
	}
	
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 >= 0 && par2 < 9)
            {
                if (!this.mergeItemStack(itemstack1, 18, 54, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 18 && par2 < 45)
            {
                if (!this.mergeItemStack(itemstack1, 45, 54, false))
                {
                    return null;
                }
            }
            else if (par2 >= 45 && par2 < 54)
            {
                if (!this.mergeItemStack(itemstack1, 18, 45, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 18, 54, false))
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
