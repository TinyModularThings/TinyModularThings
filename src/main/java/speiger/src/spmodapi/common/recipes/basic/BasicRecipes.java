package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class BasicRecipes
{
	public static void load(PathProxy pp)
	{
		//Gear Parts
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.WoodenCorner, 32), "X", "Y", 'Y', "logWood", 'X', Items.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.WoodenRing, 16), " X " , "XYX", " X ", 'Y', Items.flint, 'X', "plankWood"));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing, 2), " X ", "XYX", " X ", 'X', "cobblestone", 'Y', Items.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing, 4), " X ", "XYX", " X ", 'X', "stone", 'Y', Items.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.StoneCorner, 8), "X", "Y", 'Y', "stone", 'X', Items.flint));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.CobbleRing), " X ", "XYX", " X ", 'X', "cobblestone", 'Y', ItemGear.getGearFromType(GearType.WoodenRing)));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.StoneRing), " X ", "XYX", " X ", 'X', "stone", 'Y', ItemGear.getGearFromType(GearType.WoodenRing)));
		pp.addFurnaceRecipe(ItemGear.getGearFromType(GearType.CobbleRing), ItemGear.getGearFromType(GearType.StoneRing), 0.2F);
		
		//Gears Constructing
		pp.addRecipe(ItemGear.getGearFromType(GearType.Wood), new Object[]{" X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.WoodenRing), 'X', ItemGear.getGearFromType(GearType.WoodenCorner)});
		pp.addRecipe(ItemGear.getGearFromType(GearType.Stone), new Object[]{" X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.StoneRing), 'X', ItemGear.getGearFromType(GearType.StoneCorner)});
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Bone), " X ", "XXX", " X ", 'X', "bone"));
		
		//Gear Upgrading
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Cobblestone), " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.Wood), 'X', "cobblestone"));
		pp.addRecipe(new ShapedOreRecipe(ItemGear.getGearFromType(GearType.Stone), " X ", "XYX", " X ", 'Y', ItemGear.getGearFromType(GearType.Wood), 'X', "stone"));
		pp.addRecipe(ItemGear.getGearFromType(GearType.Iron), new Object[]{" X ", "XYX", " X ", 'X', Items.iron_ingot, 'Y', ItemGear.getGearFromType(GearType.Stone)});
		pp.addRecipe(ItemGear.getGearFromType(GearType.Gold), new Object[]{" X ", "XYX", " X ", 'X', Items.gold_ingot, 'Y', ItemGear.getGearFromType(GearType.Iron)});
		pp.addRecipe(ItemGear.getGearFromType(GearType.Diamond), new Object[]{" X ", "XYX", " X ", 'X', Items.diamond, 'Y', ItemGear.getGearFromType(GearType.Gold)});
		pp.addRecipe(ItemGear.getGearFromType(GearType.Redstone), new Object[]{" X ", "XYX", " X ", 'X', Items.redstone, 'Y', ItemGear.getGearFromType(GearType.Gold)});
		pp.addFurnaceRecipe(ItemGear.getGearFromType(GearType.Cobblestone), ItemGear.getGearFromType(GearType.Stone), 0.35F);
		
		pp.addRecipe(new ShapedOreRecipe(new ItemStack(APIItems.colorCard, 8), "XYX", "CVD", "XBX", 'X', "plankWood", 'V', "stickWood", 'Y', "dyeWhite", 'C', "dyeGreen", 'D', "dyeBlue", 'B', "dyeRed"));
		pp.addRecipe(new ColorCardRecipe());
		for(FluidContainerData data : PathProxy.getDataFromFluid(FluidRegistry.WATER))
		{
			pp.addRecipe(new ColorCardCleaning(data.filledContainer));
		}
		pp.addRecipe(new ItemStack(APIBlocks.blockUtils, 1, 3), new Object[]{"XYX", "CVC", "XYX", 'X', Blocks.glass, 'C', Blocks.ice, 'V', Items.porkchop, 'Y', new ItemStack(Items.dye, 1, EnumColor.BLACK.getAsDye())});
	}
}
