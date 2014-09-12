package speiger.src.spmodapi.common.items.trades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import speiger.src.spmodapi.common.util.slot.BasicItemInventory;

public class BasicTradeInventory extends BasicItemInventory
{
	TradeInventory trade;
	
	public BasicTradeInventory(int size)
	{
		super(size);
	}
	
	public void setContainer(TradeInventory inv)
	{
		trade = inv;
	}
	
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		super.setInventorySlotContents(par1, par2ItemStack);
		if (par1 != 2 && trade != null)
		{
			trade.onCraftMatrixChanged(this);
		}
	}
	
	public static class TradeSlot extends Slot
	{
		TradeInventory trade;
		
		public TradeSlot(IInventory par1iInventory, TradeInventory par1, int par2, int par3, int par4)
		{
			super(par1iInventory, par2, par3, par4);
			trade = par1;
		}
		
		@Override
		public boolean isItemValid(ItemStack par1ItemStack)
		{
			return false;
		}
		
		@Override
		public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
		{
			super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
			MerchantRecipe recipe = trade.getCurrentTrade();
			if (recipe != null)
			{
				ItemStack key1 = recipe.getItemToBuy();
				ItemStack key2 = recipe.getSecondItemToBuy();
				if (key1 != null)
				{
					ItemStack cu = this.inventory.getStackInSlot(0);
					cu.stackSize -= key1.stackSize;
					this.inventory.setInventorySlotContents(0, cu);
				}
				if (key2 != null)
				{
					ItemStack cu = this.inventory.getStackInSlot(1);
					cu.stackSize -= key2.stackSize;
					this.inventory.setInventorySlotContents(1, cu);
				}
			}
		}
		
	}
}
