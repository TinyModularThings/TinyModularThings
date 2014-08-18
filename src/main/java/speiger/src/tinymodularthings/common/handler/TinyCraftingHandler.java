package speiger.src.tinymodularthings.common.handler;

import net.minecraft.item.ItemStack;
import speiger.src.api.util.InventoryUtil;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;

public class TinyCraftingHandler
{
	
	@SubscribeEvent
	public void onCrafting(PlayerEvent.ItemCraftedEvent event)
	{
		if (event.player != null && event.crafting.getItem() == TinyItems.interfaceBlock)
		{
			ItemStack stack = InventoryUtil.getItemFromInventory(event.craftMatrix, event.crafting.getItem());
			if (stack != null && stack.hasTagCompound() && ItemInterfaceBlock.isValidBlock(stack))
			{
				ItemStack block = ItemInterfaceBlock.getBlockFromInterface(stack);
				if (block != null)
				{
					if (!event.player.inventory.addItemStackToInventory(block))
					{
						event.player.dropPlayerItemWithRandomChoice(block, true);
					}
				}
			}
		}
	}
	
	
}
