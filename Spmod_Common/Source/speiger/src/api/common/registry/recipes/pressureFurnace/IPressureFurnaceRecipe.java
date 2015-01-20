package speiger.src.api.common.registry.recipes.pressureFurnace;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.INormalRecipe;

public interface IPressureFurnaceRecipe extends INormalRecipe
{
	public ItemStack getOutput();
	
	public ItemStack getInput();
	
	public ItemStack getSecondInput();
	
	public ItemStack getCombiner();
	
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times);
	
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times);
	
	public boolean isMultiRecipe();
	
	public boolean usesCombiner();
	
	public int getRequiredCookTime();
	
}
