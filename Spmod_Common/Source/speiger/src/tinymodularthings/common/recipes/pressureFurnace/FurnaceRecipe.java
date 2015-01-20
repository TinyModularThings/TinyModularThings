package speiger.src.tinymodularthings.common.recipes.pressureFurnace;


import java.util.List;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class FurnaceRecipe implements IPressureFurnaceRecipe
{
	ItemStack input;
	ItemStack output;
	
	public FurnaceRecipe(int id, ItemStack item)
	{
		input = new ItemStack(id, 1, PathProxy.getRecipeBlankValue());
		output = item.copy();
	}
	
	public FurnaceRecipe(List<Integer> par1, ItemStack par2)
	{
		input = new ItemStack(par1.get(0), 1, par1.get(1));
		output = par2.copy();
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
		return null;
	}
	
	@Override
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(input != null && secondInput == null && combiner != null)
		{
			if(InventoryUtil.isItemEqual(input, this.input) && input.stackSize >= times)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		input.stackSize-=times;
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
		// TODO Auto-generated method stub
		return 0;
	}
	
}
