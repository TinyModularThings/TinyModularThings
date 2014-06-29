package speiger.src.tinymodularthings.common.recipes;

import speiger.src.tinymodularthings.common.recipes.recipeMaker.PipeRecipes;

public class RecipesCore
{
	public static void loadAllRecipes()
	{
		OreRecipes.loadOreRecipes();
		PipeRecipes.loadPipeRecipes();
		PreRecipes.initRecipes();
	}
}
