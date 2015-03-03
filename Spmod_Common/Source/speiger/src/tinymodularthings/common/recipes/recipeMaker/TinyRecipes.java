package speiger.src.tinymodularthings.common.recipes.recipeMaker;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.blocks.storage.ItemBlockStorage;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.TinyDowngradeRecipe;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.TinyRecipeHelper;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.TinyTransformRecipe;
import speiger.src.tinymodularthings.common.recipes.recipeHelper.TinyUpgradeRecipe;

public class TinyRecipes
{
	static TinyRecipeHelper helper = new TinyRecipeHelper();
	
	public static void init(PathProxy pp)
	{
		pp.addOreRecipe(new ItemStack(TinyItems.tinyChest, 1, 0), "XXX", "XYX", "XXX", 'X', GearType.Wood.getItem(), 'Y', "slabWood");
		pp.addOreRecipe(new ItemStack(TinyItems.tinyTank, 1, 0), "XXX", "XYX", "XXX", 'Y', Item.bucketEmpty, 'X', GearType.Wood.getItem());
		
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyChest, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyChest, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyTank, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.tinyTank, 1, PathProxy.getRecipeBlankValue()), "gearIron", helper, 2));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyTank, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 1));
		pp.addRecipe(new TinyUpgradeRecipe(new ItemStack(TinyItems.advTinyTank, 1, PathProxy.getRecipeBlankValue()), "gearIron", helper, 2));
		
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyChest, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyChest, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearWood", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 2));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyTank, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.tinyTank, 1, PathProxy.getRecipeBlankValue()), "gearIron", helper, 2));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyTank, 1, PathProxy.getRecipeBlankValue()), "gearStone", helper, 1));
		pp.addRecipe(new TinyDowngradeRecipe(new ItemStack(TinyItems.advTinyTank, 1, PathProxy.getRecipeBlankValue()), "gearIron", helper, 2));
		
		pp.addRecipe(new TinyTransformRecipe(new ItemStack(TinyItems.advTinyChest), helper, 5, new Object[]{"XYX", "XCX", "XYX", 'Y', Block.chest, 'X', GearType.Wood.getItem(), 'C', new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue())}));
		pp.addRecipe(new TinyTransformRecipe(new ItemStack(TinyItems.advTinyStorageCart), helper, 5, new Object[]{ "XYX", "CVC", "XBX", 'X', GearType.Redstone.getItem(), 'C', Block.torchRedstoneActive, 'Y', Block.stoneButton, 'B', Block.pressurePlateStone, 'V', new ItemStack(TinyItems.tinyStorageCart, 1, PathProxy.getRecipeBlankValue())}));
		pp.addRecipe(new TinyTransformRecipe(new ItemStack(TinyItems.advTinyTank, 1), helper, 5, new Object[]{"XYX", "XCX", "XYX", 'Y', Block.chest, 'X', GearType.Wood.getItem(), 'C', new ItemStack(TinyItems.tinyTank, 1, PathProxy.getRecipeBlankValue())}));
		pp.addRecipe(new TinyTransformRecipe(new ItemStack(TinyBlocks.storageBlock, 1, 4), helper, 5, new Object[]{"XYX", "XCX", "XYX", 'Y', Block.chest, 'X', GearType.Wood.getItem(), 'C', new ItemStack(TinyItems.tinyChest, 1, PathProxy.getRecipeBlankValue())}));
		
		for (int i = 0; i < 9; i++)
		{
			pp.addSRecipe(new ItemStack(TinyItems.tinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.tinyChest, 1, i), Item.minecartEmpty });
			pp.addSRecipe(new ItemStack(TinyItems.advTinyStorageCart, 1, i), new Object[] { new ItemStack(TinyItems.advTinyChest, 1, i), Item.minecartEmpty });
		}
		pp.addRecipe(new ItemStack(TinyBlocks.transportBlock, 4, 0), new Object[] { "XXX", "XYX", "XXX", 'X', Block.obsidian, 'Y', Item.enderPearl });
		pp.addRecipe(new ItemStack(TinyBlocks.machine), new Object[] { "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', new ItemStack(Item.coal, 1, pp.getRecipeBlankValue()) });
		pp.addOreRecipe(new ItemStack(TinyBlocks.machine), "XXX", "XYX", "XXX", 'X', Block.cobblestone, 'Y', "logWood");
		
		pp.addRecipe(new ItemStack(TinyBlocks.craftingBlock), new Object[]{"XXX", "XXX", "XXX", 'X', Block.workbench});
		pp.addRecipe(new ItemStack(TinyBlocks.craftingBlock, 2), new Object[]{"XXX", "XYX", "XXX", 'X', Block.workbench, 'Y', Item.goldNugget});
		
	}
}
