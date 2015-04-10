package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.InterfaceRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class PreRecipes
{
	public static void initRecipes()
	{
		
		GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 1), new Object[] { Item.diamond, Item.diamond, Block.coalBlock, Item.arrow, Item.arrow, Item.bone, Item.bone });
		
		GameRegistry.addRecipe(new ItemStack(TinyItems.interfaceBlock, 4, 0), new Object[] { "XYX", "CVC", "XBX", 'B', TinyBlocks.normalPipe, 'Y', Block.hopperBlock, 'C', ItemGear.getGearFromType(GearType.Redstone), 'V', Block.chest, 'X', new ItemStack(TinyItems.ingots, 1, EnumIngots.Aluminum.getIngotMeta()) });
		
		GameRegistry.addRecipe(new ItemStack(TinyItems.netherCrystal), new Object[] { "XYX", "YCY", "XYX", 'X', Item.netherStar, 'C', Item.diamond, 'Y', Item.enderPearl });
		GameRegistry.addShapelessRecipe(new ItemStack(TinyItems.netherCrystal), new Object[] { new ItemStack(TinyItems.netherCrystal, 1, 5), Item.diamond, Item.enderPearl });
	
		PathProxy.addRecipe(new ItemStack(TinyItems.cell, 4, 0), new Object[]{" X ", "XYX", " X ", 'X', EnumIngots.Aluminum.getIngot(), 'Y', Block.glass});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.craftingBlock, 1, 1), new Object[]{"YXY", "VFV", "DCD", 'V', Block.enderChest, 'F', GearType.Diamond.getItem(), 'D', GearType.Redstone.getItem(),  'C', Block.chest, 'X', Block.blockIron, 'Y', new ItemStack(TinyItems.advTinyChest, 1, 4)});
		PathProxy.addSRecipe(new ItemStack(TinyBlocks.transportBlock, 1, 4), new Object[]{TinyBlocks.transportBlock, new ItemStack(Item.nameTag)});
		PathProxy.addAssemblyRecipe(new ItemStack(TinyBlocks.transportBlock, 1, 4), 100000, new ItemStack(TinyBlocks.transportBlock, 1, 0));
		PathProxy.addSRecipe(new ItemStack(TinyBlocks.craftingBlock, 1, 2), new Object[] {TinyBlocks.craftingBlock, Block.enderChest});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 5), new Object[]{"X X", "XYX", " X ", 'Y', new ItemStack(TinyItems.tinyTank, 1, 2), 'X', Block.stone});
		PathProxy.addRecipe(new ItemStack(TinyItems.potionBag), new Object[]{"XYX", "CVC", "BNB", 'C', Block.chest, 'V', new ItemStack(TinyItems.cell, 1, 0), 'B', Block.pistonStickyBase, 'X', Item.leather, 'Y', Item.silk, 'N', new ItemStack(TinyItems.advTinyChest, 1, 8)});
		PathProxy.addRecipe(new ItemStack(TinyItems.interfaceBlock, 1, 1), new Object[]{"XYX", "CVC", "XZX", 'V', new ItemStack(TinyItems.interfaceBlock, 1, 0), 'X', EnumIngots.Aluminum.getIngot(), 'C', new ItemStack(TinyItems.tinyTank), 'Y', new ItemStack(Block.hopperBlock), 'Z', TinyBlocks.bigPipe});		
		PathProxy.addRecipe(new InterfaceRecipe());
		
		PathProxy.addRecipe(new ShapedOreRecipe(new ItemStack(Block.pistonBase), "XXX", "YCY", "YVY", 'X', "plankWood", 'Y', "cobblestone", 'C', "ingotBronze", 'V', Item.redstone));
		PathProxy.addRecipe(new ShapedOreRecipe(new ItemStack(Block.pistonBase), "XXX", "YCY", "YVY", 'X', "plankWood", 'Y', "cobblestone", 'C', "ingotSilver", 'V', Item.redstone));
		PathProxy.addRecipe(new ItemStack(TinyBlocks.craftingBlock, 1, 3), new Object[]{"XYX", "CVB", "XNX", 'X', new ItemStack(APIItems.circuits, 1, 1), 'V', GearType.Redstone.getItem(), 'Y', new ItemStack(APIBlocks.blockUtils), 'C', TinyItems.advTinyChest, 'B', Block.dispenser, 'N', Block.anvil});
		
	}
}
