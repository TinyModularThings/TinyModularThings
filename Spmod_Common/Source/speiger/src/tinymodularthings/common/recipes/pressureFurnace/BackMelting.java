package speiger.src.tinymodularthings.common.recipes.pressureFurnace;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.utils.InventoryUtil;

public class BackMelting implements IPressureFurnaceRecipe
{
	ItemStack input;
	ItemStack combiner;
	ItemStack output;
	
	public BackMelting(ItemStack in, ItemStack comb, ItemStack result)
	{
		input = in.copy();
		combiner = comb.copy();
		output = result.copy();
	}
	
	
	@Override
	public ItemStack getOutput()
	{
		return output.copy();
	}
	
	@Override
	public ItemStack getInput()
	{
		return input.copy();
	}
	
	@Override
	public ItemStack getSecondInput()
	{
		return null;
	}
	
	@Override
	public ItemStack getCombiner()
	{
		return combiner.copy();
	}
	
	@Override
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(input != null && secondInput == null && combiner != null)
		{
			if(InventoryUtil.isItemEqual(combiner, this.combiner) && InventoryUtil.isItemEqual(input, this.input) && input.stackSize >= (this.input.stackSize * times))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		input.stackSize -= (this.input.stackSize * times);
	}
	
	@Override
	public boolean isMultiRecipe()
	{
		return false;
	}
	
	@Override
	public boolean usesCombiner()
	{
		return false;
	}
	
	@Override
	public int getRequiredCookTime()
	{
		return 200;
	}
	
}
