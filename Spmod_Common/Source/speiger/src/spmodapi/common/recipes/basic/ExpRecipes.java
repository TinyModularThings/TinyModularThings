package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.items.exp.ExpBottle;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class ExpRecipes
{
	public static void load(PathProxy pp)
	{
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 0, false, false), "XYX", "X X", "XXX", 'Y', "logWood", 'X', "glass"));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 0, true, false), "XYX", "X X", "XXX", 'Y', "logWood", 'X', "ingotSilver"));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 1, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Item.diamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 1, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Item.diamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 2, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Item.emerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 2, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Item.emerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 2, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Block.blockDiamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 2, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Block.blockDiamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 3, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Block.blockEmerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 3, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Block.blockEmerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 4, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Block.dragonEgg));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles.itemID, 4, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Block.dragonEgg));
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 0), new Object[]{"XX", "XX", 'X', Block.cobblestone});
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 1), new Object[]{"XXX", "XYX", "XXX", 'X', Item.glassBottle, 'Y', Block.wood});

	}
}
