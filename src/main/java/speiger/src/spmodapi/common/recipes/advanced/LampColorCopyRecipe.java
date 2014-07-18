package speiger.src.spmodapi.common.recipes.advanced;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagByte;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;

public class LampColorCopyRecipe extends ShapedRecipes
{
	ItemStack item;
	public LampColorCopyRecipe(ItemStack lampType)
	{
		super(3, 3, new ItemStack[]{lampType, new ItemStack(APIItems.colorCard), lampType, new ItemStack(APIItems.colorCard), lampType, new ItemStack(APIItems.colorCard), lampType, new ItemStack(APIItems.colorCard), lampType}, lampType);
		item = lampType;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		ItemStack end = item.copy();
		end.stackSize = 5;
		end.setTagInfo("Recipe", new NBTTagByte((byte)1));
		return end;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1)
	{
		ItemStack stack = par1.getStackInSlot(4);
		ItemStack end = stack.copy();
		end.stackSize = 5;
		return end;
	}
	
	
	
	
	
}
