package speiger.src.api.common.recipes.squezingCompressor.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.api.common.recipes.squezingCompressor.EnumRecipeType;
import speiger.src.api.common.recipes.util.RecipeHardness;
import speiger.src.api.common.recipes.util.Result;

public class CompressorRecipe implements INormalRecipe
{
	Result[] results;
	ItemStack recipeItem;
	
	public CompressorRecipe(ItemStack par1, Result...par2)
	{
		recipeItem = par1;
		results = par2;
	}
	
	@Override
	public ItemStack getItemInput()
	{
		return recipeItem;
	}
	
	@Override
	public FluidStack[] getFluidInput()
	{
		return null;
	}
	
	@Override
	public Result[] getResults()
	{
		return results;
	}
	
	@Override
	public boolean matches(ItemStack input, FluidTank first, FluidTank second, World world)
	{
		if(recipeItem.isItemEqual(input) && input.stackSize >= recipeItem.stackSize)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public EnumRecipeType getRecipeType()
	{
		return EnumRecipeType.Compressor;
	}
	
	@Override
	public void runResult(ItemStack input, FluidTank first, FluidTank second, World world)
	{
		input.stackSize -= recipeItem.stackSize;
	}
	
	@Override
	public RecipeHardness getComplexity()
	{
		return RecipeHardness.Extrem_Easy;
	}
	
}
