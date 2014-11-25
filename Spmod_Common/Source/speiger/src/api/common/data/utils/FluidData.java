package speiger.src.api.common.data.utils;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidData implements IFluidInfo
{
	Fluid fluid;
	int amount;
	
	public FluidData(FluidStack par1)
	{
		this(par1.getFluid(), par1.amount);
	}
	
	public FluidData(Fluid par1)
	{
		this(par1, 0);
	}
	
	public FluidData(Fluid par1, int par2)
	{
		fluid = par1;
		amount = par2;
	}

	@Override
	public FluidStack getResult()
	{
		return new FluidStack(fluid, amount);
	}
	
}
