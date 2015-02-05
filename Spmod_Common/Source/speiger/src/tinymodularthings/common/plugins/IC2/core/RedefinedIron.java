package speiger.src.tinymodularthings.common.plugins.IC2.core;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.recipes.pressureFurnace.IPressureFurnaceRecipe;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class RedefinedIron implements IPressureFurnaceRecipe
{
	
	@Override
	public ItemStack getOutput()
	{
		ItemStack copy = ItemStack.copyItemStack(Items.getItem("refinedIronIngot"));
		copy.stackSize = 2;
		return copy;
	}
	
	@Override
	public ItemStack getInput()
	{
		return new ItemStack(Block.oreIron);
	}
	
	@Override
	public ItemStack getSecondInput()
	{
		return null;
	}
	
	@Override
	public ItemStack getCombiner()
	{
		return new ItemStack(Item.coal);
	}
	
	@Override
	public boolean recipeMatches(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		if(input != null && secondInput == null && combiner != null)
		{
			return InventoryUtil.isItemEqual(input, getInput()) && input.stackSize >= times && InventoryUtil.isItemEqual(combiner, new ItemStack(Item.coal, 1, PathProxy.getRecipeBlankValue()));
		}
		return false;
	}
	
	@Override
	public void runRecipe(ItemStack input, ItemStack secondInput, ItemStack combiner, int times)
	{
		input.stackSize -= times;
		if(CodeProxy.getRandom().nextInt(100) == 0)
		{
			combiner.stackSize--;
		}
	}
	
	@Override
	public boolean isMultiRecipe()
	{
		return true;
	}
	
	@Override
	public boolean usesCombiner()
	{
		return false;
	}
	
	@Override
	public int getRequiredCookTime()
	{
		return 120;
	}
	
}
