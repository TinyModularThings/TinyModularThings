package speiger.src.tinymodularthings.common.recipes;

import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.recipes.pressureFurnace.PressureRecipes;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.EnergyRecipes;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.PipeRecipes;
import speiger.src.tinymodularthings.common.recipes.recipeMaker.TinyRecipes;
import speiger.src.tinymodularthings.common.recipes.uncrafter.UncraftingRecipes;

public class RecipesCore
{
	public static void loadAllRecipes()
	{
		PathProxy proxy = new PathProxy();
		PressureRecipes.initRecipes(proxy);
		PipeRecipes.loadPipeRecipes();
		PreRecipes.initRecipes();
		TinyRecipes.init(proxy);
		WCraftingRecipes.init();
		UncraftingRecipes.initRecipes(proxy);
		EnergyRecipes.init(proxy);
	}
}
