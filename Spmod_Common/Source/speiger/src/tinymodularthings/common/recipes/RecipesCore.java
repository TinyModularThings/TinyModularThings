package speiger.src.tinymodularthings.common.recipes;

import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.PipeRecipes;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.TinyRecipes;

public class RecipesCore
{
	public static void loadAllRecipes()
	{
		PathProxy proxy = new PathProxy();
		OreRecipes.loadOreRecipes();
		PipeRecipes.loadPipeRecipes();
		PreRecipes.initRecipes();
		TinyRecipes.init(proxy);
	}
}
