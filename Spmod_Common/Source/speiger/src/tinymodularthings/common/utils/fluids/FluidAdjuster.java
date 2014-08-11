package speiger.src.tinymodularthings.common.utils.fluids;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidAdjuster
{
	FluidTank[] tanks;
	FluidStack fluid;
	int totalFluid = 0;
	int totalSpace = 0;
	int removed = 0;
	
	public FluidAdjuster(FluidStack par0, FluidTank...par1)
	{
		if(par0 == null)
		{
			fluid = null;
		}
		else
		{
			fluid = par0.copy();
		}
		tanks = par1;
		for(int i = 0;i<tanks.length;i++)
		{
			FluidTank tank = tanks[i];
			this.fill(tank);
		}
	}
	
	public void adjust()
	{
		if(fluid == null)
		{
			return;
		}
		for(FluidTank tank : tanks)
		{
			this.setFluid(tank);
		}

		double per = ((double)totalFluid / (double)totalSpace) * 100;
		
		for(int i = 0;i<tanks.length;i++)
		{
			FluidTank tank = tanks[i];
			tank.getFluid().amount = drain(this.getFluidSizeFromProzent(tank, per));
		}
	}
	
	private int getFluidSizeFromProzent(FluidTank tank, double prozent)
	{
		double one = ((double)1 / (double)tank.getCapacity())*100;
		return (int) (prozent / one);
	}
	
	public void fill(FluidTank tank)
	{
		totalFluid += tank.getFluidAmount();
		totalSpace += tank.getCapacity();
		if(tank.getFluid() == null && fluid != null)
		{
			tank.setFluid(fluid);
		}
	}
	
	public void setFluid(FluidTank tank)
	{
		tank.setFluid(fluid);
	}
	
	public int drain(int req)
	{
		if(removed + req <= totalFluid)
		{
			this.removed += req;
			return req;
		}
		int total = req - (removed + req - totalFluid);
		this.removed += req;
		return total;
	}
	
	//Useage only for IFluidHandler Function.
	public int fill(FluidStack fluid, boolean doFill)
	{
		
		if(fluid.isFluidEqual(this.fluid))
		{
			int amount = fluid.amount;
			if(totalFluid + amount >= totalSpace)
			{
				int added = amount - ((totalFluid + amount) - totalSpace);
				if(doFill)
				{
					totalFluid = totalSpace;
				}
				return added;
			}
			else
			{
				if(doFill)
				{
					totalFluid += amount;
				}
				return amount;
			}
		}
		if(this.fluid == null && fluid != null)
		{
			if(doFill)
			{
				this.fluid = fluid.copy();
				this.fluid.amount = 0;
				this.totalFluid += fluid.amount;
			}
			return fluid.amount;
		}
		return 0;
	}
	
	public FluidStack drain(int amount, boolean doDrain)
	{
		if(totalFluid - amount < 0)
		{
			int removed = amount - totalFluid;
			if(doDrain)
			{
				totalFluid = 0;
			}
			fluid.amount = removed;
			return fluid;
		}
		else
		{
			
			if(doDrain)
			{
				totalFluid -= amount;
			}
			fluid.amount = amount;
			
			return fluid;
		}
	}
	
	public boolean tankIsFull()
	{
		return totalFluid >= totalSpace;
	}
	
	public int getFluidAmount()
	{
		return totalFluid;
	}
}
