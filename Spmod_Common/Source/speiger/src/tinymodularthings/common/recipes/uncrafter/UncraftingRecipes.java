package speiger.src.tinymodularthings.common.recipes.uncrafter;

import ic2.api.item.Items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.api.common.data.utils.BlockData;
import speiger.src.api.common.registry.recipes.output.RandomOutput;
import speiger.src.api.common.registry.recipes.output.RecipeOutput;
import speiger.src.api.common.registry.recipes.output.StackOutput;
import speiger.src.api.common.registry.recipes.uncrafter.UncrafterRecipeList;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.data.ClassStorage;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.common.Loader;

public class UncraftingRecipes
{
	public static void initRecipes(PathProxy pp)
	{
		
		UncrafterRecipeList un = UncrafterRecipeList.getInstance();
		ClassStorage storage = ClassStorage.getInstance();

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
