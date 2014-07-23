package speiger.src.tinymodularthings.common.recipes.recipeMaker;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import speiger.src.api.blocks.BlockStack;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.BlockPipe;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.InterfaceRecipe;

public class PipeRecipes
{
	public static void loadPipeRecipes()
	{
		for (int i = 0; i < BlockPipe.getRecipesLenght(); i++)
		{
			Block output = BlockPipe.getBasicPipes()[i];
			BlockStack slab = BlockPipe.getSlabs()[i];
			CraftingManager.getInstance().addRecipe(new ItemStack(output, 16), new Object[] { "XXX", "YCY", "XXX", 'X', slab.asItemStack(), 'C', Item.silk, 'Y', Block.glass });
		}
		upgradeRecipes();
		
		CraftingManager.getInstance().getRecipeList().add(new InterfaceRecipe());
	}
	
	public static void upgradeRecipes()
	{
		int size = BlockPipe.getRecipesLenght();
		for (int i = 1; i < size; i++)
		{
			Block output = BlockPipe.getBasicPipes()[i];
			Block input = BlockPipe.getBasicPipes()[i - 1];
			BlockStack slapType = BlockPipe.getSlabs()[i];
			
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(output, 3), new Object[] { slapType.asItemStack(), input, input, input });
		}
	}
}