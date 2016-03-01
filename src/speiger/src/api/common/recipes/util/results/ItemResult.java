package speiger.src.api.common.recipes.util.results;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.recipes.util.Result;

public class ItemResult implements Result
{
	int chance;
	ItemStack item;
	
	public ItemResult(ItemStack par1, int par2)
	{
		item = par1;
		chance = par2;
	}
	
	@Override
	public boolean hasItemResult()
	{
		return true;
	}
	
	@Override
	public ItemStack getItemResult()
	{
		if(item == null)
		{
			return null;
		}
		return item.copy();
	}
	
	@Override
	public int getItemResultChance()
	{
		return chance;
	}
	
	@Override
	public boolean hasFluidResult()
	{
		return false;
	}
	
	@Override
	public FluidStack getFluidResult()
	{
		return null;
	}
	
	@Override
	public int getFluidResultChance()
	{
		return 0;
	}
	
}
