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
	ArrayList<IPressureFurnaceRecipe> list = new ArrayList<IPressureFurnaceRecipe>();
	HashMap<IStackInfo, Float> expDrops = new HashMap<IStackInfo, Float>();
	
	private static PressureRecipeList instance = new PressureRecipeList();
	
	public static PressureRecipeList getInstance()
	{
		return instance;
	}
	
	public void addRecipe(IPressureFurnaceRecipe par1)
	{
		list.add(par1);
	}
	
	public void addRecipe(IPressureFurnaceRecipe par1, float par2)
	{
		list.add(par1);
		expDrops.put(new ItemData(par1.getOutput()), par2);
	}
	
	public float getExpFromResult(ItemStack par1)
	{
		float value = FurnaceRecipes.smelting().getExperience(par1);
		if(value <= 0F)
		{
			value = expDrops.get(new ResultData(par1));
		}
		if(value <= 0F)
		{
			return 0F;
		}
		return value;
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
