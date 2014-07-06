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
import cpw.mods.fml.common.registry.GameRegistry;

public class PreRecipes
{
	public static void initRecipes()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(TinyItems.tinyChest, 1, 0), "XXX", "X X", "XXX", 'X', "slabWood"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(TinyItems.tinyTank, 1, 0), "XXX", "XCX", "XXX", 'X', "slabWood", 'C', Item.bucketEmpty));
		
		for (int i = 0; i < 8; i++)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.tinyChest, 1, i + 1), new Object[] { new ItemStack(TinyItems.tinyChest, 1, i), new ItemStack(TinyItems.tinyChest) });
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.tinyTank, 1, i + 1), new Object[] { new ItemStack(TinyItems.tinyTank, 1, i), new ItemStack(TinyItems.tinyTank) });
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.advTinyChest, 1, i + 1), new Object[] { new ItemStack(TinyItems.advTinyChest, 1, i), new ItemStack(TinyItems.advTinyChest) });
		}
		for (int i = 0; i < 9; i++)
		{
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.tinyChest, 1, i), Item.minecartEmpty });
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.advTinyChest, 1, i), Item.minecartEmpty });
			CraftingManager.getInstance().addShapelessRecipe(new ItemStack(TinyItems.advTinyChest, 1, i), new Object[] { new ItemStack(TinyItems.tinyChest, 1, i), Item.redstone });
		}
		CraftingManager.getInstance().addRecipe(new ItemStack(TinyBlocks.transportBlock, 4, 0), new Object[] { "XXX", "XYX", "XXX", 'X', Block.obsidian, 'Y', Item.enderPearl});
		GameRegistry.addRecipe(new ItemStack(TinyBlocks.machine), new Object[] { "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', Item.coal});
		GameRegistry.addRecipe(new ItemStack(TinyBlocks.machine), new Object[] { "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', new ItemStack(Item.coal, 1, 1)});
		GameRegistry.addRecipe(new ItemStack(TinyBlocks.machine), new Object[] { "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', new ItemStack(Block.wood, 1, PathProxy.getRecipeBlankValue())});
		GameRegistry.addShapelessRecipe(new ItemStack(Item.skull, 1, 1), new Object[]{Item.diamond, Item.diamond, Block.coalBlock, Item.arrow, Item.arrow, Item.bone, Item.bone});
		
		GameRegistry.addRecipe(new ItemStack(TinyItems.interfaceBlock, 4, 0), new Object[]{"XYX", "CVC", "XBX", 'B', TinyBlocks.normalPipe, 'Y', Block.hopperBlock, 'C', ItemGear.getGearFromType(GearType.Redstone), 'V', Block.chest, 'X', new ItemStack(TinyItems.ingots, 1, EnumIngots.Aluminum.getIngotMeta())});
	}
}
