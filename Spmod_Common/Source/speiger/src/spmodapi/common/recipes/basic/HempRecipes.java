package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class HempRecipes
{
	public static void load(PathProxy pp)
	{
		//Hemp Plates
		pp.addRecipe(InventoryUtil.getItemWithStackSize(APIItems.hempPlates[0], 4), new Object[]{"XX", "XX", 'X', APIItems.hemp});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase), new Object[]{"XX", "XX", 'X', APIItems.hempPlates[0]});
		addColorRecipe(pp, APIItems.multiPlate, 1, APIItems.hempPlates[0], EnumColor.Nothing);
		for(int i = 1;i<17;i++)
		{
			addColorRecipe(pp, APIItems.multiPlate, 1, APIItems.hempPlates[i], EnumColor.values()[i-1]);
			pp.addRecipe(new ItemStack(APIBlocks.hempBlock, 1, i-1), new Object[]{"XX", "XX", 'X', APIItems.hempPlates[i]});
		}
				
		//Hemp Blocks
		pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 2), new Object[]{new ItemStack(APIBlocks.hempBlockBase, 1, 0), APIItems.hempPlates[0]});
		pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 3), new Object[]{new ItemStack(APIBlocks.hempBlockBase, 1, 1), APIItems.hempPlates[0]});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 1), new Object[]{" X ", "XYX", " X ", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 0)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 1), new Object[]{"XXX", "XYX", "XXX", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 0)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 3), new Object[]{" X ", "XYX", " X ", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 2)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 3), new Object[]{"XXX", "XYX", "XXX", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 2)});
		
		pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 6), new Object[]{new ItemStack(APIBlocks.hempBlockBase, 1, 4), APIItems.hempPlates[0]});
		pp.addSRecipe(new ItemStack(APIBlocks.hempBlockBase, 1, 7), new Object[]{new ItemStack(APIBlocks.hempBlockBase, 1, 5), APIItems.hempPlates[0]});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 5), new Object[]{" X ", "XYX", " X ", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 4)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 5), new Object[]{"XXX", "XYX", "XXX", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 4)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 4, 7), new Object[]{" X ", "XYX", " X ", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 6)});
		pp.addRecipe(new ItemStack(APIBlocks.hempBlockBase, 8, 7), new Object[]{"XXX", "XYX", "XXX", 'Y', Block.stone, 'X', new ItemStack(APIBlocks.hempBlockBase, 1, 6)});
		
		addHempRecipe(pp, APIBlocks.hempBlock, APIBlocks.savedHempBlock);
		addHempRecipe(pp, APIBlocks.hempBrick, APIBlocks.savedHempBrick);
		addHempRecipe(pp, APIBlocks.hempBlockPlated, APIBlocks.savedHempBlockPlated);
		addHempRecipe(pp, APIBlocks.hempBrickPlated, APIBlocks.savedHempBrickPlated);
		addHempRecipe(pp, APIBlocks.hempBlockBase, 4, new ItemStack(APIBlocks.hempBlockBase));
		addHempRecipe(pp, APIBlocks.hempBlockBase, 5, new ItemStack(APIBlocks.hempBlockBase, 1, 1));
		addHempRecipe(pp, APIBlocks.hempBlockBase, 6, new ItemStack(APIBlocks.hempBlockBase, 1, 2));
		addHempRecipe(pp, APIBlocks.hempBlockBase, 7, new ItemStack(APIBlocks.hempBlockBase, 1, 3));
		
		
		addStoneRecipe(pp, APIBlocks.hempBlock, APIBlocks.hempBrick);
		addStoneRecipe(pp, APIBlocks.hempBlockPlated, APIBlocks.hempBrickPlated);
		addStoneRecipe(pp, APIBlocks.savedHempBlock, APIBlocks.savedHempBrick);
		addStoneRecipe(pp, APIBlocks.savedHempBlockPlated, APIBlocks.savedHempBrickPlated);
		
		
		for(int i = 0;i<16;i++)
		{
			pp.addSRecipe(new ItemStack(APIBlocks.hempBlockPlated, 1, i), new Object[]{new ItemStack(APIBlocks.hempBlock, 1, i), APIItems.hempPlates[i+1]});
			pp.addSRecipe(new ItemStack(APIBlocks.hempBrickPlated, 1, i), new Object[]{new ItemStack(APIBlocks.hempBrick, 1, i), APIItems.hempPlates[i+1]});
			pp.addSRecipe(new ItemStack(APIBlocks.savedHempBlockPlated, 1, i), new Object[]{new ItemStack(APIBlocks.savedHempBlock, 1, i), APIItems.hempPlates[i+1]});
			pp.addSRecipe(new ItemStack(APIBlocks.savedHempBrickPlated, 1, i), new Object[]{new ItemStack(APIBlocks.savedHempBrick, 1, i), APIItems.hempPlates[i+1]});
		}
		
		//Hemp Utils
		pp.addRecipe(new ItemStack(APIBlocks.hempStraw), new Object[] { "XXX", "XXX", "XXX", 'X', APIItems.hemp });
		pp.addSRecipe(new ItemStack(APIItems.hemp, 9), new Object[] { new ItemStack(APIBlocks.hempStraw) });
		pp.addSRecipe(new ItemStack(Item.silk), new Object[] { APIItems.hemp });
		pp.addRecipe(new ItemStack(Item.paper, 3), new Object[] { "X X", " X ", "X X", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempBoots), new Object[] { "X X", "X X", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempHelmet), new Object[] { "XXX", "X X", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempChestPlate), new Object[] { "X X", "XXX", "XXX", 'X', APIItems.hemp });
		pp.addRecipe(new ItemStack(APIItems.hempLeggings), new Object[] { "XXX", "X X", "X X", 'X', APIItems.hemp });		
		pp.addRecipe(new ItemStack(APIItems.hempResinBucket), new Object[] { "XBX", "XXX", "CVC", 'C', Block.stone, 'V', Item.bucketEmpty, 'B', Item.flint, 'X', APIBlocks.hempStraw });
	}
	
	public static void addHempRecipe(PathProxy par0, Block output, Block input)
	{
		for(int i = 0;i<16;i++)
		{
			addHempRecipe(par0, output, i, new ItemStack(input, 1, i));
		}
	}
	
	public static void addHempRecipe(PathProxy par0, Block output, int outputMeta, ItemStack input)
	{
		par0.addSRecipe(new ItemStack(output, 1, outputMeta), new Object[] { Item.goldNugget, input });
		par0.addSRecipe(new ItemStack(output, 2, outputMeta), new Object[] { Item.goldNugget, input, input });
		par0.addSRecipe(new ItemStack(output, 3, outputMeta), new Object[] { Item.goldNugget, input, input, input });
		par0.addSRecipe(new ItemStack(output, 4, outputMeta), new Object[] { Item.goldNugget, input, input, input, input });
		par0.addSRecipe(new ItemStack(output, 5, outputMeta), new Object[] { Item.goldNugget, input, input, input, input, input });
		par0.addSRecipe(new ItemStack(output, 6, outputMeta), new Object[] { Item.goldNugget, input, input, input, input, input, input });
		par0.addSRecipe(new ItemStack(output, 7, outputMeta), new Object[] { Item.goldNugget, input, input, input, input, input, input, input });
		par0.addSRecipe(new ItemStack(output, 8, outputMeta), new Object[] { Item.goldNugget, input, input, input, input, input, input, input, input });
	}
	
	public static void addStoneRecipe(PathProxy pp, Block input, Block output)
	{
		for(int i = 0;i<16;i++)
		{
			pp.addRecipe(new ItemStack(output, 4, i), new Object[]{" X ", "XYX", " X ", 'X', new ItemStack(input, 1, i), 'Y', Block.stone});
			pp.addRecipe(new ItemStack(output, 8, i), new Object[]{"XXX", "XYX", "XXX", 'X', new ItemStack(input, 1, i), 'Y', Block.stone});
		}
	}
	
	
	public static void addColorRecipe(PathProxy pp, Item par1, int startMeta, ItemStack input, EnumColor color)
	{
		for(int i = startMeta;i<startMeta+16;i++)
		{
			if(color.getAsDye() == (i-startMeta))
			{
				continue;
			}
			pp.addSRecipe(new ItemStack(par1, 8, i), new Object[]{new ItemStack(Item.dyePowder, 1, i-startMeta), input, input, input, input, input, input, input, input});
		}
	}
}
