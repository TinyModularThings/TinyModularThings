package speiger.src.spmodapi.common.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidTank;

public class AdvancedFluidTank extends FluidTank
{
	private String tankName;
	
	public AdvancedFluidTank(TileEntity tile, String name, int capacity)
	{
		super(capacity);
		tankName = name;
		this.tile = tile;
	}
	
	public String getName()
	{
		return tankName;
	}
	
}
