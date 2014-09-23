package speiger.src.tinymodularthings.common.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;

public class OreRecipes
{
	public static void loadOreRecipes()
	{
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 0), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.bauxitDust, 8, 0), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 1), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyBlocks.ores, 3, 0), new ItemStack(TinyBlocks.ores, 1, 1), (ItemStack) null, new ItemStack(TinyItems.ingots, 8, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(TinyItems.ingots, 3, EnumIngots.Copper.getIngotMeta()), new ItemStack(TinyItems.ingots, 1, EnumIngots.Tin.getIngotMeta()), (ItemStack) null, new ItemStack(TinyItems.ingots, 4, EnumIngots.Bronze.getIngotMeta()), true, true);
		PathProxy.addPressureRecipes(new ItemStack(Item.minecartEmpty), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 5), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.bucketEmpty), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 3), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.horseArmorDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 7), false, false);
		
		// Armor
		PathProxy.addPressureRecipes(new ItemStack(Item.helmetDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 4), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.helmetGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 4), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.helmetIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 4), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.plateDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.plateGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 7), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.plateIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 7), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.legsDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 5), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.legsGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 5), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.legsIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 5), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.bootsDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 3), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.bootsGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 3), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.bootsIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 3), false, false);
		
		// Tools
		PathProxy.addPressureRecipes(new ItemStack(Item.pickaxeDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 2), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.pickaxeGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 2), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.pickaxeIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 2), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.axeDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 2), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.axeGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 2), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.axeIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 2), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.hoeDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.hoeGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.hoeIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1), false, false);
		
		PathProxy.addPressureRecipes(new ItemStack(Item.shovelDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.shovelGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.shovelIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1), false, false);
		
		// Weapon
		PathProxy.addPressureRecipes(new ItemStack(Item.swordDiamond), null, new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.swordGold), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1), false, false);
		PathProxy.addPressureRecipes(new ItemStack(Item.swordIron), null, new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1), false, false);
		
		PathProxy.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 0), EnumIngots.Copper.getIngot(), 0.5F);
		PathProxy.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 3), EnumIngots.Lead.getIngot(), 0.5F);
		PathProxy.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 2), EnumIngots.Silver.getIngot(), 0.5F);
		PathProxy.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 1), EnumIngots.Tin.getIngot(), 0.5F);
		
	}
	
}
