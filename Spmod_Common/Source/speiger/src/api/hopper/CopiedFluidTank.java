package speiger.src.api.hopper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class CopiedFluidTank
{
	int maxStorage;
	int stored;
	Fluid liquid;
	FluidStack info;
	public ItemStack filter;
	
	public static CopiedFluidTank[] createTanks(FluidTank[] par1)
	{
		CopiedFluidTank[] copied = new CopiedFluidTank[par1.length];
		for (int i = 0; i < par1.length; i++)
		{
			copied[i] = new CopiedFluidTank(par1[i]);
		}
		return copied;
	}
	
	public CopiedFluidTank(FluidTank par1, ItemStack filter)
	{
		this(par1);
		this.setFilter(filter);
	}
	
	public CopiedFluidTank(FluidTank par1)
	{
		this(par1.getFluidAmount(), par1.getCapacity(), par1.getFluid() != null ? par1.getFluid() : null, par1.getFluid() != null ? null : par1.getFluid().getFluid() != null ? par1.getFluid().getFluid() : null, null);
	}
	
	public CopiedFluidTank(int amount, int maxAmount, FluidStack stack, Fluid fluid, ItemStack Filter)
	{
		liquid = fluid;
		stored = amount;
		maxStorage = maxAmount;
		info = stack;
		filter = Filter;
	}
	
	public void setFilter(ItemStack par1)
	{
		filter = par1;
	}
	
	public ItemStack getFilter()
	{
		return filter;
	}
	
	public Fluid getFluidFilter()
	{
		return FluidContainerRegistry.getFluidForFilledItem(filter) != null ? FluidContainerRegistry.getFluidForFilledItem(filter).getFluid() : null;
	}
	
	public Fluid getFluid()
	{
		return liquid;
	}
	
	public int getTankSize()
	{
		return maxStorage;
	}
	
	public int getStoredFluid()
	{
		return stored;
	}
	
	public FluidStack getFluidInfo()
	{
		return info;
	}
}