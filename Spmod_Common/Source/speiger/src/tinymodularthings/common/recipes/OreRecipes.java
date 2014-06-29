package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;

public class OreRecipes
{
	public static void loadOreRecipes()
	{
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 0), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 1), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyBlocks.ores, 3, 0), new ItemStack(TinyBlocks.ores, 1, 1), (ItemStack) null, new ItemStack(TinyItems.ingots, 8, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.ingots, 3, EnumIngots.Copper.getIngotMeta()), new ItemStack(TinyItems.ingots, 1, EnumIngots.Tin.getIngotMeta()), (ItemStack) null, new ItemStack(TinyItems.ingots, 4, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(Item.minecartEmpty), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 5), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.bucketEmpty), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 3), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 7), false, false);
	}
	
}
