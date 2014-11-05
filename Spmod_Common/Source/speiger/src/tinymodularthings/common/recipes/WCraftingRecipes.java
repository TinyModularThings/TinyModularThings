package speiger.src.tinymodularthings.common.recipes;

import speiger.src.api.common.world.gen.WorldCraftingManager;
import speiger.src.tinymodularthings.common.recipes.multiStructures.OilGeneratorCraftingRecipe;

public class WCraftingRecipes
{
	public static void init()
	{
		WorldCraftingManager.registerWorldCraftingRecipe(new OilGeneratorCraftingRecipe());
	}

}
