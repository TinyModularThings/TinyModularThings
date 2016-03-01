package speiger.src.api.common.recipes.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface Result
{
	public boolean hasItemResult();
	
	public ItemStack getItemResult();
	
	public int getItemResultChance();
	
	public boolean hasFluidResult();
	
	public FluidStack getFluidResult();
	
	public int getFluidResultChance();
	
}
