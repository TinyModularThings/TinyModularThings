package speiger.src.tinymodularthings.common.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class FuelHandler implements IFuelHandler
{
	public static void init()
	{
		GameRegistry.registerFuelHandler(new FuelHandler());
	}
	
	@Override
	public int getBurnTime(ItemStack fuel)
	{
		if (fuel != null)
		{
			if (fuel.itemID == TinyItems.netherCrystal.itemID && fuel.getItemDamage() == 3)
			{
				return TileEntityFurnace.getItemBurnTime(new ItemStack(Item.bucketLava));
			}
		}
		return 0;
	}
	
}
