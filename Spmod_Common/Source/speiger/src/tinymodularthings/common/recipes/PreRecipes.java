package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.recipes.advanced.ItemInventoryRecipe;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;
import speiger.src.tinymodularthings.common.items.tools.ItemNetherCrystal;
import speiger.src.tinymodularthings.common.items.tools.ItemPotionBag;
import cpw.mods.fml.common.registry.GameRegistry;

public class PreRecipes
{
	public static void initRecipes()
	{
		
		GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 1), new Object[] { Item.diamond, Item.diamond, Block.coalBlock, Item.arrow, Item.arrow, Item.bone, Item.bone });
		
		GameRegistry.addRecipe(new ItemStack(TinyItems.interfaceBlock, 4, 0), new Object[] { "XYX", "CVC", "XBX", 'B', TinyBlocks.normalPipe, 'Y', Block.hopperBlock, 'C', ItemGear.getGearFromType(GearType.Redstone), 'V', Block.chest, 'X', new ItemStack(TinyItems.ingots, 1, EnumIngots.Aluminum.getIngotMeta()) });
		
		GameRegistry.addRecipe(ItemNetherCrystal.createEmptyNetherCrystal(TinyItems.netherCrystal.itemID), new Object[] { "XYX", "YCY", "XYX", 'X', Item.netherStar, 'C', Item.diamond, 'Y', Item.enderPearl });
		GameRegistry.addShapelessRecipe(ItemNetherCrystal.createEmptyNetherCrystal(TinyItems.netherCrystal.itemID), new Object[] { new ItemStack(TinyItems.netherCrystal, 1, 5), Item.diamond, Item.enderPearl });
	
		PathProxy.addRecipe(new ItemStack(TinyItems.cell, 4, 0), new Object[]{" X ", "XYX", " X ", 'X', EnumIngots.Aluminum.getIngot(), 'Y', Block.glass});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.craftingBlock, 1, 1), new Object[]{"YXY", "VFV", "DCD", 'V', Block.enderChest, 'F', GearType.Diamond.getItem(), 'D', GearType.Redstone.getItem(),  'C', Block.chest, 'X', Block.blockIron, 'Y', new ItemStack(TinyItems.advTinyChest, 1, 4)});
		PathProxy.addSRecipe(new ItemStack(TinyBlocks.transportBlock, 1, 4), new Object[]{TinyBlocks.transportBlock, new ItemStack(Item.nameTag)});
		PathProxy.addAssemblyRecipe(new ItemStack(TinyBlocks.transportBlock, 1, 4), 100000, new ItemStack(TinyBlocks.transportBlock, 1, 0));
		PathProxy.addSRecipe(new ItemStack(TinyBlocks.craftingBlock, 1, 2), new Object[] {TinyBlocks.craftingBlock, Block.enderChest});
		PathProxy.addRecipe(new ItemStack(TinyBlocks.machine, 1, 5), new Object[]{"X X", "XYX", " X ", 'Y', new ItemStack(TinyItems.tinyTank, 2), 'X', Block.stone});
		PathProxy.addRecipe(new ItemInventoryRecipe((ItemPotionBag)TinyItems.potionBag, 0, new ItemStack[]{new ItemStack(Item.leather), new ItemStack(Item.silk), new ItemStack(Item.leather), new ItemStack(Block.chest), new ItemStack(TinyItems.cell, 1, 0), new ItemStack(Block.chest), new ItemStack(Block.pistonStickyBase), new ItemStack(TinyItems.advTinyChest, 1, 8),new ItemStack(Block.pistonStickyBase)}));

		
	}
}
