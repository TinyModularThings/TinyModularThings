package speiger.src.api.common.registry.recipes.output;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;

public class RandomOutput extends RecipeOutput
{
	ItemStack firstOutput;
	ItemStack secondOutput;
	int chance;
	public RandomOutput(ItemStack output, ItemStack secondOutput, int chanceFirstItem, int chance)
	{
		super(output, chance);
		this.chance = chanceFirstItem;
		this.firstOutput = output;
		this.secondOutput = secondOutput;
	}
	@Override
	public ItemStack getOutput()
	{
		if(CodeProxy.getRandom().nextInt(100) < chance)
		{
			return firstOutput;
		}
		return secondOutput;
	}
	
	/**
	 * @Usage: For Nei. So you do not get every tick a new Output.
	 */
	public ItemStack[] getAllOutputs()
	{
		return new ItemStack[]{firstOutput, secondOutput};
	}

	
	
}
