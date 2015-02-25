package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.tinymodularthings.common.blocks.storage.ItemBlockStorage;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class TinyBarrelRecipe extends ShapedRecipes
{

	public TinyBarrelRecipe()
	{
		super(3, 3, new ItemStack[]{GearType.Wood.getItem(), GearType.Wood.getItem(), GearType.Wood.getItem(),
								   GearType.Wood.getItem(), new ItemStack(TinyBlocks.storageBlock, 1, 4), GearType.Wood.getItem(),
								   GearType.Wood.getItem(), GearType.Wood.getItem(), GearType.Wood.getItem()}, new ItemStack(TinyBlocks.storageBlock, 1, 4));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1)
	{
		int meta = 0;
		for(int i = 0;i<par1.getSizeInventory();i++)
		{
			ItemStack stack = par1.getStackInSlot(i);
			if(stack != null && stack.itemID == TinyBlocks.storageBlock.blockID && stack.getItemDamage() == 4)
			{
				meta = NBTHelper.getTag(stack, "BarrelMeta").getInteger("Metadata");
				break;
			}
		}
		if(meta >= 9)
		{
			return null;
		}
		ItemStack result = ItemBlockStorage.createTinyBarrel(meta+1);
		return result;
	}
	
	
	
}
