package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodCraftingSlot;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;

public class ContainerAdvCrafting extends AdvContainer
{
	public World world;
	public InventoryCrafting matrix = new InventoryCrafting(this, 3, 3);
	public IInventory[] result = new IInventory[9];
	
	public ContainerAdvCrafting(InventoryPlayer par1)
	{
		super(par1);
		world = par1.player.worldObj;
		for(int i = 0;i<result.length;i++)
		{
			result[i] = new InventoryCraftResult();
		}
		
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				this.addSpmodSlotToContainer(new SpmodCraftingSlot(par1.player, matrix, result[x+y*3], x+y*3, 110 + x * 18, 18 + y * 18));
				this.addSpmodSlotToContainer(new SpmodSlot(this.matrix, y + x * 3, 10 + y * 18, 17 + x * 18));
			}
		}
		this.setInventory(par1);
		
		this.onCraftMatrixChanged(matrix);
	}
	
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int i = 0; i < 9; ++i)
            {
                ItemStack itemstack = this.matrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    par1EntityPlayer.dropPlayerItem(itemstack);
                }
            }
        }
    }
	
	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		ArrayList<IRecipe> results = new ArrayList<IRecipe>();
		for(IRecipe par1 : recipes)
		{
			if(par1.matches(matrix, world))
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
				result[i].setInventorySlotContents(0, results.get(i).getCraftingResult(matrix));
			}
			else
			{
				result[i].setInventorySlotContents(0, null);
			}
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
