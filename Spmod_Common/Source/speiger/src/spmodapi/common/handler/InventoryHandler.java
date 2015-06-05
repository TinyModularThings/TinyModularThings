package speiger.src.spmodapi.common.handler;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.IFuelHandler;

public class InventoryHandler implements IFuelHandler, ICraftingHandler
{
	HashMap<Integer, String> ids = new HashMap<Integer, String>();
	
	public static InventoryHandler instance = new InventoryHandler();
	
	public static void registerItemGui(int id, String invName)
	{
		instance.ids.put(id, invName);
	}
	
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return 0;
		}
		if(fuel != null)
		{
			int id = fuel.itemID;
			int meta = fuel.getItemDamage();
			if(id == APIItems.gasCell.itemID)
			{
				return 4000;
			}
			else if(id == APIItems.gasBucket.itemID)
			{
				return 4000;
			}
			else if(id == APIItems.gasCellC.itemID)
			{
				return 20000;
			}
			else if(id == Item.gunpowder.itemID)
			{
				return 2400;
			}
		}
		return 0;
	}
	
	public static boolean delay = false;
	
	@ForgeSubscribe
	public void getItemDrops(ItemTossEvent evt)
	{
		if(delay)
		{
			delay = false;
			return;
		}
		ItemStack stack = evt.entityItem.getEntityItem();
		if(!ids.containsKey(stack.itemID))
		{
			return;
		}
		String toCompare = ids.get(stack.itemID);
		EntityPlayer player = evt.player;
		if(player != null && player.openContainer != null && player.openContainer instanceof AdvContainer && ((AdvContainer)player.openContainer).getInvName().equals(toCompare))
		{
			delay = true;
			((AdvContainer)player.openContainer).getTile().onPlayerCloseContainer(player);
			player.closeScreen();
		}
	}

	@Override
	public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix)
	{
		if(player != null)
		{
			HashMap<String, Boolean> flag = PlayerHandler.flags.get(player.username);
			if(flag != null && flag.containsKey("CraftHungry") && flag.get("CraftHungry"))
			{
				int amount = 0;
				for(int i = 0;i<craftMatrix.getSizeInventory();i++)
				{
					if(craftMatrix.getStackInSlot(i) != null)
					{
						amount++;
					}
				}
				float result = (float)amount * 0.025F;
				player.getFoodStats().addExhaustion(result);
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item)
	{
		
	}
	
	@ForgeSubscribe
	public void onItemToolTipp(ItemTooltipEvent evt)
	{
		ItemStack stack = evt.itemStack;
		if(stack != null)
		{
			if(stack.getItem() instanceof IBCBattery)
			{
				IBCBattery battery = (IBCBattery)stack.getItem();
				evt.toolTip.add("Transferlimit: "+battery.getTransferlimit(stack)+" MJ");
				evt.toolTip.add("Stored Energy: "+battery.getStoredMJ(stack)+" / "+battery.getMaxMJStorage(stack)+" MJ");
			}
			if(stack.getItem().itemID == Item.record11.itemID)
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt != null && nbt.hasKey("Radio"))
				{
					String text = evt.toolTip.get(0);
					evt.toolTip.clear();
					evt.toolTip.add(text);
				}
			}
		}
		
	}
}
