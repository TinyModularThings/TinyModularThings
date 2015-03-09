package speiger.src.tinymodularthings.common.recipes.pressureFurnace;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

import com.google.common.base.Strings;

public class MetalMixingOre implements IPressureFurnaceRecipe
{
	boolean multi;
	boolean combUse;
	int time;
	ItemStack input;
	String oreInput;
	ItemStack secondInput;
	String oreSecondInput;
	ItemStack combiner;
	String oreCombiner;
	
	ItemStack result;
	
	public MetalMixingOre(ItemStack input, ItemStack secondInput, ItemStack output)
	{
		this(input, secondInput, null, output);
	}
	
	public MetalMixingOre(ItemStack input, ItemStack secondInput, ItemStack combiner, ItemStack output)
	{
		this.input = ItemStack.copyItemStack(input);
		this.secondInput = ItemStack.copyItemStack(secondInput);
		this.combiner = ItemStack.copyItemStack(combiner);
		this.result = ItemStack.copyItemStack(output);
		initOreData();
	}
	
	private void initOreData()
	{
		oreInput = getOreName(input);
		oreSecondInput = getOreName(secondInput);
		oreCombiner = getOreName(combiner);
	}
	
	public MetalMixingOre setCombinerUse()
	{
		combUse = true;
		return this;
	}
	
	public MetalMixingOre setMulti()
	{
		multi = true;
		return this;
	}
	
	public MetalMixingOre setTime(int par1)
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
	public boolean recipeMatches(ItemStack par1, ItemStack par2, ItemStack par3, int times)
	{
		if((isValidOre(oreInput) && oreInput.equalsIgnoreCase(getOreName(par1))) || (!InventoryUtil.isItemEqualTotal(input, par1)))
		{
			return false;
		}
		if((isValidOre(oreSecondInput) && oreSecondInput.equalsIgnoreCase(getOreName(par2))) || (!InventoryUtil.isItemEqualTotal(secondInput, par2)))
		{
			return false;
		}
		if((isValidOre(oreCombiner) && oreCombiner.equalsIgnoreCase(getOreName(par3))) || (!InventoryUtil.isItemEqualTotal(combiner, par3)))
		{
			return false;
		}
		if(input != null)
		{
			if(par1.stackSize < (input.stackSize * times))
			{
				return false;
			}
		}
		if(secondInput != null)
		{
			if(par2.stackSize < (secondInput.stackSize * times))
			{
				return false;
			}
		}
		if(combiner != null)
		{
			if(par3.stackSize < (combiner.stackSize * times))
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
	
	private boolean isValidOre(String par1)
	{
		if(Strings.isNullOrEmpty(par1))
		{
			return false;
		}
		return par1.equalsIgnoreCase("Unkowen");
	}
	
	private String getOreName(ItemStack par1)
	{
		return PathProxy.getOreName(par1);
	}
	
}
