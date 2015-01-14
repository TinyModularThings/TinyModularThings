package speiger.src.api.common.registry.recipes.pressureFurnace;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class PressureRecipeList
{
	ArrayList<PressureRecipe> list = new ArrayList<PressureRecipe>();
	
	private static PressureRecipeList instance = new PressureRecipeList();
	
	public static PressureRecipeList getInstance()
	{
		return instance;
	}
	
	public void addFurnaceRecipes()
	{
		Map<List<Integer>, ItemStack> metaList = FurnaceRecipes.smelting().getMetaSmeltingList();
		Iterator<Entry<List<Integer>, ItemStack>> iter = metaList.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<List<Integer>, ItemStack> cuRecipe = iter.next();
			if (cuRecipe != null)
			{
				ItemStack input = new ItemStack(cuRecipe.getKey().get(0), 1, cuRecipe.getKey().get(1));
				PressureRecipe recipe = new PressureRecipe(input, (ItemStack) null, (ItemStack) null, cuRecipe.getValue(), false, false);
				addRecipe(recipe);
			}
		}
		
		Map list = FurnaceRecipes.smelting().getSmeltingList();
		Iterator iter2 = list.entrySet().iterator();
		while (iter2.hasNext())
		{
			Entry<Integer, ItemStack> recipe = (Entry<Integer, ItemStack>) iter2.next();
			ItemStack input = new ItemStack(recipe.getKey(), 1, -1);
			PressureRecipe recipes = new PressureRecipe(input, (ItemStack) null, (ItemStack) null, recipe.getValue(), false, false);
			addRecipe(recipes);
		}
		PressureRecipe recipes = new PressureRecipe(new ItemStack(Block.wood, 1, OreDictionary.WILDCARD_VALUE), (ItemStack) null, (ItemStack) null, new ItemStack(Item.coal, 1, 1), false, false);
		addRecipe(recipes);
	}
	
	public void addRecipe(PressureRecipe par1)
	{
		if (contains(par1))
		{
			return;
		}
		
		list.add(par1);
	}
	
	public boolean contains(PressureRecipe par1)
	{
		for (PressureRecipe cu : list)
		{
			if (cu.areEqual(par1))
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<PressureRecipe> getRecipeList()
	{
		return list;
	}
	
	public PressureRecipe getRecipeOutput(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		PressureRecipe end = null;
		
		for (PressureRecipe cu : list)
		{
			
			if (cu.firstInputEquals(input) && cu.secondInputEquals(input2) && cu.combinerEquals(combiner))
			{
				end = cu;
				break;
			}
		}
		
		return end;
	}
	
	public boolean hasRecipe(ItemStack input, ItemStack input2, ItemStack combiner)
	{
		return getRecipeOutput(input, input2, combiner) != null;
	}
	
}
