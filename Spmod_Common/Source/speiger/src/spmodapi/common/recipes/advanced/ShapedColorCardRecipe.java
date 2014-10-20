package speiger.src.spmodapi.common.recipes.advanced;

import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedColorCardRecipe extends ShapedOreRecipe implements IRecipe
{

	public ShapedColorCardRecipe(ItemStack result, Object...recipe)
	{
		super(result, recipe);
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean match = super.matches(inv, world);
		
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack item = inv.getStackInSlot(i);
			if(item != null && item.itemID == APIItems.colorCard.itemID)
			{
				match = false;
			}
		}
		
		return match;
	}
	
	
	
}
