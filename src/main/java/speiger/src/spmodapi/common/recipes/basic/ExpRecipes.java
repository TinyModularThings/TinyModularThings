package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 0, false, false), "XYX", "X X", "XXX", 'Y', "logWood", 'X', "glass"));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 0, true, false), "XYX", "X X", "XXX", 'Y', "logWood", 'X', "ingotSilver"));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 1, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Items.diamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 1, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Items.diamond));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 2, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Items.emerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 2, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Items.emerald));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 2, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Blocks.diamond_block));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 2, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Blocks.diamond_block));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 3, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Blocks.emerald_block));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 3, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Blocks.emerald_block));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 4, false, false), "XYX", "X X", "XCX", 'X', "glass", 'Y', "logWood", 'C', Blocks.dragon_egg));
		pp.addRecipe(new ShapedOreRecipe(ExpBottle.getExpBottle(APIItems.expBottles, 4, true, false), "XYX", "X X", "XCX", 'X', "ingotSilver", 'Y', "logWood", 'C', Blocks.dragon_egg));
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 0), new Object[]{"XX", "XX", 'X', Blocks.cobblestone});
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 1), new Object[]{"XXX", "XYX", "XXX", 'X', Items.glass_bottle, 'Y', Blocks.planks});

	}
}
