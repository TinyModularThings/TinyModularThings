package speiger.src.tinymodularthings.common.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.tinymodularthings.common.blocks.machine.OilGenerator;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class FuelHandler implements IFuelHandler
{
	public static void init()
	{
		GameRegistry.registerFuelHandler(new FuelHandler());
		PressureFurnace.validFuels.add(Item.coal.itemID);
		PressureFurnace.fuelMeta.put(Item.coal.itemID, 0);
		PressureFurnace.fuelMeta.put(Item.coal.itemID, 1);
		PressureFurnace.validFuels.add(Block.coalBlock.blockID);
		OilGenerator.allowedItems.add(new ItemData(Item.porkRaw));
		OilGenerator.allowedItems.add(new ItemData(Item.beefRaw));
		OilGenerator.allowedItems.add(new ItemData(Item.chickenRaw));
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
