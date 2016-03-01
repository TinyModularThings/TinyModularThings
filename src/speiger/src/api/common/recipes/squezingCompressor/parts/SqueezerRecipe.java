package speiger.src.api.common.recipes.squezingCompressor.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.api.common.recipes.squezingCompressor.EnumRecipeType;
import speiger.src.api.common.recipes.util.RecipeHardness;
import speiger.src.api.common.recipes.util.Result;

public class SqueezerRecipe implements INormalRecipe
{
	ItemStack recipeInput;
	Result[] result;
	
	public SqueezerRecipe(ItemStack par1, Result...par2)
	{
		recipeInput = par1;
		result = par2;
	}
	
	@Override
	public ItemStack getItemInput()
	{
		return recipeInput;
	}
	
	@Override
	public FluidStack[] getFluidInput()
	{
		return null;
	}
	
	@Override
	public Result[] getResults()
	{
		return result;
	}
	
	@Override
	public boolean matches(ItemStack input, FluidTank first, FluidTank second, World world)
	{
		if(recipeInput.isItemEqual(input) && input.stackSize >= recipeInput.stackSize)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public EnumRecipeType getRecipeType()
	{
		return EnumRecipeType.Sqeezing;
	}
	
	@Override
	public void runResult(ItemStack input, FluidTank first, FluidTank second, World world)
	{
		input.stackSize -= recipeInput.stackSize;
	}
	
	@Override
	public RecipeHardness getComplexity()
	{
		return RecipeHardness.Extrem_Easy;
	}
	
}
