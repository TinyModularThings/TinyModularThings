package speiger.src.spmodapi.common.recipes.basic;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class HempRecipes
{
	public static void load(PathProxy pp)
	{
		for(int i = 1;i<17;i++)
		{
			for(int z = 0;z<17;z++)
			{
				ItemStack g = APIItems.hempPlates[z];
				pp.addSRecipe(new ItemStack(APIItems.hempPlates[i].getItem(), 8, APIItems.hempPlates[i].getItemDamage()), new Object[]{new ItemStack(Items.dye, 1, i-1), g, g, g, g, g, g, g, g});
				pp.addSRecipe(new ItemStack(APIBlocks.hempBlockPlated, 1, i-1), new Object[]{new ItemStack(APIBlocks.hempBlock, 1, i-1), APIItems.hempPlates[z]});
				pp.addSRecipe(new ItemStack(APIBlocks.hempBrickPlated, 1, i-1), new Object[]{new ItemStack(APIBlocks.hempBrick, 1, i-1), APIItems.hempPlates[z]});
			}
			pp.addRecipe(new ItemStack(APIBlocks.hempBlock, 1, i-1), new Object[]{"XX", "XX", 'X', APIItems.hempPlates[i]});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockPlated, 1, i-1), new Object[]{new ItemStack(APIBlocks.hempBlock, 1, i-1), APIItems.hempPlates[i]});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrick, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlock, 1, i-1), 'Y', Blocks.stone});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrick, 4, i-1), new Object[]{" X ", "XYX", " X ", 'X', new ItemStack(APIBlocks.hempBlock, 1, i-1), 'Y', Blocks.stone});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBrickPlated, 1, i-1), new Object[]{new ItemStack(APIBlocks.hempBrick, 1, i-1), APIItems.hempPlates[i]});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrickPlated, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockPlated, 1, i-1), 'Y', Blocks.stone});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrickPlated, 4, i-1), new Object[]{" X ", "XYX", " X ", 'X', new ItemStack(APIBlocks.hempBlockPlated, 1, i-1), 'Y', Blocks.stone});
			pp.addRecipe(new ItemStack(APIBlocks.hempBlock, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 0), 'Y', new ItemStack(Items.dye, 1, i-1)});
			pp.addRecipe(new ItemStack(APIBlocks.hempBlockPlated, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 1), 'Y', new ItemStack(Items.dye, 1, i-1)});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrick, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 2), 'Y', new ItemStack(Items.dye, 1, i-1)});
			pp.addRecipe(new ItemStack(APIBlocks.hempBrickPlated, 8, i-1), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 3), 'Y', new ItemStack(Items.dye, 1, i-1)});
		
			addHempRecipe(pp, APIBlocks.savedHempBlock, i-1, new ItemStack(APIBlocks.hempBlock, 1, i-1));
			addHempRecipe(pp, APIBlocks.savedHempBlockPlated, i-1, new ItemStack(APIBlocks.hempBlockPlated, 1, i-1));
			addHempRecipe(pp, APIBlocks.savedHempBrick, i-1, new ItemStack(APIBlocks.hempBrick, 1, i-1));
			addHempRecipe(pp, APIBlocks.savedHempBrickPlated, i-1, new ItemStack(APIBlocks.hempBrickPlated, 1, i-1));
		}
		

		for(int z = 0;z<17;z++)
		{
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 1), new Object[]{APIItems.hempPlates[z], new ItemStack(APIBlocks.hempBlockBase, 1, 0)});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 3), new Object[]{APIItems.hempPlates[z], new ItemStack(APIBlocks.hempBlockBase, 1, 2)});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 5), new Object[]{APIItems.hempPlates[z], new ItemStack(APIBlocks.hempBlockBase, 1, 4)});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 7), new Object[]{APIItems.hempPlates[z], new ItemStack(APIBlocks.hempBlockBase, 1, 6)});
		}

		for(int z = 0;z<4;z++)
		{
			addHempRecipe(pp, APIBlocks.hempBlockBase, z+4, new ItemStack(APIBlocks.hempBlockBase, 1, z));
		}
		
		pp.addRecipe(new ItemStack(APIItems.hempPlates[0].getItem(), 4, APIItems.hempPlates[0].getItemDamage()), new Object[]{"XX", "XX", 'X', APIItems.hemp});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 0), new Object[]{"XX", "XX", 'X', APIItems.hempPlates[0]});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 2), new Object[]{" X ", "XYX", " X ", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 0), 'Y', Blocks.stone});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 3), new Object[]{" X ", "XYX", " X ", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 1), 'Y', Blocks.stone});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 2), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 0), 'Y', Blocks.stone});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 3), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 1), 'Y', Blocks.stone});
		
		pp.addRecipe(new ItemStack(APIBlocks.hempStraw), new Object[]{"XXX", "XXX", "XXX", 'X', APIItems.hemp});
		pp.addSRecipe(new ItemStack(APIItems.hemp, 9), new Object[]{new ItemStack(APIBlocks.hempStraw)});
		pp.addSRecipe(new ItemStack(Items.string), new Object[] { APIItems.hemp });
		pp.addRecipe(new ItemStack(Items.paper, 3), new Object[] { "X X", " X ", "X X", 'X', APIItems.hemp });
		
		pp.addRecipe(new ItemStack(APIItems.hempBoots), new Object[] { "X X", "X X", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempHelmet), new Object[] { "XXX", "X X", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempChestPlate), new Object[] { "X X", "XXX", "XXX", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempLeggings), new Object[] { "XXX", "X X", "X X", 'X', APIItems.hemp });
		pp.addSRecipe(new ItemStack(APIItems.blueDye, 2), new Object[]{APIBlocks.blueFlower});
	}
	
	public static void addHempRecipe(PathProxy par0, Block output, int outputMeta, ItemStack input)
	{
		par0.addSRecipe(new ItemStack(output, 1, outputMeta), new Object[]{Items.gold_nugget, input});
		par0.addSRecipe(new ItemStack(output, 2, outputMeta), new Object[]{Items.gold_nugget, input, input});
		par0.addSRecipe(new ItemStack(output, 3, outputMeta), new Object[]{Items.gold_nugget, input, input, input});
		par0.addSRecipe(new ItemStack(output, 4, outputMeta), new Object[]{Items.gold_nugget, input, input, input, input});
		par0.addSRecipe(new ItemStack(output, 5, outputMeta), new Object[]{Items.gold_nugget, input, input, input, input, input});
		par0.addSRecipe(new ItemStack(output, 6, outputMeta), new Object[]{Items.gold_nugget, input, input, input, input, input, input});
		par0.addSRecipe(new ItemStack(output, 7, outputMeta), new Object[]{Items.gold_nugget, input, input, input, input, input, input, input});
		par0.addSRecipe(new ItemStack(output, 8, outputMeta), new Object[]{Items.gold_nugget, input, input, input, input, input, input, input, input});
	}
}
