package speiger.src.spmodapi.common.recipes.advanced;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

public class ColorCardCleaning extends ShapelessOreRecipe
{
	public ColorCardCleaning(ItemStack item)
	{
		super(new ItemStack(APIItems.colorCard), new ItemStack(APIItems.colorCard, 1, PathProxy.getRecipeBlankValue()), item);
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		ItemStack output = this.getRecipeOutput().copy();
		ItemStack input = null;
		
		for(int i = 0;i<var1.getSizeInventory();i++)
		{
			ItemStack cu = var1.getStackInSlot(i);
			if(cu != null && cu.itemID == APIItems.colorCard.itemID)
			{
				input = cu;
				break;
			}
		}
		output.stackSize = input.stackSize;
		
		return output;
	}
	
	
}
