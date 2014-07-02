package speiger.src.api.recipe.pressureFurnace;

import net.minecraft.item.ItemStack;
import speiger.src.api.util.InventoryUtil;

public class PressureRecipe
{
	// First input
	ItemStack firstInput;
	
	// Second Input
	ItemStack secondInput;
	
	// Binder
	ItemStack combiner;
	
	// Output
	ItemStack output;
	
	// StackSizeSensitive
	boolean stacksize;
	
	// Combiner getUsed
	boolean useCombiner;
	
	public PressureRecipe(ItemStack par1, ItemStack par2, ItemStack par3, ItemStack par4, boolean par5, boolean par6)
	{
		firstInput = par1;
		secondInput = par2;
		combiner = par3;
		output = par4;
		stacksize = par5;
		useCombiner = par6;
	}
	
	public boolean hasFirstInput()
	{
		return firstInput != null;
	}
	
	public boolean hasSecondInput()
	{
		return secondInput != null;
	}
	
	public boolean isStackSizeSensitive()
	{
		return stacksize;
	}
	
	public boolean hasInput()
	{
		return firstInput != null || secondInput != null;
	}
	
	public boolean hasTwoInputs()
	{
		return firstInput != null && secondInput != null && !firstInput.isItemEqual(secondInput);
	}
	
	public boolean hasCombiner()
	{
		return combiner != null;
	}
	
	public boolean hasOutput()
	{
		return output != null;
	}
	
	public boolean useCombiner()
	{
		return useCombiner;
	}
	
	public ItemStack getFirstInput()
	{
		return firstInput;
	}
	
	public ItemStack getSecondInput()
	{
		return secondInput;
	}
	
	public ItemStack getCombiner()
	{
		return combiner;
	}
	
	public ItemStack getOutput()
	{
		return output;
	}
	
	public boolean firstInputEquals(ItemStack input)
	{
		if (input == null && firstInput == null)
		{
			return true;
		}
		if (input != null && firstInput != null && (InventoryUtil.isItemEqual(input, firstInput) || input.isItemEqual(firstInput)))
		{
			return true;
		}
		return false;
	}
	
	public boolean secondInputEquals(ItemStack input)
	{
		if (input == null && secondInput == null)
		{
			return true;
		}
		if (input != null && secondInput != null && (InventoryUtil.isItemEqual(input, secondInput) || input.isItemEqual(secondInput)))
		{
			return true;
		}
		return false;
	}
	
	public boolean areEqual(PressureRecipe par1)
	{
		if ((firstInput != null && par1.firstInput != null && par1.firstInputEquals(firstInput)) || (par1.firstInput == null && firstInput == null))
		{
			if ((secondInput != null && par1.secondInput != null && par1.firstInputEquals(secondInput)) || (par1.secondInput == null && secondInput == null))
			{
				if ((combiner != null && par1.combiner != null && par1.firstInputEquals(combiner)) || (par1.combiner == null && combiner == null))
				{
					if ((output != null && par1.output != null && par1.firstInputEquals(output)))
					{
						if (stacksize == par1.stacksize && useCombiner == par1.useCombiner)
						{
							return true;
						}
					}
					
				}
			}
		}
		
		return false;
	}
	
	public boolean combinerEquals(ItemStack input)
	{
		if (input == null && combiner == null)
		{
			return true;
		}
		if (input != null && combiner != null && (InventoryUtil.isItemEqual(input, combiner) || input.isItemEqual(combiner)))
		{
			return true;
		}
		return false;
	}
	
	public ItemStack consumeInput(ItemStack input)
	{
		if (input == null)
		{
			return null;
		}
		
		ItemStack end = input.copy();
		end.stackSize -= firstInput.stackSize;
		if (end.stackSize <= 0 && end.getItem().getContainerItemStack(end) != null)
		{
			end = end.getItem().getContainerItemStack(end);
		}
		
		if (end.stackSize <= 0)
		{
			return null;
		}
		return end.copy();
		
	}
	
	public ItemStack consumeSecondInput(ItemStack input)
	{
		if (input == null)
		{
			return null;
		}
		
		ItemStack end = input.copy();
		end.stackSize -= secondInput.stackSize;
		
		if (end.stackSize <= 0 && end.getItem().getContainerItemStack(end) != null)
		{
			end = end.getItem().getContainerItemStack(end);
		}
		
		if (end.stackSize <= 0)
		{
			return null;
		}
		return end.copy();
	}
	
	public ItemStack consumeCombiner(ItemStack input)
	{
		
		if (input == null)
		{
			return null;
		}
		
		ItemStack end = input.copy();
		end.stackSize -= combiner.stackSize;
		
		if (end.stackSize <= 0 && end.getItem().getContainerItemStack(end) != null)
		{
			end = end.getItem().getContainerItemStack(end);
		}
		
		if (end.stackSize <= 0)
		{
			return null;
		}
		return end.copy();
	}
	
	public ItemStack consumeItem(ItemStack input, int size)
	{
		if (input == null)
		{
			return null;
		}
		ItemStack end = input.copy();
		
		end.stackSize -= size;
		if (end.stackSize <= 0 && end.getItem().getContainerItemStack(end) != null)
		{
			end = end.getItem().getContainerItemStack(end);
		}
		
		if (end.stackSize <= 0)
		{
			return null;
		}
		return end.copy();
		
	}
	
}