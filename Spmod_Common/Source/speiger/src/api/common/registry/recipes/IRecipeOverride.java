package speiger.src.api.common.registry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.crafting.IRecipe;

public interface IRecipeOverride
{
	/**
	 * Prevents that my mod overrides your Recipes.
	 * @Created because it did override my recipes!
	 */
	public boolean preventOverriding();
	
	public static class RecipeOverriderRegistry
	{
		public static RecipeOverriderRegistry instance = new RecipeOverriderRegistry();
		
		final List<IRecipe> blacklistedRecipes = new ArrayList<IRecipe>();
		
		public void addRecipe(IRecipe par1)
		{
			if(!blacklistedRecipes.contains(par1))
			{
				blacklistedRecipes.add(par1);
			}
		}
		
		public boolean canOverride(IRecipe par1)
		{
			if(par1 == null)
			{
				return false;
			}
			if(par1 instanceof IRecipeOverride)
			{
				return !((IRecipeOverride)par1).preventOverriding();
			}
			return !blacklistedRecipes.contains(par1);
		}
	}
}
