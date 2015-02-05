package speiger.src.tinymodularthings.common.recipes.pressureFurnace;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import speiger.src.api.common.registry.recipes.pressureFurnace.PressureRecipeList;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIngots;

public class PressureRecipes
{
	public static void initRecipes(PathProxy pp)
	{
		pp.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 0), EnumIngots.Copper.getIngot(), 0.5F);
		pp.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 3), EnumIngots.Lead.getIngot(), 0.5F);
		pp.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 2), EnumIngots.Silver.getIngot(), 0.5F);
		pp.addFurnaceRecipe(new ItemStack(TinyBlocks.ores, 1, 1), EnumIngots.Tin.getIngot(), 0.5F);
		
		PressureRecipeList list = PressureRecipeList.getInstance();

		list.addRecipe(new MetalMixing(new ItemStack(TinyItems.bauxitDust, 8), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 0), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta())).setTime(350).setMulti());
		list.addRecipe(new MetalMixing(new ItemStack(TinyItems.bauxitDust, 8), new ItemStack(Block.sand, 2), new ItemStack(Item.coal, 1, 1), new ItemStack(TinyItems.ingots, 2, EnumIngots.Aluminum.getIngotMeta())).setTime(350).setMulti());
		list.addRecipe(new MetalMixing(new ItemStack(TinyBlocks.ores, 3, 0), new ItemStack(TinyBlocks.ores, 1, 1), (ItemStack) null, new ItemStack(TinyItems.ingots, 8, EnumIngots.Bronze.getIngotMeta())).setTime(150));
		list.addRecipe(new MetalMixing(new ItemStack(TinyItems.ingots, 3, EnumIngots.Copper.getIngotMeta()), new ItemStack(TinyItems.ingots, 1, EnumIngots.Tin.getIngotMeta()), (ItemStack) null, new ItemStack(TinyItems.ingots, 4, EnumIngots.Bronze.getIngotMeta())));
		list.addRecipe(new BackMelting(new ItemStack(Item.minecartEmpty), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 5)));
		list.addRecipe(new BackMelting(new ItemStack(Item.bucketEmpty), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 3)));
		list.addRecipe(new BackMelting(new ItemStack(Item.horseArmorIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 7)));
		list.addRecipe(new BackMelting(new ItemStack(Item.horseArmorGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 7)));
		list.addRecipe(new BackMelting(new ItemStack(Item.horseArmorDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 7)));
		
		// Armor
		list.addRecipe(new BackMelting(new ItemStack(Item.helmetDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 4)));
		list.addRecipe(new BackMelting(new ItemStack(Item.helmetGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 4)));
		list.addRecipe(new BackMelting(new ItemStack(Item.helmetIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 4)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.plateDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 7)));
		list.addRecipe(new BackMelting(new ItemStack(Item.plateGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 7)));
		list.addRecipe(new BackMelting(new ItemStack(Item.plateIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 7)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.legsDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 5)));
		list.addRecipe(new BackMelting(new ItemStack(Item.legsGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 5)));
		list.addRecipe(new BackMelting(new ItemStack(Item.legsIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 5)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.bootsDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 3)));
		list.addRecipe(new BackMelting(new ItemStack(Item.bootsGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 3)));
		list.addRecipe(new BackMelting(new ItemStack(Item.bootsIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 3)));
		
		// Tools
		list.addRecipe(new BackMelting(new ItemStack(Item.pickaxeDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 2)));
		list.addRecipe(new BackMelting(new ItemStack(Item.pickaxeGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 2)));
		list.addRecipe(new BackMelting(new ItemStack(Item.pickaxeIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 2)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.axeDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 2)));
		list.addRecipe(new BackMelting(new ItemStack(Item.axeGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 2)));
		list.addRecipe(new BackMelting(new ItemStack(Item.axeIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 2)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.hoeDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.hoeGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.hoeIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1)));
		
		list.addRecipe(new BackMelting(new ItemStack(Item.shovelDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.shovelGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.shovelIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1)));
		
		// Weapon
		list.addRecipe(new BackMelting(new ItemStack(Item.swordDiamond), new ItemStack(Item.bucketLava), new ItemStack(Item.diamond, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.swordGold), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotGold, 1)));
		list.addRecipe(new BackMelting(new ItemStack(Item.swordIron), new ItemStack(Item.bucketLava), new ItemStack(Item.ingotIron, 1)));
		

	}
}
