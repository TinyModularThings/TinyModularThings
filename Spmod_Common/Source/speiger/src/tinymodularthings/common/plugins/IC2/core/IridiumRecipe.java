package speiger.src.tinymodularthings.common.plugins.IC2.core;

import ic2.api.item.Items;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class IridiumRecipe implements IPressureFurnaceRecipe
{
	
	@Override
	public ItemStack getOutput()
	{
		return Items.getItem("iridiumOre");
	}
	
	@Override
	public ItemStack getInput()
	{
		return new ItemStack(TinyItems.IridiumDrop, 5);
	}
	
	@Override
	public ItemStack getSecondInput()
	{
		return null;
	}
	
	@Override
	public ItemStack getCombiner()
	{
		return null;
	}
	
	@Override
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(secondInput != null || combiner != null || input == null)
		{
			return false;
		}
		if(input.itemID == getInput().itemID && input.stackSize >= (5 * times))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		input.stackSize-=(5 * times);
	}
	
	@Override
	public boolean isMultiRecipe()
	{
		return true;
	}
	
	@Override
	public boolean usesCombiner()
	{
		return false;
	}
	
	@Override
	public int getRequiredCookTime()
	{
		return 12000;
	}
	
}
