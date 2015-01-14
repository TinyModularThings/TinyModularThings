package speiger.src.api.common.utils;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import speiger.src.spmodapi.common.lib.bc.IStackFilter;

public class FluidUtils
{
	
	public static int getTankForFluidFilter(ItemStack[] par1, FluidStack stack)
	{
		for (int i = 0; i < par1.length; i++)
		{
			ItemStack item = par1[i];
			if (item != null && FluidContainerRegistry.isFilledContainer(item))
			{
				FluidStack fluid = FluidContainerRegistry.getFluidForFilledItem(item);
				if (fluid.isFluidEqual(stack))
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	
	public static FluidItemFilter getFluidFilter(Fluid...par1)
	{
		return new FluidItemFilter(par1);
	}
	
	public static Fluid getMobEssens()
	{
		return FluidRegistry.getFluid("mobessence");
	}
	
	public static Fluid getLiquidExp()
	{
		return FluidRegistry.getFluid("xpjuice");
	}
	
	public static boolean MFRExp()
	{
		return getMobEssens() != null;
	}
	
	public static boolean OpenBlocksExp()
	{
		return getLiquidExp() != null;
	}
	
	/**
	 * My Filled Tank finder (internal tanks)
	 */
	public static FluidTank getFirstFilledTank(FluidTank[] tanks)
	{
		if (tanks == null || tanks.length == 0)
		{
			return null;
		}
		FluidTank possibleTank = null;
		
		for (int i = 0; i < tanks.length; i++)
		{
			FluidTank cuTank = tanks[i];
			if (cuTank != null && cuTank.getFluidAmount() > 0)
			{
				possibleTank = cuTank;
				break;
			}
		}
		
		return possibleTank;
		
	}
	
	/**
	 * My FluidTank finder for my Hopper so the hopper Automaticly try to fill
	 * tanks which contains Its a helper for my Hopper
	 */
	public static FluidTank getPossibleTankFromFluid(FluidTank[] tanks, FluidStack stack)
	{
		FluidTank tank = null;
		
		for (int i = 0; i < tanks.length; i++)
		{
			FluidTank cuTank = tanks[i];
			if (cuTank != null && cuTank.getFluid() != null && cuTank.getCapacity() > cuTank.getFluid().amount)
			{
				FluidStack stacks = cuTank.getFluid();
				if (stacks != null && stacks.isFluidEqual(stack))
				{
					tank = cuTank;
					break;
				}
			}
		}
		
		if (tank == null)
		{
			tank = getFirstEmptyTank(tanks);
		}
		
		return tank;
		
	}
	
	/**
	 * The Function to find the first empty tank.
	 */
	
	public static FluidTank getFirstEmptyTank(FluidTank[] tanks)
	{
		FluidTank tank = null;
		for (int i = 0; i < tanks.length; i++)
		{
			FluidTank cuTank = tanks[i];
			if (cuTank != null && cuTank.getFluid() == null)
			{
				tank = cuTank;
				break;
			}
		}
		return tank;
	}
	
	public static FluidTank getPossibleTankFromFluidAndFilter(FluidTank[] tanks, FluidStack stack, ItemStack[] items)
	{
		FluidTank tank = null;
		
		for (int i = 0; i < tanks.length; i++)
		{
			FluidTank cuTank = tanks[i];
			if (cuTank != null && cuTank.getCapacity() > cuTank.getFluidAmount())
			{
				FluidStack stacks = cuTank.getFluid();
				if ((items[i] != null && FluidContainerRegistry.isFilledContainer(items[i]) && FluidContainerRegistry.getFluidForFilledItem(items[i]).isFluidEqual(stack)))
				{
					tank = cuTank;
					break;
				}
			}
		}
		
		if (tank == null)
		{
			tank = getFirstValidTank(tanks, stack, items);
		}
		
		return tank;
	}
	
	public static FluidTank getFirstValidTank(FluidTank[] tanks, FluidStack input, ItemStack[] items)
	{
		for (int i = 0; i < tanks.length; i++)
		{
			ItemStack stack = items[i];
			if (stack == null)
			{
				FluidTank tank = tanks[i];
				if (tank.getFluid() == null)
				{
					return tank;
				}
				else
				{
					if (tank.getFluid().isFluidEqual(input))
					{
						return tank;
					}
				}
			}
		}
		
		return null;
	}
	
	public static boolean areTankFull(IFluidHandler tank)
	{
		ArrayList<FluidTankInfo> list = new ArrayList<FluidTankInfo>();
		for (ForgeDirection dir : ForgeDirection.values())
		{
			for (FluidTankInfo info : tank.getTankInfo(dir))
			{
				if (info.fluid == null)
				{
					return false;
				}
				if (info.capacity > info.fluid.amount)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	static class FluidItemFilter implements IStackFilter
	{
		Fluid[] fluids;
		
		public FluidItemFilter(Fluid...par1)
		{
			fluids = par1;
			if(fluids == null)
			{
				fluids = new Fluid[0];
			}
		}
		
		@Override
		public boolean matches(ItemStack stack)
		{
			if(fluids == null || fluids.length == 0 || stack == null || (FluidContainerRegistry.getFluidForFilledItem(stack) == null && !(stack.getItem() instanceof IFluidContainerItem)))
			{
				return false;
			}
			for(int i = 0;i<fluids.length;i++)
			{
				if(stack.getItem() instanceof IFluidContainerItem)
				{
					IFluidContainerItem item = (IFluidContainerItem)stack.getItem();
					if(item.getFluid(stack) != null && item.getFluid(stack).fluidID == fluids[i].getID())
					{
						return true;
					}
				}
				else
				{
					if(FluidContainerRegistry.getFluidForFilledItem(stack).fluidID == fluids[i].getID())
					{
						return true;
					}
				}
			}
			return false;
		}
		
	}
}
