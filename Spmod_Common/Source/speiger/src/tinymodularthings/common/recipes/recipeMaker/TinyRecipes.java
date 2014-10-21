package speiger.src.tinymodularthings.common.recipes.recipeMaker;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class TinyRecipes
{
	public static void init(PathProxy pp)
	{
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(TinyItems.tinyChest, 1, 0), "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', "slabWood"));
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(TinyItems.tinyTank, 1, 0), "XXX", "XYX", "XXX", 'Y', Item.bucketEmpty, 'X', GearType.Wood.getItem()));
		
		for (int i = 0; i < 8; i++)
		{
			pp.addRecipe(new ItemStack(TinyItems.tinyChest, 1, i + 1), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.tinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyChest, 1, i + 1), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.advTinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyTank, 1, i + 1), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyTank, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i + 1), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.tinyStorageCart, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i + 1), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.advTinyStorageCart, 1, i) });
		}
		
		for (int i = 0; i < 7; i++)
		{
			pp.addRecipe(new ItemStack(TinyItems.tinyChest, 1, i + 2), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyChest, 1, i + 2), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.advTinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyTank, 1, i + 2), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Iron.getItem(), 'Y', new ItemStack(TinyItems.tinyTank, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i + 2), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyStorageCart, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i + 2), new Object[] { "XXX", "XYX", "XXX", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.advTinyStorageCart, 1, i) });
		}
		
		for (int i = 0; i < 9; i++)
		{
			pp.addSRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.tinyChest, 1, i), Item.minecartEmpty });
			pp.addSRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.advTinyChest, 1, i), Item.minecartEmpty });
			pp.addRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i), new Object[] { "XYX", "CVC", "XBX", 'X', GearType.Redstone.getItem(), 'C', Block.torchRedstoneActive, 'Y', Block.stoneButton, 'B', Block.pressurePlateStone, 'V', new ItemStack(TinyItems.tinyStorageCart, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyChest, 1, i), new Object[] { "XYX", "CVC", "XBX", 'X', GearType.Redstone.getItem(), 'C', Block.torchRedstoneActive, 'Y', Block.stoneButton, 'B', Block.pressurePlateStone, 'V', new ItemStack(TinyItems.tinyChest, 1, i) });
		}
		pp.addRecipe(new ItemStack(TinyBlocks.transportBlock, 4, 0), new Object[] { "XXX", "XYX", "XXX", 'X', Block.obsidian, 'Y', Item.enderPearl });
		pp.addRecipe(new ItemStack(TinyBlocks.machine), new Object[] { "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', new ItemStack(Item.coal, 1, pp.getRecipeBlankValue()) });
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(TinyBlocks.machine), "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', "logWood"));
		
		for (int i = 8; i > 0; i--)
		{
			pp.addRecipe(new ItemStack(TinyItems.tinyChest, 1, i - 1), new Object[] { " X ", "XYX", " X ", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.tinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyChest, 1, i - 1), new Object[] { " X ", "XYX", " X ", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.advTinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyTank, 1, i - 1), new Object[] { " X ", "XYX", " X ", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyTank, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i - 1), new Object[] { " X ", "XYX", " X ", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.tinyStorageCart, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i - 1), new Object[] { " X ", "XYX", " X ", 'X', GearType.Wood.getItem(), 'Y', new ItemStack(TinyItems.advTinyStorageCart, 1, i) });
		}
		
		pp.addRecipe(new ItemStack(TinyBlocks.craftingBlock), new Object[]{"XXX", "XXX", "XXX", 'X', Block.workbench});
		pp.addRecipe(new ItemStack(TinyBlocks.craftingBlock, 2), new Object[]{"XXX", "XYX", "XXX", 'X', Block.workbench, 'Y', Item.goldNugget});
		
		for (int i = 8; i > 1; i--)
		{
			pp.addRecipe(new ItemStack(TinyItems.tinyChest, 1, i - 2), new Object[] { " X ", "XYX", " X ", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyChest, 1, i - 2), new Object[] { " X ", "XYX", " X ", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.advTinyChest, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyTank, 1, i - 2), new Object[] { " X ", "XYX", " X ", 'X', GearType.Iron.getItem(), 'Y', new ItemStack(TinyItems.tinyTank, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i - 2), new Object[] { " X ", "XYX", " X ", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.tinyStorageCart, 1, i) });
			pp.addRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i - 2), new Object[] { " X ", "XYX", " X ", 'X', GearType.Stone.getItem(), 'Y', new ItemStack(TinyItems.advTinyStorageCart, 1, i) });
		}
	}
}
