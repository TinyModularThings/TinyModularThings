package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.recipes.advanced.ColorCardCleaning;
import speiger.src.spmodapi.common.recipes.advanced.ColorCardRecipe;
import speiger.src.spmodapi.common.recipes.advanced.ShapedColorCardRecipe;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class BasicRecipes
{
	public static void load(PathProxy pp)
	{
		// Gear Parts
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.WoodenCorner, 32), "X", "Y", 'Y', "logWood", 'X', Item.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.WoodenRing, 16), " X ", "XYX", " X ", 'Y', Item.flint, 'X', "plankWood"));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing, 2), " X ", "XYX", " X ", 'X', "cobblestone", 'Y', Item.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing, 4), " X ", "XYX", " X ", 'X', "stone", 'Y', Item.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.StoneCorner, 8), "X", "Y", 'Y', "stone", 'X', Item.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing), " X ", "XYX", " X ", 'X', "cobblestone", 'Y', ItemGear.getGearFromType(GearType.WoodenRing)));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.StoneRing), " X ", "XYX", " X ", 'X', "stone", 'Y', ItemGear.getGearFromType(GearType.WoodenRing)));
		pp.addFurnaceRecipe(ItemGear.getGearFromType(GearType.CobbleRing), ItemGear.getGearFromType(GearType.StoneRing), 0.2F);
		
		// Gears Constructing
		pp.addRecipe(ItemGear.getGearFromType(GearType.Wood), new Object[] { " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.WoodenRing), 'X', ItemGear.getGearFromType(GearType.WoodenCorner) });
		pp.addRecipe(ItemGear.getGearFromType(GearType.Stone), new Object[] { " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.StoneRing), 'X', ItemGear.getGearFromType(GearType.StoneCorner) });
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Bone), " X ", "XXX", " X ", 'X', "bone"));
		
		// Gear Upgrading
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Cobblestone), " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.Wood), 'X', "cobblestone"));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Stone), " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.Wood), 'X', "stone"));
		pp.addRecipe(ItemGear.getGearFromType(GearType.Iron), new Object[] { " X ", "XYX", " X ", 'X', Item.ingotIron, 'Y', ItemGear.getGearFromType(GearType.Stone) });
		pp.addRecipe(ItemGear.getGearFromType(GearType.Gold), new Object[] { " X ", "XYX", " X ", 'X', Item.ingotGold, 'Y', ItemGear.getGearFromType(GearType.Iron) });
		pp.addRecipe(ItemGear.getGearFromType(GearType.Diamond), new Object[] { " X ", "XYX", " X ", 'X', Item.diamond, 'Y', ItemGear.getGearFromType(GearType.Gold) });
		pp.addRecipe(ItemGear.getGearFromType(GearType.Redstone), new Object[] { " X ", "XYX", " X ", 'X', Item.redstone, 'Y', ItemGear.getGearFromType(GearType.Gold) });
		pp.addFurnaceRecipe(ItemGear.getGearFromType(GearType.Cobblestone), ItemGear.getGearFromType(GearType.Stone), 0.35F);
		
		pp.addRecipe(new ShapedColorCardRecipe(new ItemStack(APIItems.colorCard, 8), "XYX", "CVD", "XBX", 'X', "plankWood", 'V', "stickWood", 'Y', "dyeWhite", 'C', "dyeGreen", 'D', "dyeBlue", 'B', "dyeRed"));
		pp.addRecipe(new ColorCardRecipe());
		for (FluidContainerData data : PathProxy.getDataFromFluid(FluidRegistry.WATER))
		{
			pp.addRecipe(new ColorCardCleaning(data.filledContainer));
		}
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 3), new Object[] { "XYX", "CVC", "XYX", 'X', Block.glass, 'C', Block.ice, 'V', Item.porkRaw, 'Y', new ItemStack(Item.dyePowder, 1, EnumColor.BLACK.getAsDye()) });
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 2), new Object[] { "ABC", "DEF", "GGG", 'A', Item.leather, 'B', Item.porkRaw, 'C', Item.feather, 'D', Block.cloth, 'E', Item.ingotIron, 'F', Block.mushroomRed, 'G', new ItemStack(Block.glowStone) });
		
	}
}
