package speiger.src.spmodapi.common.recipes.basic;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.recipes.advanced.LampColorCopyRecipe;
import speiger.src.spmodapi.common.util.proxy.PathProxy;



public class LampRecipes
{
	public static void load(PathProxy pp)
	{
		for(FluidContainerData data : PathProxy.getDataFromFluid(FluidRegistry.WATER))
		{
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 16), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', data.filledContainer});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 34), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', data.filledContainer});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 52), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'M', Block.fenceIron, 'C', APIItems.hempResinBucket, 'Y', data.filledContainer});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 70), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'M', Block.fenceIron, 'C', APIItems.hempResinBucket, 'Y', data.filledContainer});
		}
		for(int i = 0;i<16;i++)
		{
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, i), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', new ItemStack(Item.dyePowder, 1, i)});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, i+18), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', new ItemStack(Item.dyePowder, 1, i)});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, i+36), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'M', Block.fenceIron, 'Y', new ItemStack(Item.dyePowder, 1, i)});
			pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, i+54), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'M', Block.fenceIron,  'Y', new ItemStack(Item.dyePowder, 1, i)});
		}
		
		pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 17), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', APIItems.colorCard});
		pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 35), new Object[]{"XYX", "XCX", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'C', APIItems.hempResinBucket, 'Y', APIItems.colorCard});
		pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 53), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Item.redstone, 'X', Block.glass, 'M', Block.fenceIron, 'C', APIItems.hempResinBucket, 'Y', APIItems.colorCard});
		pp.addRecipe(new ItemStack(APIBlocks.hempLamp, 4, 71), new Object[]{"XYX", "MCM", "VBV", 'V', Item.glowstone, 'B', Block.torchRedstoneActive, 'X', Block.glass, 'M', Block.fenceIron, 'C', APIItems.hempResinBucket, 'Y', APIItems.colorCard});
		for(int i = 0;i<4;i++)
		{
			pp.addRecipe(new LampColorCopyRecipe(new ItemStack(APIBlocks.hempLamp, 1, 18*i+16)));
		}
	}
}
