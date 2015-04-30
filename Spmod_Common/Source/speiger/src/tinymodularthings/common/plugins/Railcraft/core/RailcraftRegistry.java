package speiger.src.tinymodularthings.common.plugins.Railcraft.core;

import ic2.api.item.Items;

import java.util.Arrays;

import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.blocks.anvil.BlockRCAnvil;
import mods.railcraft.common.blocks.detector.BlockDetector;
import mods.railcraft.common.items.ItemGear;
import mods.railcraft.common.items.RailcraftToolItems;
import mods.railcraft.common.items.firestone.ItemFirestoneCracked;
import mods.railcraft.common.items.firestone.ItemFirestoneRefined;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;

public class RailcraftRegistry
{
	public static void init()
	{
		loadUncraftingRecipes();
	}
	
	static void loadUncraftingRecipes()
	{

		UncrafterRecipeList un = UncrafterRecipeList.getInstance();
		try
		{
			PressureFurnace.bonusHeat.put(ItemFirestoneRefined.item.itemID, 100);
			PressureFurnace.bonusHeat.put(ItemFirestoneCracked.item.itemID, 100);
			PressureFurnace.validFuels.add(ItemFirestoneRefined.item.itemID);
			PressureFurnace.validFuels.add(ItemFirestoneCracked.item.itemID);
			ItemStack stack = RailcraftToolItems.getCoalCoke();
			if(stack != null)
			{
				PressureFurnace.validFuels.add(stack.itemID);
			}
			stack = EnumCube.COKE_BLOCK.getItem();
			if(stack != null)
			{
				PressureFurnace.validFuels.add(stack.itemID);
				PressureFurnace.fuelMeta.put(stack.itemID, Arrays.asList(stack.getItemDamage()));
			}
			
			
			ItemStack steel = InventoryUtil.getItemFromModAndOreDictWithSize("ingotSteel", "Railcraft", true, 2);
			ItemStack blockSteel = InventoryUtil.getItemFromModAndOreDictWithSize("blockSteel", "Railcraft", true, 1);
			if(steel != null && blockSteel != null)
			{
				un.addUncraftingRecipe(new ItemStack(BlockRCAnvil.getBlock(), 1, 0), new RecipeOutput(steel, 75), new RecipeOutput(blockSteel, 69), new RecipeOutput(blockSteel, 69), new RecipeOutput(blockSteel, 69));
				un.addUncraftingRecipe(new ItemStack(BlockRCAnvil.getBlock(), 1, 1), new RecipeOutput(steel, 75), new RecipeOutput(blockSteel, 69), new RecipeOutput(blockSteel, 69));
				un.addUncraftingRecipe(new ItemStack(BlockRCAnvil.getBlock(), 1, 2), new RecipeOutput(steel, 75), new RecipeOutput(blockSteel, 69));
				Block alpha = RailcraftBlocks.getBlockMachineAlpha();
				if(alpha != null)
				{
					steel.stackSize = 6;
					un.addUncraftingRecipe(new ItemStack(alpha, 1, 1), new RecipeOutput(steel, 82), new RecipeOutput(steel, 82));
					un.addUncraftingRecipe(new ItemStack(alpha, 1, 9), new RecipeOutput(Block.dispenser, 88));
					steel.stackSize = 1;
					un.addUncraftingRecipe(new ItemStack(alpha, 1, 15), new RecipeOutput(Block.pistonBase, 77), new RecipeOutput(steel, 37), new RecipeOutput(Item.diamond, 5));
				}
			}
			Block gamma = RailcraftBlocks.getBlockMachineGamma();
			Block detector = BlockDetector.getBlock();
			if(gamma != null && detector != null)
			{
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 0), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 0), 75));					
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 1), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 0), 75));
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 2), new RecipeOutput(gamma, 95));
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 3), new RecipeOutput(new ItemStack(gamma, 1, 1), 95));
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 4), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 8), 75));					
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 5), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 8), 75));	
				ItemStack machine = Items.getItem("machine");
				if(machine != null)
				{
					un.addUncraftingRecipe(new ItemStack(gamma, 1, 6), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 10), 75), new RecipeOutput(machine, 85));
					un.addUncraftingRecipe(new ItemStack(gamma, 1, 7), new RecipeOutput(Block.hopperBlock, 75), new RecipeOutput(new ItemStack(detector, 1, 10), 75), new RecipeOutput(machine, 85));
				}
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 8), new RecipeOutput(Block.dispenser, 89));
				un.addUncraftingRecipe(new ItemStack(gamma, 1, 9), new RecipeOutput(new ItemStack(gamma, 1, 8), 89));
			}
			Block beta = RailcraftBlocks.getBlockMachineBeta();
			if(beta != null)
			{
				ItemStack goldPlate = ItemGear.getGoldPlateGear();
				if(goldPlate != null) un.addUncraftingRecipe(new ItemStack(beta, 1, 7), new RecipeOutput(Block.pistonBase, 73), new RecipeOutput(goldPlate, 25), new RecipeOutput(goldPlate, 25));
				else un.addUncraftingRecipe(new ItemStack(beta, 1, 7), new RecipeOutput(Block.pistonBase, 73));
					
				ItemStack iron = ItemGear.getIronGear();
				if(iron != null) un.addUncraftingRecipe(new ItemStack(beta, 1, 8), new RecipeOutput(Block.pistonBase, 73), new RecipeOutput(iron, 25), new RecipeOutput(iron, 25));
				else un.addUncraftingRecipe(new ItemStack(beta, 1, 8), new RecipeOutput(Block.pistonBase, 73));
				
				ItemStack steelGear = ItemGear.getSteelGear();
				if(steelGear != null) un.addUncraftingRecipe(new ItemStack(beta, 1, 9), new RecipeOutput(Block.pistonBase, 73), new RecipeOutput(steelGear, 25), new RecipeOutput(steelGear, 25));
				else un.addUncraftingRecipe(new ItemStack(beta, 1, 9), new RecipeOutput(Block.pistonBase, 73));
			}
		}
		catch(Exception e)
		{
		}
	
	
	}
}
