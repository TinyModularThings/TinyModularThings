package speiger.src.spmodapi.common.handler;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.common.IFuelHandler;

public class InventoryHandler implements IFuelHandler
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
	public void onDrop(ItemTossEvent evt)
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
	
}
