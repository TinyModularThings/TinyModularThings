package speiger.src.spmodapi.common.recipes.advanced;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import speiger.src.spmodapi.common.items.core.SpmodInventoryItem;

public class ItemInventoryRecipe extends ShapedRecipes
{
	int meta;
	SpmodInventoryItem item;
	public ItemInventoryRecipe(SpmodInventoryItem output, int meta, ItemStack[] input)
	{
		super(3, 3, input, output.createItem(meta));
		item = output;
		this.meta = meta;
	}
	
	public ItemInventoryRecipe(SpmodInventoryItem output, int meta, ItemStack[] input, int x, int y)
	{
		super(x, y, input, output.createItem(meta));
		item = output;
		this.meta = meta;
	}
	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting)
	{
		return item.createItem(meta);
	}
	
	
	
	
	
}
