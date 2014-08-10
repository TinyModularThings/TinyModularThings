package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;

public class OreRecipes
{
	public static void loadOreRecipes()
	{
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Blocks.sand, 2), new ItemStack(Items.coal, 1, 0), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Blocks.sand, 2), new ItemStack(Items.coal, 1, 1), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyBlocks.ores, 3, 0), new ItemStack(TinyBlocks.ores, 1, 1), (ItemStack) null, new ItemStack(TinyItems.ingots, 8, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.ingots, 3, EnumIngots.Copper.getIngotMeta()), new ItemStack(TinyItems.ingots, 1, EnumIngots.Tin.getIngotMeta()), (ItemStack) null, new ItemStack(TinyItems.ingots, 4, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(Items.minecart), null, new ItemStack(Items.lava_bucket), new ItemStack(Items.iron_ingot, 5), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Items.bucket), null, new ItemStack(Items.lava_bucket), new ItemStack(Items.iron_ingot, 3), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Items.iron_horse_armor), null, new ItemStack(Items.lava_bucket), new ItemStack(Items.iron_ingot, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Items.golden_horse_armor), null, new ItemStack(Items.lava_bucket), new ItemStack(Items.gold_ingot, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Items.diamond_horse_armor), null, new ItemStack(Items.lava_bucket), new ItemStack(Items.diamond, 7), false, false);
	}
	
}
