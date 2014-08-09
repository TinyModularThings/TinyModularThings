package speiger.src.spmodapi.common.recipes;

import speiger.src.spmodapi.common.recipes.basic.BasicRecipes;
import speiger.src.spmodapi.common.recipes.basic.ExpRecipes;
import speiger.src.spmodapi.common.recipes.basic.HempRecipes;
import speiger.src.spmodapi.common.recipes.basic.LampRecipes;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class SpmodRecipeRegistry
{
	public static void loadRecipes()
	{
		PathProxy proxy = new PathProxy();
		BasicRecipes.load(proxy);
		HempRecipes.load(proxy);
		LampRecipes.load(proxy);
		ExpRecipes.load(proxy);
	}
	



}
