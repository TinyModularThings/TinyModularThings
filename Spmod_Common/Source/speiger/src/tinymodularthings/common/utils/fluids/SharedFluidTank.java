package speiger.src.tinymodularthings.common.utils.fluids;

import java.math.RoundingMode;
import java.util.ArrayList;

import com.google.common.math.DoubleMath;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;

public class SharedFluidTank
{
	private TinyTank core;
	private ArrayList<TinyTank> allTanks = new ArrayList<TinyTank>();
	
	public SharedFluidTank(TinyTank par1)
	{
		core = par1;
		allTanks.add(par1);
	}
	
	
	public TinyTank getCore()
	{
		return core;
	}
	
	public ArrayList<TinyTank> getAllTanks()
	{
		return allTanks;
	}
	
	public void addTank(TinyTank par1)
	{
		if(!allTanks.contains(par1))
		{
			allTanks.add(par1);
		}
	}
	
	public void adjust()
	{

		this.defineFluid();
		FluidAdjuster adjust = this.getAdjuster();
		adjust.adjust();
	}
	
	public void defineFluid()
	{
		FluidStack fluid = null;
		if(core.getTank().getFluid() == null)
		{
			for(TinyTank par1 : allTanks)
			{
				if(par1.getTank().getFluid() != null)
				{
					fluid = par1.getTank().getFluid().copy();
					break;
				}
			}
		}
		else
		{
			fluid = core.getTank().getFluid().copy();
		}
		if(fluid != null)
		{
			fluid.amount = 0;
			for(TinyTank tank : allTanks)
			{
				if(tank.getTank().getFluid() == null)
				{
					tank.getTank().setFluid(fluid);
				}
			}
		}
	}
	
	public boolean isSame(SharedFluidTank par1)
	{
		if(par1 == null)
		{
			return false;
		}
		BlockPosition other = par1.getCore().getPosition();
		BlockPosition me = this.getCore().getPosition();
		if(me.isThisPosition(other))
		{
			return true;
		}
		return false;
	}
	
	public boolean canAddFluid(SharedFluidTank par1)
	{
		if(par1 == null)
		{
			return false;
		}
		FluidStack fluid = par1.getCore().getTank().getFluid();
		if(fluid != null)
		{
			FluidStack myFluid = this.getCore().getTank().getFluid();
			if(myFluid == null)
			{
				return true;
			}
			else if(myFluid != null && fluid.isFluidEqual(myFluid))
			{
				return true;
			}
		}
		return false;
	}


	public void replace(SharedFluidTank par1)
	{
		this.allTanks.addAll(par1.getAllTanks());
		FluidStorage.instance.replace(this);
	}


	public void removeTank(TinyTank par1)
	{
		allTanks.remove(par1);
		if(!allTanks.isEmpty())
		{
			core = allTanks.get(0);
		}
	}

	public FluidAdjuster getAdjuster()
	{
		FluidAdjuster adjust = new FluidAdjuster(allTanks, core);
		return adjust;
	}
	
	public int fill(FluidStack stack, boolean doFill)
	{
		this.defineFluid();
		if(core.getTank().getFluid() == null)
		{
			FluidStack copy = stack.copy();
			copy.amount = 0;
			core.getTank().setFluid(copy);
		}
		
		FluidAdjuster fluid = this.getAdjuster();
		int filled = fluid.fill(stack, doFill);
		fluid.adjust();
		return filled;
	}

	public FluidStack drain(int amount, boolean doDrain)
	{
		this.defineFluid();
		FluidAdjuster fluid = this.getAdjuster();
		FluidStack drained = fluid.drain(amount, doDrain);
		fluid.adjust();
		return drained;
	}
	
	
	
	public static class FluidAdjuster
	{
		FluidTank[] tanks;
		FluidStack fluid = null;
		int totalAmount = 0;
		int totalSpace = 0;
		int removed = 0;
		
		public FluidAdjuster(ArrayList<TinyTank> par1, TinyTank core)
		{
			tanks = new FluidTank[par1.size()];
			for(int i = 0;i<par1.size();i++)
			{
				FluidTank tank = par1.get(i).getTank();
				totalAmount += tank.getFluidAmount();
				totalSpace += tank.getCapacity();
				tanks[i] = tank;
			}
			FluidStack fluid = core.getTank().getFluid();
			if(fluid != null)
			{
				fluid = fluid.copy();
				fluid.amount = 0;
				this.fluid = fluid;
			}
		}
		
		public FluidStack drain(int amount, boolean doDrain)
		{
			if(fluid == null)
			{
				return null;
			}
			if(totalAmount - amount < 0)
			{
				int removed = amount - totalAmount;
				if(doDrain)
				{
					totalAmount = 0;
				}
				fluid.amount = removed;
				return fluid;
			}
			else
			{
				if(doDrain)
				{
					totalAmount -= amount;
				}
				fluid.amount = amount;
				return fluid;
			}
		}
		
		public int fill(FluidStack fluid, boolean doFill)
		{
			if(fluid == null)
			{
				return 0;
			}
			if(fluid.isFluidEqual(this.fluid))
			{
				int amount = fluid.amount;
				if(totalAmount + amount >= totalSpace)
				{
					int added = amount - ((totalAmount + amount) - totalSpace);
					if(doFill)
					{
						totalAmount = totalSpace;
					}
					return added;
				}
				else
				{
					if(doFill)
					{
						totalAmount += amount;
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
					this.totalAmount += fluid.amount;
				}
				return fluid.amount;
			}
			return 0;
		}
		
		public void adjust()
		{
			if(fluid == null)
			{
				return;
			}
			if(this.totalAmount <= 0)
			{
				for(FluidTank tank : tanks)
				{
					tank.setFluid(null);
				}
			}
			
			double per = ((double)totalAmount / (double)totalSpace) * 100;
			for(FluidTank tank : tanks)
			{
				tank.getFluid().amount = drain(this.getFluidSizeFromProzent(tank, per));
			}
		}
		
		private int getFluidSizeFromProzent(FluidTank tank, double prozent)
		{
			double one = ((double)1 / (double)tank.getCapacity())*100;
			return DoubleMath.roundToInt(prozent / one, RoundingMode.HALF_EVEN);
		}
		
		private int drain(int req)
		{
			if(removed + req <= totalAmount)
			{
				this.removed += req;
				return req;
			}
			int total = req - ((removed + req) - totalAmount);
			this.removed += req;
			return total;
		}
	}
}
