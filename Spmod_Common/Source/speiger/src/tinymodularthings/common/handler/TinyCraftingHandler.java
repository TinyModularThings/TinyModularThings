package speiger.src.tinymodularthings.common.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.api.common.world.items.energy.IBCBattery;
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
		if(item != null)
		{
			if(item.itemID == TinyItems.netherCrystal.itemID && item.getItemDamage() == 0)
			{
				for(int i = 0;i<craftMatrix.getSizeInventory();i++)
				{
					ItemStack stack = craftMatrix.getStackInSlot(i);
					if(stack != null && stack.itemID == TinyItems.netherCrystal.itemID)
					{
						craftMatrix.setInventorySlotContents(i, null);
						craftMatrix.onInventoryChanged();
					}
				}
			}
			else if(item.getItem() instanceof IBCBattery)
			{
				boolean stopped = false;
				for(int i = 0;i<craftMatrix.getSizeInventory();i++)
				{
					ItemStack stack = craftMatrix.getStackInSlot(i);
					if(stack != null && stack.getItem() instanceof IBCBattery)
					{
						IBCBattery battery = (IBCBattery)stack.getItem();
						int charge = battery.getStoredMJ(stack);
						while(!stopped && charge > 0)
						{
							int added = ((IBCBattery)item.getItem()).charge(item, charge, false);
							if(added <= 0)
							{
								stopped = true;
								break;
							}
							charge -= added;
						}
						if(stopped)
						{
							break;
						}
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
