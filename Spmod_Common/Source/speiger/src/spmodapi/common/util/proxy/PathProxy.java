package speiger.src.spmodapi.common.util.proxy;

import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipe;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import buildcraft.api.recipes.AssemblyRecipe;
import cpw.mods.fml.common.FMLCommonHandler;

public class PathProxy
{
	public static void addFurnaceRecipe(ItemStack input, ItemStack output, float exp)
	{
		FurnaceRecipes.smelting().addSmelting(input.itemID, input.getItemDamage(), output, exp);
	}
	
	public static void addPressureRecipes(ItemStack input, ItemStack input2, ItemStack combiner, ItemStack output, boolean stacksize, boolean useCombiner)
	{
		PressureRecipeList.getInstance().addRecipe(new PressureRecipe(input, input2, combiner, output, stacksize, useCombiner));
	}
	
	public static int getRecipeBlankValue()
	{
		return OreDictionary.WILDCARD_VALUE;
	}
	
	public static void addRecipe(IRecipe par1)
	{
		CraftingManager.getInstance().getRecipeList().add(par1);
	}
	
	public static void addRecipe(ItemStack output, Object... input)
	{
		CraftingManager.getInstance().addRecipe(output, input);
	}
	
	public static void addSRecipe(ItemStack output, Object... input)
	{
		CraftingManager.getInstance().addShapelessRecipe(output, input);
	}
	
	public static void addNBTRecipe(ItemStack output, Object... input)
	{
		ShapedRecipes recipe = CraftingManager.getInstance().addRecipe(output, input);
		CraftingManager.getInstance().getRecipeList().remove(recipe);
		CraftingManager.getInstance().getRecipeList().add(recipe.func_92100_c());
	}
	
	public static void addFurnaceRecipe(ItemStack input, ItemStack output)
	{
		addFurnaceRecipe(input, output, 0F);
	}
	
	public static FluidContainerData[] getDataFromFluid(Fluid par1)
	{
		ArrayList<FluidContainerData> result = new ArrayList<FluidContainerData>();
		FluidContainerData[] fluids = FluidContainerRegistry.getRegisteredFluidContainerData();
		for(FluidContainerData data : fluids)
		{
			if(data.fluid.fluidID == par1.getID())
			{
				result.add(data);
			}
		}
		return result.toArray(new FluidContainerData[result.size()]);
	}
	
	public static ItemStack[] getFluidContainerItems(Fluid par1)
	{
		ArrayList<ItemStack> stack = new ArrayList<ItemStack>();
		FluidContainerData[] fluids = FluidContainerRegistry.getRegisteredFluidContainerData();
		for(FluidContainerData data : fluids)
		{
			if(data.fluid.fluidID == par1.getID())
			{
				stack.add(data.filledContainer);
			}
		}
		return stack.toArray(new ItemStack[stack.size()]);
	}
	
	public static void removeRecipeS(ItemStack input, Item output)
	{
		List<IRecipe> crafting = CraftingManager.getInstance().getRecipeList();
		for(int i = 0;i < crafting.size();i++)
		{
			IRecipe recipe = crafting.get(i);
			if(recipe != null)
			{
				if(recipe instanceof ShapelessOreRecipe)
				{
					ShapelessOreRecipe ore = (ShapelessOreRecipe)recipe;
					if(output.itemID == ore.getRecipeOutput().itemID)
					{
						ArrayList list = (ArrayList)ore.getInput().get(0);
						ArrayList toRemove = new ArrayList();
						for(int z = 0;z < list.size();z++)
						{
							if(input.isItemEqual((ItemStack)list.get(z)))
							{
								toRemove.add(list.get(z));
							}
						}
						list.removeAll(toRemove);
						break;
					}
				}
			}
		}
	}
	
	public static void removeRecipe(ItemStack resultItem)
	{
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for(int i = 0;i < recipes.size();i++)
		{
			IRecipe tmpRecipe = recipes.get(i);
			ItemStack recipeResult = tmpRecipe.getRecipeOutput();
			if(recipeResult != null)
			{
				if(resultItem.itemID == recipeResult.itemID && resultItem.getItemDamage() == recipeResult.getItemDamage())
				{
					if(recipes.remove(recipeResult))
					{
						return;
					}
				}
			}
		}
	}
	
	public static void addAssemblyRecipe(ItemStack output, int MJCost, ItemStack... input)
	{
		AssemblyRecipe recipe = new AssemblyRecipe(input, MJCost, output);
		recipe.assemblyRecipes.add(recipe);
	}
	
	public static void addAssemblyRecipe(ItemStack output, int MJCost, Object... input)
	{
		AssemblyRecipe recipe = new AssemblyRecipe(MJCost, output, input);
		recipe.assemblyRecipes.add(recipe);
	}
	
	public static ItemStack getIC2Item(String name, int qty)
	{
		ItemStack result = Items.getItem(name);
		result.stackSize = qty;
		return result;
	}
	
	public static boolean isSingelPlayer()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance() != null;
	}
	
}
