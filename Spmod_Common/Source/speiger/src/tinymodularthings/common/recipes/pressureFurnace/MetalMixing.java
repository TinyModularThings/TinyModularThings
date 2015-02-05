package speiger.src.tinymodularthings.common.recipes.pressureFurnace;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.utils.InventoryUtil;

public class MetalMixing implements IPressureFurnaceRecipe
{
	ItemStack input;
	ItemStack secondInput;
	ItemStack combiner;
	ItemStack result;
	boolean multi;
	boolean combUse;
	int time;
	
	public MetalMixing(ItemStack input, ItemStack secondInput, ItemStack output)
	{
		this(input, secondInput, null, output);
	}
	
	public MetalMixing(ItemStack input, ItemStack secondInput, ItemStack combiner, ItemStack output)
	{
		this.input = ItemStack.copyItemStack(input);
		this.secondInput = ItemStack.copyItemStack(secondInput);
		this.combiner = ItemStack.copyItemStack(combiner);
		this.result = ItemStack.copyItemStack(output);
	}
	
	public MetalMixing setMulti()
	{
		multi = true;
		return this;
	}
	
	public MetalMixing setTime(int par1)
	{
		time = par1;
		return this;
	}
	
	@Override
	public ItemStack getOutput()
	{
		return result;
	}
	
	@Override
	public ItemStack getInput()
	{
		return input;
	}
	
	@Override
	public ItemStack getSecondInput()
	{
		return secondInput;
	}
	
	@Override
	public ItemStack getCombiner()
	{
		return combiner;
	}
	
	@Override
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(!InventoryUtil.isItemEqualTotal(this.input, input))
		{
			return false;
		}
		if(!InventoryUtil.isItemEqualTotal(this.secondInput, secondInput))
		{
			return false;
		}
		if(!InventoryUtil.isItemEqualTotal(this.combiner, combiner))
		{
			return false;
		}
		if(this.input != null)
		{
			if(input.stackSize < (this.input.stackSize * times))
			{
				return false;
			}
		}
		if(this.secondInput != null)
		{
			if(secondInput.stackSize < (this.secondInput.stackSize * times))
			{
				return false;
			}
		}
		if(this.combiner != null)
		{
			if(combiner.stackSize < (this.combiner.stackSize * times))
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(input != null)
		{
			input.stackSize -= (this.input.stackSize * times);
		}
		if(secondInput != null)
		{
			secondInput.stackSize -= (this.secondInput.stackSize * times);
		}
		if(combiner != null && combUse)
		{
			combiner.stackSize -= (this.combiner.stackSize * times);
		}
	}
	
	@Override
	public boolean isMultiRecipe()
	{
		return multi;
	}
	
	@Override
	public boolean usesCombiner()
	{
		return combUse;
	}
	
	@Override
	public int getRequiredCookTime()
	{
		return time;
	}
	
}
