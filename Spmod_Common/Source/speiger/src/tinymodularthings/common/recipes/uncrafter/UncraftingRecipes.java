package speiger.src.tinymodularthings.common.recipes.uncrafter;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import buildcraft.BuildCraftBuilders;
import buildcraft.BuildCraftCore;
import buildcraft.BuildCraftEnergy;
import buildcraft.BuildCraftFactory;
import buildcraft.BuildCraftSilicon;
import buildcraft.BuildCraftTransport;
import cpw.mods.fml.common.Loader;

public class UncraftingRecipes
{
	public static void initRecipes(PathProxy pp)
	{
		UncrafterRecipeList un = UncrafterRecipeList.getInstance();
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(Block.pistonBase), "XXX", "YCY", "YVY", 'X', "plankWood", 'Y', "cobblestone", 'C', "ingotBronze", 'V', Item.redstone));
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(Block.pistonBase), "XXX", "YCY", "YVY", 'X', "plankWood", 'Y', "cobblestone", 'C', "ingotSilver", 'V', Item.redstone));
		
		un.addUncraftingRecipe(new BlockData(Block.pistonBase, 0), new RecipeOutput(new ItemStack(Block.fenceIron, 2), 75), new RecipeOutput(Item.redstone, 40), new RecipeOutput(Block.planks, 70), new RecipeOutput(Block.cobblestone, 60));
		un.addUncraftingRecipe(new ItemStack(Block.dropper), new RecipeOutput(new ItemStack(Block.cobblestone, 5), 90), new RecipeOutput(Item.redstone, 25));
		un.addUncraftingRecipe(new ItemStack(Block.dispenser), new RecipeOutput(new ItemStack(Block.cobblestone, 5), 90), new RecipeOutput(Item.redstone, 25), new RecipeOutput(Item.bow, 75));
		un.addUncraftingRecipe(new ItemStack(Block.pistonStickyBase), new ItemStack(Block.pistonBase));
		un.addUncraftingRecipe(new ItemStack(Block.beacon), new RecipeOutput(new ItemStack(Block.obsidian, 2), 75), new RecipeOutput(Item.netherStar, 25), new RecipeOutput(new ItemStack(Block.thinGlass, 4), 90));
		un.addUncraftingRecipe(new ItemStack(Block.jukebox), new RecipeOutput(Item.diamond, 15), new RecipeOutput(new ItemStack(Block.planks, 5), 75));
		un.addUncraftingRecipe(new ItemStack(Item.redstoneRepeater), new RecipeOutput(Block.torchRedstoneActive), new RecipeOutput(Item.redstone, 45), new RecipeOutput(new ItemStack(Block.stoneSingleSlab, 2), 75));
		un.addUncraftingRecipe(new ItemStack(Block.anvil), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(new ItemStack(Item.ingotIron, 4), 50));
		un.addUncraftingRecipe(new ItemStack(Block.anvil, 1, 1), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(new ItemStack(Item.ingotIron, 4), 50));
		un.addUncraftingRecipe(new ItemStack(Block.anvil, 1, 2), new RecipeOutput(Block.blockIron, 75), new RecipeOutput(new ItemStack(Item.ingotIron, 4), 50));
		un.addUncraftingRecipe(new ItemStack(Block.hopperBlock), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 75), new RecipeOutput(Item.ingotIron, 50), new RecipeOutput(Item.ingotIron, 50));
		
		if(Loader.isModLoaded("BuildCraft|Core"))
		{
			un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 80));
			un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 60));
			un.addUncraftingRecipe(new ItemStack(BuildCraftEnergy.engineBlock), new RecipeOutput(Block.pistonBase, 40), new RecipeOutput(BuildCraftCore.ironGearItem, 40), new RecipeOutput(BuildCraftCore.ironGearItem, 40));
			un.addUncraftingRecipe(new ItemStack(BuildCraftBuilders.architectBlock), new RecipeOutput(BuildCraftCore.goldGearItem, 60), new RecipeOutput(Block.workbench, 95), new RecipeOutput(Block.chest, 95));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.hopperBlock), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 75), new RecipeOutput(Item.ingotIron, 50), new RecipeOutput(Item.ingotIron, 50));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.refineryBlock), new RecipeOutput(Item.diamond, 75), new RecipeOutput(new ItemStack(BuildCraftFactory.tankBlock, 2), 80), new RecipeOutput(BuildCraftFactory.tankBlock, 60));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.miningWellBlock), new RecipeOutput(BuildCraftCore.ironGearItem, 75), new RecipeOutput(Item.pickaxeIron, 50));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.pumpBlock), new RecipeOutput(BuildCraftFactory.miningWellBlock, 95), new RecipeOutput(BuildCraftFactory.tankBlock, 25));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.floodGateBlock), new RecipeOutput(BuildCraftCore.ironGearItem, 85), new RecipeOutput(BuildCraftFactory.tankBlock, 75), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 50), new RecipeOutput(new ItemStack(Item.ingotIron, 2), 50));
			un.addUncraftingRecipe(new ItemStack(BuildCraftFactory.quarryBlock), new RecipeOutput(new ItemStack(BuildCraftCore.diamondGearItem, 2), 65), new RecipeOutput(new ItemStack(BuildCraftCore.goldGearItem, 2), 75), new RecipeOutput(new ItemStack(BuildCraftCore.ironGearItem, 2), 90), new RecipeOutput(Item.pickaxeDiamond, 75));
			un.addUncraftingRecipe(new ItemStack(BuildCraftTransport.filteredBufferBlock), new RecipeOutput(Block.pistonBase, 75), new RecipeOutput(BuildCraftTransport.pipeItemsDiamond, 60), new RecipeOutput(Block.chest, 85));
			un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.laserBlock), new RecipeOutput(new ItemStack(Item.diamond, 2), 50), new RecipeOutput(new ItemStack(Item.redstone, 3), 75), new RecipeOutput(Block.obsidian, 80));
			un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.assemblyTableBlock, 1, 0), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(Item.diamond, 60), new RecipeOutput(BuildCraftCore.diamondGearItem, 40));
			un.addUncraftingRecipe(new ItemStack(BuildCraftSilicon.assemblyTableBlock, 1, 1), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(new ItemStack(Block.obsidian, 3), 50), new RecipeOutput(Block.workbench, 87), new RecipeOutput(Block.chest, 80));
		}
		
		if(Loader.isModLoaded("IC2"))
		{
			un.addUncraftingRecipe(Items.getItem("rubberTrampoline"), new RecipeOutput(PathProxy.getIC2Item("rubber", 2), 40), new RecipeOutput(PathProxy.getIC2Item("rubber", 2), 40), new RecipeOutput(PathProxy.getIC2Item("rubber", 2), 40));
			un.addUncraftingRecipe(Items.getItem("insulatedCopperCableBlock"), new RecipeOutput(Items.getItem("copperCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("insulatedGoldCableBlock"), new RecipeOutput(Items.getItem("goldCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("doubleInsulatedGoldCableBlock"), new RecipeOutput(Items.getItem("goldCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 50));
			un.addUncraftingRecipe(Items.getItem("insulatedIronCableBlock"), new RecipeOutput(Items.getItem("ironCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("doubleInsulatedIronCableBlock"), new RecipeOutput(Items.getItem("ironCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 50));
			un.addUncraftingRecipe(Items.getItem("trippleInsulatedIronCableBlock"), new RecipeOutput(Items.getItem("ironCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 75));
			un.addUncraftingRecipe(Items.getItem("insulatedtinCableBlock"), new RecipeOutput(Items.getItem("tinCableBlock"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			
		}
	}
}
