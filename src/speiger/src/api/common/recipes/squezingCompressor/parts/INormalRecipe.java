package speiger.src.api.common.recipes.squezingCompressor.parts;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.api.common.recipes.squezingCompressor.EnumRecipeType;
import speiger.src.api.common.recipes.util.RecipeHardness;
import speiger.src.api.common.recipes.util.Result;

public interface INormalRecipe
{
	public ItemStack getItemInput();
	
	public FluidStack[] getFluidInput();
	
	public Result[] getResults();
	
	public boolean matches(ItemStack input, FluidTank first, FluidTank second, World world);
	
	public EnumRecipeType getRecipeType();
	
	public void runResult(ItemStack input, FluidTank first, FluidTank second, World world);
	
	public RecipeHardness getComplexity();
}
