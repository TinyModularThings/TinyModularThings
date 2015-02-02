package speiger.src.api.common.registry.recipes.pressureFurnace;

import java.util.ArrayList;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.item.ItemStack;

public class PressureRecipeList
{
	ArrayList<IPressureFurnaceRecipe> list = new ArrayList<IPressureFurnaceRecipe>();
	
	private static PressureRecipeList instance = new PressureRecipeList();
	
	public static PressureRecipeList getInstance()
	{
		return instance;
	}
	
	public void addRecipe(IPressureFurnaceRecipe par1)
	{
		list.add(par1);
	}
	
	public ArrayList<IPressureFurnaceRecipe> getRecipeList()
	{
		return list;
	}
	
	public IPressureFurnaceRecipe getRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		for (IPressureFurnaceRecipe cu : list)
		{
			if(cu.recipeMatches(input, input2, combiner, 1))
			{
				return cu;
			}
		}
		return null;
	}
	
	public boolean hasRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		return getRecipe(input, input2, combiner) != null;
	}
	
}
