package speiger.src.spmodapi.common.items.trades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.items.trades.BasicTradeInventory.TradeSlot;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class TradeInventory extends AdvContainer
{
	MerchantRecipe recipe;
	int currentSlot;
	BasicTradeInventory inv;
	
	public TradeInventory(InventoryPlayer par1, BasicTradeInventory item)
	{
		this.addSpmodSlotToContainer(new Slot(item, 0, 30, 50));
		this.addSpmodSlotToContainer(new Slot(item, 1, 70, 50));
		this.addSpmodSlotToContainer(new TradeSlot(item, this, 2, 135, 50));
		this.setInventory(par1);
		recipe = new MerchantRecipe(ItemRandomTrade.getRecipeFromItem(par1.player.getCurrentEquippedItem()).writeToTags());
		currentSlot = par1.currentItem;
		inv = item;
		inv.setContainer(this);
	}
	
	public MerchantRecipe getCurrentTrade()
	{
		return recipe;
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1iInventory)
	{
		ItemStack key1 = inv.getStackInSlot(0);
		ItemStack key2 = inv.getStackInSlot(1);
		if (!recipe.func_82784_g() && InventoryUtil.isItemEqualSave(recipe.getItemToBuy(), key1) && InventoryUtil.isItemEqualSave(recipe.getSecondItemToBuy(), key2))
		{
			inv.setInventorySlotContents(2, recipe.getItemToSell().copy());
		}
		else
		{
			inv.setInventorySlotContents(2, null);
		}
		super.onCraftMatrixChanged(par1iInventory);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer par1)
	{
		InventoryPlayer player = par1.inventory;
		player.mainInventory[currentSlot].stackSize--;
		if (player.mainInventory[currentSlot].stackSize <= 0)
		{
			player.mainInventory[currentSlot] = null;
		}
		super.onContainerClosed(par1);
		if (!par1.worldObj.isRemote)
		{
			ItemStack itemstack = this.inv.getStackInSlotOnClosing(0);
			
			if (itemstack != null)
			{
				par1.dropPlayerItem(itemstack);
			}
			
			itemstack = this.inv.getStackInSlotOnClosing(1);
			
			if (itemstack != null)
			{
				par1.dropPlayerItem(itemstack);
			}
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if (par1 == currentSlot + 30)
		{
			return null;
		}
		if (par1 != 2)
		{
			this.onCraftMatrixChanged(inv);
		}
		if (par1 == 2 && !par4EntityPlayer.worldObj.isRemote)
		{
			this.recipe.incrementToolUses();
		}
		
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (par2 == 3)
			{
				if (!this.mergeItemStack(itemstack1, 3, 39, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (par2 != 0 && par2 != 1)
			{
				if (par2 >= 3 && par2 < 30)
				{
					if (!this.mergeItemStack(itemstack1, 30, 39, false))
					{
						return null;
					}
				}
				else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(itemstack1, 3, 39, false))
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
