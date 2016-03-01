package speiger.src.api.common.recipes.util.results;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.recipes.util.Result;

public class FluidResult implements Result
{
	FluidStack fluid;
	int chance;
	
	public FluidResult(FluidStack par1, int par2)
	{
		fluid = par1;
		chance = par2;
	}
	
	@Override
	public boolean hasItemResult()
	{
		return false;
	}
	
	@Override
	public ItemStack getItemResult()
	{
		return null;
	}
	
	@Override
	public int getItemResultChance()
	{
		return 0;
	}
	
	@Override
	public boolean hasFluidResult()
	{
		return true;
	}
	
	@Override
	public FluidStack getFluidResult()
	{
		if(fluid == null)
		{
			return null;
		}
		return fluid.copy();
	}
	
	@Override
	public int getFluidResultChance()
	{
		return chance;
	}
	
}
