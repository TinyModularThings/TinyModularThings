package speiger.src.tinymodularthings.common.recipes.uncrafter;

import forestry.core.config.ForestryBlock;
import forestry.core.config.ForestryItem;
import ic2.api.item.Items;

import java.util.Arrays;
import java.util.List;

import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.blocks.anvil.BlockRCAnvil;
import mods.railcraft.common.blocks.detector.BlockDetector;
import mods.railcraft.common.items.ItemGear;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.registry.recipes.output.RandomOutput;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.output.StackOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.util.data.ClassStorage;
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
		ClassStorage storage = ClassStorage.getInstance();
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
			un.addUncraftingRecipe(Items.getItem("insulatedCopperCableItem"), new RecipeOutput(Items.getItem("copperCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("insulatedGoldCableItem"), new RecipeOutput(Items.getItem("goldCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("doubleInsulatedGoldCableItem"), new RecipeOutput(Items.getItem("goldCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 50));
			un.addUncraftingRecipe(Items.getItem("insulatedIronCableItem"), new RecipeOutput(Items.getItem("ironCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("doubleInsulatedIronCableItem"), new RecipeOutput(Items.getItem("ironCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 50));
			un.addUncraftingRecipe(Items.getItem("trippleInsulatedIronCableItem"), new RecipeOutput(Items.getItem("ironCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 75));
			un.addUncraftingRecipe(Items.getItem("insulatedTinCableItem"), new RecipeOutput(Items.getItem("tinCableItem"), 95), new RecipeOutput(Items.getItem("rubber"), 25));
			un.addUncraftingRecipe(Items.getItem("generator"), new RecipeOutput(Items.getItem("machine"), 85), new RecipeOutput(Items.getItem("reBattery"), 75));
			un.addUncraftingRecipe(Items.getItem("geothermalGenerator"), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 95), new RecipeOutput(Block.glass, 80), new RecipeOutput(PathProxy.getIC2Item("cell", 2), 75));
			un.addUncraftingRecipe(Items.getItem("waterMill"), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 95));
			un.addUncraftingRecipe(Items.getItem("solarPanel"), new RecipeOutput(Items.getItem("electronicCircuit"), 50), new RecipeOutput(Items.getItem("electronicCircuit"), 50), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 95));
			un.addUncraftingRecipe(Items.getItem("windMill"), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 95), new RecipeOutput(Item.ingotIron, 60), new RecipeOutput(Item.ingotIron, 60));
			un.addUncraftingRecipe(Items.getItem("nuclearReactor"), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 95), new RecipeOutput(Items.getItem("plateDenseLead"), 50), new RecipeOutput(PathProxy.getIC2Item("denseplatelead", 2), 25), new RecipeOutput(Items.getItem("advancedCircuit"), 40), new RecipeOutput(Items.getItem("reactorChamber"), 65), new RecipeOutput(Items.getItem("reactorChamber"), 65));
			un.addUncraftingRecipe(Items.getItem("reactorChamber"), new RecipeOutput(Items.getItem("machine"), 49));
			un.addUncraftingRecipe(Items.getItem("RTGenerator"), new RecipeOutput(Items.getItem("reactorChamber"), 65), new RandomOutput(Items.getItem("generator"), Items.getItem("machine"), 80, 75));
			un.addUncraftingRecipe(Items.getItem("semifluidGenerator"), new RecipeOutput(Items.getItem("geothermalGenerator"), 95));
			un.addUncraftingRecipe(Items.getItem("batBox"), new RecipeOutput(Items.getItem("reBattery"), 65), new RecipeOutput(Items.getItem("reBattery"), 65), new RecipeOutput(Items.getItem("reBattery"), 65));
			un.addUncraftingRecipe(Items.getItem("cesuUnit"), new RecipeOutput(Items.getItem("advBattery"), 65), new RecipeOutput(Items.getItem("advBattery"), 65), new RecipeOutput(Items.getItem("advBattery"), 65));
			un.addUncraftingRecipe(Items.getItem("mfeUnit"), new RecipeOutput(Items.getItem("machine"), 65), new RecipeOutput(Items.getItem("energyCrystal"), 45), new RecipeOutput(Items.getItem("energyCrystal"), 45), new RecipeOutput(Items.getItem("energyCrystal"), 45), new RecipeOutput(Items.getItem("energyCrystal"), 45));
			un.addUncraftingRecipe(Items.getItem("mfsUnit"), new RandomOutput(Items.getItem("mfeUnit"), Items.getItem("machine"), 75, 80), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 75, 85), new RecipeOutput(PathProxy.getIC2Item("lapotronCrystal", 3), 50), new RecipeOutput(PathProxy.getIC2Item("lapotronCrystal", 3), 50), new RecipeOutput(Items.getItem("advancedCircuit")));
			un.addUncraftingRecipe(Items.getItem("lvTransformer"), new RecipeOutput(Items.getItem("coil"), 80));
			un.addUncraftingRecipe(Items.getItem("mvTransformer"), new RecipeOutput(Items.getItem("machine"), 85));
			un.addUncraftingRecipe(Items.getItem("hvTransformer"), new RandomOutput(Items.getItem("mvTransformer"), Items.getItem("machine"), 75, 87), new RecipeOutput(Items.getItem("advBattery"), 83), new RecipeOutput(Items.getItem("electronicCircuit")));
			un.addUncraftingRecipe(Items.getItem("evTransformer"), new RecipeOutput(Items.getItem("hvTransformer"), 75), new RecipeOutput(Items.getItem("advancedCircuit"), 65), new RecipeOutput(Items.getItem("lapotronCrystal"), 55));
			un.addUncraftingRecipe(Items.getItem("electroFurnace"), new RecipeOutput(Items.getItem("ironFurnace"), 95), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("advancedMachine"), new RecipeOutput(Items.getItem("machine")));
			un.addUncraftingRecipe(Items.getItem("macerator"), new RecipeOutput(Items.getItem("machine"), 87), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("extractor"), new RecipeOutput(Items.getItem("machine"), 87), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("compressor"), new RecipeOutput(Items.getItem("machine"), 87), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("canner"), new RecipeOutput(Items.getItem("machine"), 87), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("miner"), new RecipeOutput(Items.getItem("machine"), 87), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RecipeOutput(Items.getItem("miningPipe")));
			un.addUncraftingRecipe(Items.getItem("pump"), new RecipeOutput(Items.getItem("machine"), 89), new RecipeOutput(Items.getItem("electronicCircuit"), 75), new RecipeOutput(PathProxy.getIC2Item("miningPipe", 2), 85));
			un.addUncraftingRecipe(Items.getItem("magnetizer"), new RecipeOutput(Items.getItem("machine"), 88), new RecipeOutput(Items.getItem("miningPipe"), 70), new RecipeOutput(Items.getItem("miningPipe"), 70));
			un.addUncraftingRecipe(Items.getItem("electrolyzer"), new RecipeOutput(Items.getItem("machine"), 90), new RecipeOutput(Items.getItem("electronicCircuit"), 50), new RecipeOutput(PathProxy.getIC2Item("insulatedCopperCableItem", 2), 50));
			un.addUncraftingRecipe(Items.getItem("recycler"), new RandomOutput(Items.getItem("compressor"), Items.getItem("machine"), 75, 85));
			un.addUncraftingRecipe(Items.getItem("inductionFurnace"), new RandomOutput(Items.getItem("electroFurnace"), Items.getItem("machine"), 85, 85), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 65, 85));
			un.addUncraftingRecipe(Items.getItem("massFabricator"), new RecipeOutput(Items.getItem("lapotronCrystal"), 60), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 67, 89), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 67, 89), new RecipeOutput(Items.getItem("advancedCircuit"), 62), new RecipeOutput(Items.getItem("advancedCircuit"), 62));
			un.addUncraftingRecipe(Items.getItem("terraformer"), new RecipeOutput(Items.getItem("terraformerBlueprint"), 99), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 76, 80));
			un.addUncraftingRecipe(Items.getItem("teleporter"), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 76, 80), new RecipeOutput(PathProxy.getIC2Item("advancedCircuit", 2), 20), new RecipeOutput(PathProxy.getIC2Item("advancedCircuit", 2), 20));
			un.addUncraftingRecipe(Items.getItem("teslaCoil"), new RecipeOutput(Items.getItem("electronicCircuit")), new RandomOutput(Items.getItem("mvTransformer"), Items.getItem("machine"), 78, 87));
			un.addUncraftingRecipe(Items.getItem("cropmatron"), new RecipeOutput(Block.chest), new RecipeOutput(Items.getItem("electronicCircuit"), 80), new RecipeOutput(Items.getItem("machine"), 79));
			un.addUncraftingRecipe(Items.getItem("centrifuge"), new RecipeOutput(Items.getItem("coil"), 60), new RecipeOutput(Items.getItem("coil"), 60), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 67, 76), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80));
			un.addUncraftingRecipe(Items.getItem("metalformer"), new RecipeOutput(Items.getItem("coil"), 50), new RecipeOutput(Items.getItem("coil"), 55), new RecipeOutput(Items.getItem("coil"), 50), new RecipeOutput(Items.getItem("machine"), 80));
			un.addUncraftingRecipe(Items.getItem("orewashingplant"), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RecipeOutput(Items.getItem("machine"), 75), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80));
			un.addUncraftingRecipe(Items.getItem("patternstorage"), new RecipeOutput(Items.getItem("advancedCircuit"), 40), new RecipeOutput(Items.getItem("crystalmemory"), 35), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 75, 80));
			un.addUncraftingRecipe(Items.getItem("scanner"), new RecipeOutput(Items.getItem("advancedCircuit"), 40), new RecipeOutput(Items.getItem("advancedCircuit"), 40), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 75, 80), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80));
			un.addUncraftingRecipe(Items.getItem("replicator"), new RandomOutput(Items.getItem("teleporter"), Items.getItem("advancedMachine"), 50, 80), new RandomOutput(Items.getItem("teleporter"), Items.getItem("advancedMachine"), 50, 80), new RandomOutput(Items.getItem("teleporter"), Items.getItem("advancedMachine"), 50, 80), new RandomOutput(Items.getItem("mfeUnit"), Items.getItem("machine"), 90, 80), new RandomOutput(Items.getItem("hvTransformer"), Items.getItem("machine"), 75, 87), new RandomOutput(Items.getItem("hvTransformer"), Items.getItem("machine"), 75, 87));
			un.addUncraftingRecipe(Items.getItem("solidcanner"), new RecipeOutput(Items.getItem("machine"), 85), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("fluidbottler"), new RecipeOutput(Items.getItem("machine"), 85), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RecipeOutput(Items.getItem("electronicCircuit"), 65));
			un.addUncraftingRecipe(Items.getItem("advminer"), new RandomOutput(Items.getItem("advancedMachine"), Items.getItem("machine"), 65, 85), new RandomOutput(Items.getItem("teleporter"), Items.getItem("advancedMachine"), 62, 80), new RandomOutput(Items.getItem("miner"), Items.getItem("machine"), 75, 85), new RandomOutput(Items.getItem("miner"), Items.getItem("machine"), 75, 85), new RandomOutput(Items.getItem("mfeUnit"), Items.getItem("machine"), 65, 78));
			un.addUncraftingRecipe(Items.getItem("personalSafe"), new RecipeOutput(Block.chest), new RecipeOutput(Items.getItem("machine"), 95), new RecipeOutput(Items.getItem("electronicCircuit"), 25));
			un.addUncraftingRecipe(Items.getItem("tradeOMat"), new RecipeOutput(Block.chest), new RecipeOutput(Block.chest, 50), new RecipeOutput(Items.getItem("machine"), 95));
			un.addUncraftingRecipe(Items.getItem("energyOMat"), new RecipeOutput(PathProxy.getIC2Item("insulatedCopperCableItem", 2), 89), new RecipeOutput(Items.getItem("machine"), 77));
			un.addUncraftingRecipe(Items.getItem("elemotor"), new RecipeOutput(Items.getItem("coil"), 75), new RecipeOutput(Items.getItem("coil"), 75));
			un.addUncraftingRecipe(Items.getItem("powerunitsmall"), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80));
			un.addUncraftingRecipe(Items.getItem("powerunit"), new RecipeOutput(Items.getItem("electronicCircuit"), 65), new RandomOutput(Items.getItem("elemotor"), PathProxy.getIC2Item("coil", 2), 87, 80));
			un.addUncraftingRecipe(Items.getItem("electronicCircuit"), new RecipeOutput(Items.getItem("plateiron"), 65), new RandomOutput(PathProxy.getIC2Item("insulatedCopperCableItem", 3), PathProxy.getIC2Item("copperCableItem", 3), 75, 85), new RandomOutput(PathProxy.getIC2Item("insulatedCopperCableItem", 3), PathProxy.getIC2Item("copperCableItem", 3), 75, 85));
			un.addUncraftingRecipe(Items.getItem("advancedCircuit"), new RecipeOutput(Items.getItem("electronicCircuit"), 89));
			un.addUncraftingRecipe(Items.getItem("miningDrill"), new RecipeOutput(Items.getItem("powerunit"), 99));
			un.addUncraftingRecipe(Items.getItem("diamondDrill"), new RecipeOutput(Items.getItem("powerunit"), 99));
			un.addUncraftingRecipe(Items.getItem("iridiumDrill"), new RecipeOutput(Items.getItem("powerunit"), 99), new RecipeOutput(Items.getItem("energyCrystal"), 66));
			un.addUncraftingRecipe(Items.getItem("electricWrench"), new RecipeOutput(Items.getItem("powerunitsmall"), 97), new RecipeOutput(Items.getItem("wrench"), 25));
			un.addUncraftingRecipe(Items.getItem("electricTreetap"), new RecipeOutput(Items.getItem("powerunitsmall"), 97));
			un.addUncraftingRecipe(Items.getItem("electricHoe"), new RecipeOutput(Items.getItem("powerunitsmall"), 98));
			un.addUncraftingRecipe(Items.getItem("chainsaw"), new RecipeOutput(Items.getItem("powerunit"), 96));
			un.addUncraftingRecipe(Items.getItem("miningLaser"), new RecipeOutput(Items.getItem("energyCrystal"), 51), new RecipeOutput(Items.getItem("energyCrystal"), 49));
			un.addUncraftingRecipe(Items.getItem("odScanner"), new RandomOutput(PathProxy.getIC2Item("insulatedCopperCableItem", 3), PathProxy.getIC2Item("copperCableItem", 3), 75, 85), new RecipeOutput(Items.getItem("electronicCircuit")));
			un.addUncraftingRecipe(Items.getItem("obscurator"), new RecipeOutput(Items.getItem("advancedCircuit"), 75), new RecipeOutput(Items.getItem("advBattery"), 65), new RecipeOutput(PathProxy.getIC2Item("doubleInsulatedGoldCableItem", 2), 25));
			un.addUncraftingRecipe(Items.getItem("nanoSaber"), new RecipeOutput(Items.getItem("energyCrystal"), 55));
			un.addUncraftingRecipe(Items.getItem("solarHelmet"), new RecipeOutput(Items.getItem("solarPanel"), 95));
		}
		
		if(Loader.isModLoaded("Railcraft"))
		{
			try
			{
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
	
		
		if(Loader.isModLoaded("ThermalExpansion"))
		{
			try
			{
				List<String> ids = Arrays.asList("furnace", "macerator", "sawmill", "smelter", "crucible", "transposer", "iceGen", "rockGen", "waterGen", "crafter", "charging", "frame");
				List<String> fields = Arrays.asList("furnace", "pulverizer", "sawmill", "smelter", "crucible", "transposer", "iceGen", "rockGen", "waterGen", "assembler", "charger", "machineFrame");
				storage.requestItem(Class.forName("thermalexpansion.block.machine.BlockMachine"), ids, fields);
				
				ids = Arrays.asList("ItemClicker", "BlockBreaker", "ItemsRemover");
				fields = Arrays.asList("activator", "breaker", "nullifier");
				storage.requestItem(Class.forName("thermalexpansion.block.device.BlockDevice"), ids, fields);
			
				ids = Arrays.asList("PowerSteam", "PowerMagma", "PowerFuel", "PowerReactor");
				fields = Arrays.asList("dynamoSteam", "dynamoMagmatic", "dynamoCompression", "dynamoReactant");

				ids = Arrays.asList("Servo", "CoilGold", "CoilSilver", "CoilElectrum");
				fields = Arrays.asList("pneumaticServo", "powerCoilGold", "powerCoilSilver", "powerCoilElectrum");
				storage.requestItem(Class.forName("thermalexpansion.item.TEItems"), ids, fields);
				storage.requestItem("basicCellFrame", Class.forName("thermalexpansion.block.energycell.BlockEnergyCell"), "cellBasicFrame");
			}
			catch(Exception e)
			{
			}
			
			un.addUncraftingRecipe(storage.getStack("ItemClicker"), new RecipeOutput(Block.chest, 95), new RecipeOutput(Block.pistonBase, 75), new StackOutput(storage.getStack("Servo"), 50));
			un.addUncraftingRecipe(storage.getStack("BlockBreaker"), new RecipeOutput(Block.chest, 95), new StackOutput(storage.getStack("Servo"), 50));
			un.addUncraftingRecipe(storage.getStack("ItemsRemover"), new RecipeOutput(Block.chest, 95), new StackOutput(storage.getStack("Servo"), 50));
			un.addUncraftingRecipe(storage.getStack("PowerSteam"), new StackOutput(storage.getStack("CoilSilver"), 71));
			un.addUncraftingRecipe(storage.getStack("PowerMagma"), new StackOutput(storage.getStack("CoilSilver"), 71));
			un.addUncraftingRecipe(storage.getStack("PowerFuel"), new StackOutput(storage.getStack("CoilSilver"), 71));
			un.addUncraftingRecipe(storage.getStack("PowerReactor"), new StackOutput(storage.getStack("CoilSilver"), 71));
			un.addUncraftingRecipe(storage.getStack("furnace"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68));
			un.addUncraftingRecipe(storage.getStack("macerator"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68), new RecipeOutput(Block.pistonBase, 85));
			un.addUncraftingRecipe(storage.getStack("sawmill"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68));
			un.addUncraftingRecipe(storage.getStack("smelter"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68));
			un.addUncraftingRecipe(storage.getStack("crucible"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68), new StackOutput(storage.getStack("basicCellFrame"), 85));
			un.addUncraftingRecipe(storage.getStack("transposer"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68));
			un.addUncraftingRecipe(storage.getStack("iceGen"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68), new RecipeOutput(Block.pistonBase, 75));
			un.addUncraftingRecipe(storage.getStack("rockGen"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("Servo"), 68));
			un.addUncraftingRecipe(storage.getStack("waterGen"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("Servo"), 68));
			un.addUncraftingRecipe(storage.getStack("crafter"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68), new RecipeOutput(Block.chest, 95));
			un.addUncraftingRecipe(storage.getStack("charging"), new StackOutput(storage.getStack("frame"), 75), new StackOutput(storage.getStack("CoilGold"), 68), new StackOutput(storage.getStack("basicCellFrame"), 85));
		}
		
	}
}
