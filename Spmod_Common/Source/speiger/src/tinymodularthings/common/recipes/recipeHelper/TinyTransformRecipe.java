package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.tinymodularthings.common.interfaces.IUpgradeRecipeHelper;

public class TinyTransformRecipe extends ShapedOreRecipe
{
	int slotID;
	IUpgradeRecipeHelper helper;
	public TinyTransformRecipe(ItemStack result, IUpgradeRecipeHelper par2, int slotToCheck, Object... recipe)
	{
		super(result, recipe);
		slotID = slotToCheck;
		helper = par2;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		ItemStack result = super.getCraftingResult(var1).copy();
		ItemStack input = var1.getStackInSlot(slotID);
		helper.handleResult(result, input);
		return result;
	}
	
	
}
