package speiger.src.api.common.registry.recipes.pressureFurnace;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.data.utils.ResultData;

public class PressureRecipeList
{
	ArrayList<IPressureFurnaceRecipe> blankList = new ArrayList<IPressureFurnaceRecipe>();
	ArrayList<PressureRecipeStorage> list = new ArrayList<PressureRecipeStorage>();
	HashMap<IStackInfo, Float> expDrops = new HashMap<IStackInfo, Float>();
	
	private static PressureRecipeList instance = new PressureRecipeList();
	
	public static PressureRecipeList getInstance()
	{
		return instance;
	}
	
	public void addRecipe(IPressureFurnaceRecipe par1)
	{
		blankList.add(par1);
		PressureRecipeStorage storage = new PressureRecipeStorage(par1, list.size());
		list.add(storage);
	}
	
	public void addRecipe(IPressureFurnaceRecipe par1, float par2)
	{
		addRecipe(par1);
		expDrops.put(new ItemData(par1.getOutput()), par2);
	}
	
	public float getExpFromResult(ItemStack par1)
	{
		if(par1 == null || par1.getItem() == null)
		{
			return 0.0F;
		}
		float value = FurnaceRecipes.smelting().getExperience(par1);
		
		if(value <= 0F)
		{
			ResultData data = new ResultData(par1);
			if(expDrops.containsKey(data))
			{
				value = expDrops.get(data);
			}
			else
			{
				return 0.0F;
			}
		}
		if(value <= 0F)
		{
			return 0F;
		}
		return value;
	}
	
	public ArrayList<IPressureFurnaceRecipe> getBlankRecipeList()
	{
		return blankList;
	}
	
	public IPressureFurnaceRecipe getBlankRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		for (IPressureFurnaceRecipe cu : blankList)
		{
			if(cu.recipeMatches(input, input2, combiner, 1))
			{
				return cu;
			}
		}
		return null;
	}
	
	public boolean hasBlankRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		return getBlankRecipe(input, input2, combiner) != null;
	}
	
	public boolean hasRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		return getRecipe(input, input2, combiner) != null;
	}
	
	public PressureRecipeStorage getRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		for (PressureRecipeStorage cu : list)
		{
			if(cu.getRecipe().recipeMatches(input, input2, combiner, 1))
			{
				return cu;
			}
		}
		return null;
	}
	
	public PressureRecipeStorage getRecipeFromID(int recipeID)
	{
		for(PressureRecipeStorage storage : list)
		{
			if(storage.getRecipeID() == recipeID)
			{
				return storage;
			}
		}
		return null;
	}
	
	public static class PressureRecipeStorage
	{
		final IPressureFurnaceRecipe par1;
		final int recipeID;
		
		public PressureRecipeStorage(IPressureFurnaceRecipe recipe, int id)
		{
			par1 = recipe;
			recipeID = id;
		}
		
		public IPressureFurnaceRecipe getRecipe()
		{
			return par1;
		}
		
		public int getRecipeID()
		{
			return recipeID;
		}
		
	}
	
}
