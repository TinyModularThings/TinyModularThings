package speiger.src.tinymodularthings.common.plugins.Forestry.core;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.tinymodularthings.common.blocks.machine.OilGenerator;
import cpw.mods.fml.common.Loader;
import forestry.api.circuits.ChipsetManager;
import forestry.api.core.ItemInterface;
import forestry.api.farming.Farmables;
import forestry.api.farming.IFarmable;
import forestry.core.circuits.Circuit;
import forestry.core.config.ForestryBlock;
import forestry.core.config.ForestryItem;

public class ForestryRegistry
{
	public static void init()
	{
		if (Loader.isModLoaded("IC2"))
		{
			loadCrops();
		}
		loadUncraftingRecipes(UncrafterRecipeList.getInstance());
		loadMachineRecipes();
	}
	
	
	static void loadMachineRecipes()
	{
		OilGenerator.allowedItems.add(new ItemData(ItemInterface.getItem("beeDroneGE")));
		OilGenerator.allowedItems.add(new ItemData(ItemInterface.getItem("beeLarvaeGE")));
		OilGenerator.allowedItems.add(new ItemData(ItemInterface.getItem("beePrincessGE")));
		OilGenerator.allowedItems.add(new ItemData(ItemInterface.getItem("beeQueenGE")));
	}
	
	
	static void loadCrops()
	{
		Collection<IFarmable> list = Farmables.farmables.get("IC2Crops");
		if(list == null)
		{
			list = new ArrayList<IFarmable>();
		}
		list.add(new IC2Crops());
		Farmables.farmables.put("IC2Crops", list);
		Circuit ic2Crops = new SpmodCircuitFarmLogic("ic2crops", FarmLogicIC2Crops.class, new String[]{"IC2 Crops"}).setManual(); 
		ItemStack stack = ForestryItem.tubes.getItemStack(1, 9);
		if(stack != null)
		{
			stack.setItemDamage(9);
			ChipsetManager.solderManager.addRecipe(ChipsetManager.circuitRegistry.getLayout("forestry.farms.manual"), stack, ic2Crops);
		}
	}
	
	static void loadUncraftingRecipes(UncrafterRecipeList un)
	{
		if(Loader.isModLoaded("Forestry"))
		{
			try
			{
				Block engine = ForestryBlock.engine;
				ItemStack basicCasing = ForestryItem.sturdyCasing.getItemStack();
				if(engine != null)
				{
					un.addUncraftingRecipe(new ItemStack(engine, 1, 0), new RecipeOutput(Block.pistonBase, 74));
					un.addUncraftingRecipe(new ItemStack(engine, 1, 1), new RecipeOutput(Block.pistonBase, 74));
					un.addUncraftingRecipe(new ItemStack(engine, 1, 2), new RecipeOutput(Block.pistonBase, 74));
					un.addUncraftingRecipe(new ItemStack(engine, 1, 4), new RecipeOutput(Block.pistonBase, 74), new RecipeOutput(Item.pocketSundial, 25));
					if(basicCasing != null)
					{
						un.addUncraftingRecipe(new ItemStack(engine, 1, 3), new RecipeOutput(basicCasing, 81));
					}
				}
				Block machine = ForestryBlock.factoryTESR;
				if(machine != null && basicCasing != null)
				{
					un.addUncraftingRecipe(new ItemStack(machine, 1, 0), new RecipeOutput(basicCasing, 91));
					un.addUncraftingRecipe(new ItemStack(machine, 1, 1), new RecipeOutput(basicCasing, 91));
					un.addUncraftingRecipe(new ItemStack(machine, 1, 2), new RecipeOutput(basicCasing, 91));
					ItemStack gear = ForestryItem.gearBronze.getItemStack();
					if(gear != null)
					{
						un.addUncraftingRecipe(new ItemStack(machine, 1, 3), new RecipeOutput(basicCasing, 91), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34));
					}
					gear = ForestryItem.gearCopper.getItemStack();
					if(gear != null)
					{
						un.addUncraftingRecipe(new ItemStack(machine, 1, 4), new RecipeOutput(basicCasing, 91), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34));
					}
					ItemStack hardCasing = ForestryItem.hardenedCasing.getItemStack();
					gear = ForestryItem.gearTin.getItemStack();
					if(hardCasing != null && gear != null)
					{
						un.addUncraftingRecipe(new ItemStack(machine, 1, 7), new RecipeOutput(hardCasing, 91), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34), new RecipeOutput(gear, 34));
					}
					un.addUncraftingRecipe(new ItemStack(machine, 1, 5), new RecipeOutput(basicCasing, 91));
					un.addUncraftingRecipe(new ItemStack(machine, 1, 6), new RecipeOutput(basicCasing, 91));
				}
				Block utils = ForestryBlock.factoryPlain;
				if(utils != null && basicCasing != null)
				{
					un.addUncraftingRecipe(new ItemStack(utils, 1, 0), new RecipeOutput(basicCasing, 91), new RecipeOutput(Block.chest, 95));
					un.addUncraftingRecipe(new ItemStack(utils, 1, 1), new RecipeOutput(basicCasing, 91));
				}
			}
			catch(Exception e)
			{
			}
		}
	}
}
