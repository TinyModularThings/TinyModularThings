package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import speiger.src.tinymodularthings.common.blocks.storage.ItemBlockStorage;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class TinyBarrelRecipe extends ShapedOreRecipe
{
	
	public TinyBarrelRecipe()
	{
		super(new ItemStack(TinyBlocks.storageBlock, 1, 4), new Object[]{"XXX", "XYX", "XXX", 'X', "gearWood", 'Y', new ItemStack(TinyBlocks.storageBlock, 1, 4)});
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		int meta = 0;
		boolean found = false;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack != null && stack.itemID == TinyBlocks.storageBlock.blockID && stack.getItemDamage() == 4)
			{
				meta = NBTHelper.getTag(stack, "BarrelMeta").getInteger("Metadata");
				found = true;
				break;
			}
		}
		if(!found || meta >= 8)
		{
			return null;
		}
		return ItemBlockStorage.createTinyBarrel(meta + 1);
	}
	
	@Override
	public int getRecipeSize()
	{
		return 9;
	}
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemBlockStorage.createTinyBarrel(0);
	}
	
}
