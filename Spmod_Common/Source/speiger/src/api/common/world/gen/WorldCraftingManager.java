package speiger.src.api.common.world.gen;

import java.util.HashMap;

import speiger.src.api.common.world.blocks.BlockStack;

public class WorldCraftingManager
{
	static HashMap<BlockStack, IWorldCraftingRecipe> recipes = new HashMap<BlockStack, IWorldCraftingRecipe>();
	
	
	public static boolean registerWorldCraftingRecipe(IWorldCraftingRecipe par1)
	{
		if(par1.getStartingBlock() == null || par1.getFinsiherBlock() == null || recipes.containsKey(par1.getStartingBlock()))
		{
			return false;
		}
		
		recipes.put(par1.getStartingBlock(), par1);
		return true;
	}


	public static IWorldCraftingRecipe getRecipe(BlockStack par1)
	{
		return recipes.get(par1);
	}
	
	public static HashMap<BlockStack, IWorldCraftingRecipe> getRecipeList()
	{
		return recipes;
	}
}
