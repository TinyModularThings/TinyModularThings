package speiger.src.tinymodularthings.common.utils.fluids;

import java.math.RoundingMode;
import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;

import com.google.common.math.DoubleMath;

public class SharedFluidTank
{
	private TinyTank core;
	private ArrayList<TinyTank> allTanks = new ArrayList<TinyTank>();
	private int TotalSpace = 0;
	private int totalFluid = 0;
	private int drained = 0;
	private FluidStack fluid = null;
	
	public SharedFluidTank(TinyTank par1)
	{
		core = par1;
		allTanks.add(par1);
		if(par1.getTank().getFluid() != null)
		{
			fluid = par1.getTank().getFluid();
			fluid.amount = 0;
		}
	}
	
	public TinyTank getCore()
	{
		return core;
	}
	
	public ArrayList<TinyTank> getAllTanks()
	{
		return allTanks;
	}
	
	public FluidStack getFluid()
	{
		return fluid;
	}
	
	public int getTotalSpace()
	{
		return TotalSpace;
	}
	
	public int getStoredFluid()
	{
		return totalFluid;
	}
	
	public void addTank(TinyTank par1)
	{
		if(!contains(par1))
		{
			if(par1.getTank().getFluid() == null || this.getFluid() == null || this.getFluid().isFluidEqual(par1.getTank().getFluid()))
			{
				allTanks.add(par1);
				this.totalFluid = par1.getTank().getFluidAmount();
				this.TotalSpace = par1.getTank().getCapacity();
			}
		}
	}
	
	public boolean contains(TinyTank par1)
	{
		for(TinyTank tank : allTanks)
		{
			if(tank.getPosition().isThisPosition(par1.getPosition()))
			{
				return true;
			}
		}
		return false;
	}
	
	public void adjust()
	{

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
		if(par1.getFluid() == null || this.getFluid() == null)
		{
			return true;
		}
		
		if(par1.getFluid() != null && this.getFluid() != null)
		{
			return par1.getFluid().isFluidEqual(this.getFluid());
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
		if(this.getCore().getPosition().isThisPosition(par1.getPosition()))
		{
			core = null;
		}
		if(!allTanks.isEmpty() && core == null)
		{
			core = allTanks.get(0);
		}
	}

	
	public int fill(FluidStack stack, boolean doFill)
	{
		if(stack == null)
		{
			return 0;
		}
		
		if(this.fluid == null && doFill)
		{
			fluid = stack.copy();
			fluid.amount = 0;
		}
		
		if(fluid != null && !fluid.isFluidEqual(stack))
		{
			return 0;
		}
		int toAdd = stack.amount;
		
		if(totalFluid + toAdd > TotalSpace)
		{
			int added = toAdd - ((totalFluid + toAdd) - TotalSpace);
			if(doFill)
			{
				totalFluid += added;
			}
			return added;
		}
		else
		{
			if(doFill)
			{
				totalFluid += toAdd;
			}
			return toAdd;
		}
		
	}

	public FluidStack drain(int amount, boolean doDrain)
	{
		if(fluid == null)
		{
			return null;
		}
		FluidStack stack = fluid.copy();
		if(amount > totalFluid)
		{
			stack.amount = totalFluid;
			if(doDrain)
			{
				totalFluid = 0;
				fluid = null;
			}
			return stack;
		}
		else
		{
			stack.amount = amount;
			if(doDrain)
			{
				totalFluid -= amount;
				if(totalFluid - amount <= 0)
				{
					fluid = null;
				}
			}
			return stack;
		}
	}
	
	public void onTick()
	{
		this.drained = 0;
		FluidStack stack = null;
		
		if(fluid != null)
		{
			stack = fluid.copy();
		}
		
		double per = this.getPercentFromSharedTank();
		
		for(TinyTank tank : this.getAllTanks())
		{
			if(fluid == null)
			{
				tank.getTank().setFluid(null);
			}
			else
			{
				stack.amount = this.drain(this.getFluidSizeFromProzent(tank.getTank(), per));
				tank.getTank().setFluid(stack);
			}
		}
	}
	
	public int getFluidSizeFromProzent(FluidTank tank, double prozent)
	{
		double one = ((double)1 / (double)tank.getCapacity())*100;
		if(this.allTanks.size() == 1)
		{
			return this.totalFluid;
		}
		return DoubleMath.roundToInt(prozent / one, RoundingMode.HALF_EVEN);
	}
	
	public double getPercentFromSharedTank()
	{
		double per = ((double)totalFluid / (double)TotalSpace) * 100;;
		return per;
	}
	
	public FluidStack getFluidForTinyTank(TinyTank par1)
	{
		FluidStack stack = null;
		if(fluid != null)
		{
			stack = fluid.copy();
		}
		else
		{
			return null;
		}
		stack.amount = this.getFluidSizeFromProzent(par1.getTank(), getPercentFromSharedTank());
		
		return stack;
	}
	
	private int drain(int req)
	{
		if(drained + req <= totalFluid)
		{
			this.drained += req;
			return req;
		}
		int total = req - ((drained + req) - totalFluid);
		this.drained += total;
		return total;
	}
	
	public void readFromNBT(NBTTagCompound par1)
	{
		totalFluid = par1.getInteger("TotalF");
		TotalSpace = par1.getInteger("TotalS");
		if(par1.hasKey("Fluid"))
		{
			NBTTagCompound nbt = par1.getCompoundTag("Fluid");
			fluid = FluidStack.loadFluidStackFromNBT(nbt);
		}
	}
	
	public void writeToNBT(NBTTagCompound par1)
	{
		par1.setInteger("TotalF", totalFluid);
		par1.setInteger("TotalS", TotalSpace);
		if(fluid != null)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			fluid.writeToNBT(nbt);
			par1.setCompoundTag("Fluid", nbt);
		}

	}
	
}
