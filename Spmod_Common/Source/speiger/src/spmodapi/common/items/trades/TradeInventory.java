package speiger.src.spmodapi.common.items.trades;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.village.MerchantRecipe;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class TradeInventory extends AdvContainer
{
	MerchantRecipe recipe;
	
	public TradeInventory(InventoryPlayer par1, BasicTradeInventory item)
	{
		recipe = ItemRandomTrade.getRecipeFromItem(par1.player.getCurrentEquippedItem());
		
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
}
