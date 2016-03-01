package speiger.src.api.common.recipes.squezingCompressor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidTank;
import speiger.src.api.common.recipes.squezingCompressor.parts.INormalRecipe;
import speiger.src.api.common.recipes.util.Result;

public class MachineRecipeMaker
{
	public static MachineRecipeMaker INSTANCE = new MachineRecipeMaker();
	
	public List<INormalRecipe> compressorRecipe = new ArrayList<INormalRecipe>();
	public List<INormalRecipe> squeezerRecipe = new ArrayList<INormalRecipe>();
	public List<INormalRecipe> mixingRecipe = new ArrayList<INormalRecipe>();
	
	public void addRecipe(INormalRecipe par1)
	{
		if(par1 == null)
		{
			return;
		}
		List<INormalRecipe> list = getRecipeListFromType(par1.getRecipeType());
		if(list != null)
		{
			list.add(par1);
			updateRecipes(par1.getRecipeType());
		}
	}
	
	public boolean hasRecipe(EnumRecipeType par1, ItemStack input, FluidTank firstTank, FluidTank secondTank, World world)
	{
		return getRecipe(par1, input, firstTank, secondTank, world) != null;
	}
	
	public INormalRecipe getRecipe(EnumRecipeType par1, ItemStack input, FluidTank firstTank, FluidTank secondTank, World world)
	{
		List<INormalRecipe> list = getRecipeListFromType(par1);
		if(list != null && list.size() > 0)
		{
			for(int i = 0;i<list.size();i++)
			{
				INormalRecipe recipe = list.get(i);
				if(recipe != null && recipe.matches(input, firstTank, secondTank, world))
				{
					return recipe;
				}
			}
		}
		return null;
	}
	
	public List<INormalRecipe> getRecipeListFromType(EnumRecipeType par1)
	{
		switch(par1)
		{
			case Compressor:
				return compressorRecipe;
			case Mixing:
				return mixingRecipe;
			case Sqeezing:
				return squeezerRecipe;
		}
		return null;
	}
	
	public boolean isValidInputItem(EnumRecipeType type, ItemStack item)
	{
		if(item == null)
		{
			return false;
		}
		List<INormalRecipe> list = getRecipeListFromType(type);
		for(int i = 0;i<list.size();i++)
		{
			INormalRecipe recipe = list.get(i);
			if(recipe != null)
			{
				ItemStack input = recipe.getItemInput();
				if(input != null && item.isItemEqual(input))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public List<INormalRecipe> getRecipesFromInput(ItemStack item)
	{
		if(item == null)
		{
			return new ArrayList<INormalRecipe>();
		}
		List<INormalRecipe> list = new ArrayList<INormalRecipe>();
		for(EnumRecipeType type : EnumRecipeType.values())
		{
			list.addAll(getRecipeFromItem(type, item, true));
		}
		return list;
	}
	
	public List<INormalRecipe> getRecipesFromOutput(ItemStack item)
	{
		if(item == null)
		{
			return new ArrayList<INormalRecipe>();
		}
		List<INormalRecipe> list = new ArrayList<INormalRecipe>();
		for(EnumRecipeType type : EnumRecipeType.values())
		{
			list.addAll(getRecipeFromItem(type, item, false));
		}
		return list;
	}
	
	public List<INormalRecipe> getRecipeFromItem(EnumRecipeType type, ItemStack item, boolean input)
	{
		if(item == null)
		{
			return new ArrayList<INormalRecipe>();
		}
		List<INormalRecipe> list = new ArrayList<INormalRecipe>();
		if(getRecipeListFromType(type) != null)
		{
			for(INormalRecipe recipe : getRecipeListFromType(type))
			{
				ItemStack stack = recipe.getItemInput();
				if(input && stack != null && stack.isItemEqual(item))
				{
					list.add(recipe);
					continue;
				}
				Result[] results = recipe.getResults();
				if(!input && results != null && results.length > 0)
				{
					for(Result result : results)
					{
						if(result.hasItemResult())
						{
							ItemStack resultItem = result.getItemResult();
							if(resultItem != null && resultItem.isItemEqual(item))
							{
								list.add(recipe);
								break;
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	public List<INormalRecipe> getAllRecipes()
	{
		List<INormalRecipe> recipe = new ArrayList<INormalRecipe>();
		recipe.addAll(compressorRecipe);
		recipe.addAll(squeezerRecipe);
		recipe.addAll(mixingRecipe);
		return recipe;
	}
	
	private void updateRecipes(EnumRecipeType par1)
	{
	    Collections.sort(getRecipeListFromType(par1), new MachineRecipeSorter());
	}
	
	static class MachineRecipeSorter implements Comparator
	{
		
		@Override
		public int compare(Object arg0, Object arg1)
		{
			return sortEverything((INormalRecipe)arg0, (INormalRecipe)arg1);
		}
		
		public int sortEverything(INormalRecipe par0, INormalRecipe par1)
		{
			if(par0.getComplexity().getComplexity() > par1.getComplexity().getComplexity())
			{
				return 1;
			}
			if(par0.getComplexity().getComplexity() < par1.getComplexity().getComplexity())
			{
				return -1;
			}
			return 0;
		}
		
	}
}
