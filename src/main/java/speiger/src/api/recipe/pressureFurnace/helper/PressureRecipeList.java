package speiger.src.api.recipe.pressureFurnace.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.recipe.pressureFurnace.PressureRecipe;

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
		Map<ItemStack, ItemStack> smeltingList = FurnaceRecipes.smelting().getSmeltingList();
		Iterator<Entry<ItemStack, ItemStack>> iter = smeltingList.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<ItemStack, ItemStack> cuRecipe = iter.next();
			if (cuRecipe != null)
			{
				ItemStack input = cuRecipe.getKey();
				PressureRecipe recipe = new PressureRecipe(input, (ItemStack) null, (ItemStack) null, cuRecipe.getValue(), false, false);
				addRecipe(recipe);
			}
		}
		
		Map<ItemStack, ItemStack> list = FurnaceRecipes.smelting().getSmeltingList();
		Iterator<Entry<ItemStack, ItemStack>> iter2 = list.entrySet().iterator();
		while (iter2.hasNext())
		{
			Entry<ItemStack, ItemStack> recipe = iter2.next();
			ItemStack input = new ItemStack(recipe.getKey().getItem(), 1, -1);
			PressureRecipe recipes = new PressureRecipe(input, (ItemStack) null, (ItemStack) null, recipe.getValue(), false, false);
			addRecipe(recipes);
		}
		PressureRecipe recipes = new PressureRecipe(new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE), (ItemStack) null, (ItemStack) null, new ItemStack(Items.coal, 1, 1), false, false);
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
