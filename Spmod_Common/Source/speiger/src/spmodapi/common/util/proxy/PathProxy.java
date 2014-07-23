package speiger.src.spmodapi.common.util.proxy;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.recipe.pressureFurnace.PressureRecipe;
import speiger.src.api.recipe.pressureFurnace.helper.PressureRecipeList;

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
	
	public static void addRecipe(ItemStack output, Object...input)
	{
		CraftingManager.getInstance().addRecipe(output, input);
	}
	
	public static void addSRecipe(ItemStack output, Object...input)
	{
		CraftingManager.getInstance().addShapelessRecipe(output, input);
	}
	
	public static void addNBTRecipe(ItemStack output, Object...input)
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
	
}
