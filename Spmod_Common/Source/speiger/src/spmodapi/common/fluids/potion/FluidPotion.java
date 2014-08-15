package speiger.src.spmodapi.common.fluids.potion;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidPotion extends Fluid
{
	public FluidPotion(String fluidName)
	{
		super(fluidName);
	}
	
	@Override
	public int getColor(FluidStack stack)
	{
		return stack.tag.getInteger("Color");
	}
}