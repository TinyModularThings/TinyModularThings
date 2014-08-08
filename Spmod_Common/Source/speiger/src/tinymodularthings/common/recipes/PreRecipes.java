package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;
import speiger.src.tinymodularthings.common.items.tools.ItemNetherCrystal;
import cpw.mods.fml.common.registry.GameRegistry;

public class PreRecipes
{
	public static void initRecipes()
	{
		
		GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 1), new Object[]{Item.diamond, Item.diamond, Block.coalBlock, Item.arrow, Item.arrow, Item.bone, Item.bone});
		
		GameRegistry.addRecipe(new ItemStack(TinyItems.interfaceBlock, 4, 0), new Object[]{"XYX", "CVC", "XBX", 'B', TinyBlocks.normalPipe, 'Y', Block.hopperBlock, 'C', ItemGear.getGearFromType(GearType.Redstone), 'V', Block.chest, 'X', new ItemStack(TinyItems.ingots, 1, EnumIngots.Aluminum.getIngotMeta())});
	
		GameRegistry.addRecipe(ItemNetherCrystal.createEmptyNetherCrystal(TinyItems.netherCrystal.itemID), new Object[]{"XYX", "YCY", "XYX", 'X', Item.netherStar, 'C', Item.diamond, 'Y', Item.enderPearl});
		GameRegistry.addShapelessRecipe(ItemNetherCrystal.createEmptyNetherCrystal(TinyItems.netherCrystal.itemID), new Object[]{new ItemStack(TinyItems.netherCrystal, 1, 5), Item.diamond, Item.enderPearl});
	}
}
