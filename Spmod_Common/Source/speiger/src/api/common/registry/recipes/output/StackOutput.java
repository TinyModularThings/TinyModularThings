package speiger.src.api.common.registry.recipes.output;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.IStackInfo;

public class StackOutput extends RecipeOutput
{
	IStackInfo stack;
	public StackOutput(IStackInfo item, int chance)
	{
		super((ItemStack)null, chance);
		stack = item;
	}
	
	public StackOutput(IStackInfo item)
	{
		super((ItemStack)null);
		stack = item;
	}

	@Override
	public ItemStack getOutput()
	{
		return stack.getResult();
	}
	
	
	
}
