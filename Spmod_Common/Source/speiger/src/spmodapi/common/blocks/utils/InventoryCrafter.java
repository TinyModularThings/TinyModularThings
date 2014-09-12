package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class InventoryCrafter extends AdvContainer
{
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	private World worldObj;
	private int x;
	private int y;
	private int z;
	
	public InventoryCrafter(InventoryPlayer par1, World world, int x, int y, int z)
	{
		worldObj = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.addSlotToContainer(new SlotCrafting(par1.player, this.craftMatrix, this.craftResult, 0, 124, 35));
		int l;
		int i1;
		
		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 3; ++i1)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
			}
		}
		
		for (l = 0; l < 3; ++l)
		{
			for (i1 = 0; i1 < 9; ++i1)
			{
				this.addSlotToContainer(new Slot(par1, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
			}
		}
		
		for (l = 0; l < 9; ++l)
		{
			this.addSlotToContainer(new Slot(par1, l, 8 + l * 18, 142));
		}
		
		this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}
	
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		
		if (!this.worldObj.isRemote)
		{
			for (int i = 0; i < 9; ++i)
			{
				ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);
				
				if (itemstack != null)
				{
					par1EntityPlayer.dropPlayerItem(itemstack);
				}
			}
			this.worldObj.setBlock(x, y, z, 0);
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (par2 == 0)
			{
				if (!this.mergeItemStack(itemstack1, 10, 46, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 >= 10 && par2 < 37)
			{
				if (!this.mergeItemStack(itemstack1, 37, 46, false))
				{
					return null;
				}
			}
			else if (par2 >= 37 && par2 < 46)
			{
				if (!this.mergeItemStack(itemstack1, 10, 37, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 10, 46, false))
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
	
	public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
	{
		return par2Slot.inventory != this.craftResult && super.func_94530_a(par1ItemStack, par2Slot);
	}
	
}
