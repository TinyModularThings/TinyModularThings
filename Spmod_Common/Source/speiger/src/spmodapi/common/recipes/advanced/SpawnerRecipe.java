package speiger.src.spmodapi.common.recipes.advanced;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import speiger.src.api.common.registry.recipes.IRecipeOverride;

public class SpawnerRecipe extends ShapelessOreRecipe implements IRecipeOverride
{
	
	public SpawnerRecipe()
	{
		super(new ItemStack(Block.mobSpawner, 1, 90), new ItemStack(Item.monsterPlacer, 1, Short.MAX_VALUE), new ItemStack(Block.mobSpawner, 1, Short.MAX_VALUE));
	}

	@Override
	public boolean preventOverriding()
	{
		return true;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1)
	{
		int meta = -1;
		for(int i = 0;i<par1.getSizeInventory();i++)
		{
			ItemStack item = par1.getStackInSlot(i);
			if(item != null && item.itemID == Item.monsterPlacer.itemID)
			{
				meta = item.getItemDamage();
				break;
			}
		}
		if(meta == -1)
		{
			return null;
		}
		return new ItemStack(Block.mobSpawner, 1, meta);
	}
	
	
	
}
