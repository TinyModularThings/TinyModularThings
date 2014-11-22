package speiger.src.api.common.registry.recipes.uncrafter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.data.utils.ItemNBTData;
import speiger.src.api.common.data.utils.ResultData;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;

public class UncrafterRecipeList
{
	private static UncrafterRecipeList instance = new UncrafterRecipeList();
	
	public static UncrafterRecipeList getInstance()
	{
		return instance;
	}
	
	private HashMap<IStackInfo, List<RecipeOutput>> recipeList = new HashMap<IStackInfo, List<RecipeOutput>>();
	
	public void addUncraftingRecipe(ItemStack input, ItemStack output)
	{
		addUncraftingRecipe(input, new RecipeOutput(output));
	}
	
	public void addUncraftingRecipe(ItemStack input, RecipeOutput...output)
	{
		addUncraftingRecipe(new ItemData(input), output);
	}
	
	public void addNBTUncraftingRecipe(ItemStack input, RecipeOutput...output)
	{
		addUncraftingRecipe(new ItemNBTData(input), output);
	}
	
	/**
	 * For custom Results, So you do not need to make recipe override in my mod just implement this result in your class.^^"
	 */
	public void addUncraftingRecipe(IStackInfo input, RecipeOutput...output)
	{
		//Returns an ArrayList but will be released as List
		recipeList.put(input, Arrays.asList(output));
	}
	
	public boolean hasRecipeOutput(ItemStack input)
	{
		List<RecipeOutput> result = getRecipeOutput(input);
		return result != null && result.size() > 0;
	}
	
	public List<RecipeOutput> getRecipeOutput(ItemStack input)
	{
		return recipeList.get(new ResultData(input));
	}
	
}
