package speiger.src.spmodapi.common.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.common.IFuelHandler;

public class InventoryHandler implements IFuelHandler
{

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
	
	
}
