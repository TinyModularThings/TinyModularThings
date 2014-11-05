package speiger.src.tinymodularthings.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import cpw.mods.fml.common.ICraftingHandler;

public class TinyCraftingHandler implements ICraftingHandler
{
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if (player != null && item.itemID == TinyItems.interfaceBlock.itemID)
		{
			ItemStack stack = InventoryUtil.getItemFromInventory(craftMatrix, item.itemID);
			if (stack != null && stack.hasTagCompound() && ItemInterfaceBlock.isValidBlock(stack))
			{
				ItemStack block = ItemInterfaceBlock.getBlockFromInterface(stack);
				if (block != null)
				{
					if (!player.inventory.addItemStackToInventory(block))
					{
						player.dropPlayerItem(block);
					}
				}
			}
		}
	}
	
	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{
		
	}
	
}
