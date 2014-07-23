package speiger.src.spmodapi.common.lib.bc;

import net.minecraft.item.ItemStack;

/**
 * 
 * @author Speiger
 * 
 */
public class SpmodStackFilter implements IStackFilter
{
	
	ItemStack input;
	
	public SpmodStackFilter(ItemStack par1)
	{
		input = par1;
	}
	
	@Override
	public boolean matches(ItemStack stack)
	{
		if (input == null)
		{
			return true;
		}
		else if (input.isItemEqual(stack))
		{
			return true;
		}
		
		return false;
	}
	
}
