package speiger.src.api.common.registry.recipes.output;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeOutput
{
	ItemStack output;
	int chance;
	
	public RecipeOutput(Block par1)
	{
		this(par1, 100);
	}
	
	public RecipeOutput(Block par1, int chance)
	{
		this(new ItemStack(par1), chance);
	}
	
	public RecipeOutput(Item par1)
	{
		this(par1, 100);
	}
	
	public RecipeOutput(Item par1, int chance)
	{
		this(new ItemStack(par1), chance);
	}
	
	public RecipeOutput(ItemStack output)
	{
		this(output, 100);
	}
	
	public RecipeOutput(ItemStack output, int chance)
	{
		this.output = output;
		this.chance = chance;
	}
	
	public ItemStack getOutput()
	{
		return output;
	}
	
	public int getChance()
	{
		return chance;
	}
}
