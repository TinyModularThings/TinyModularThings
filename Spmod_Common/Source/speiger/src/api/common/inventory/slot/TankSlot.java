package speiger.src.api.common.inventory.slot;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidTank;
import speiger.src.api.common.world.blocks.BlockStack;

public class TankSlot
{
	FluidTank tank;
	int x;
	int y;
	
	public TankSlot(FluidTank par0, int xCoord, int yCoord)
	{
		tank = par0;
		x = xCoord;
		y = yCoord;
	}
	
	public FluidTank getTank()
	{
		return tank;
	}
	
	public int getXCoord()
	{
		return x;
	}
	
	public int getYCoord()
	{
		return y;
	}
	
	public List<String> getTankInfo()
	{
		ArrayList<String> texts = new ArrayList<String>();
		if (tank != null)
		{
			texts.add("Fluid Tank");
			texts.add("Stored Fluid: " + getFluidName());
			texts.add("Stored Amount: " + tank.getFluidAmount() + "mB / " + tank.getCapacity() + "mB");
		}
		return texts;
	}
	
	public String getFluidName()
	{
		if (tank != null)
		{
			if (tank.getFluid() != null)
			{
				if (tank.getFluid().getFluid().canBePlacedInWorld())
				{
					return new BlockStack(tank.getFluid().getFluid().getBlockID()).getBlockDisplayName();
				}
				else
				{
					if (tank.getFluid().getFluid().getName().startsWith("tile."))
					{
						return "Unknowen Fluid";
					}
					else
					{
						return tank.getFluid().getFluid().getName();
					}
				}
			}
		}
		return "Nothing Stored";
	}
}
