package speiger.src.tinymodularthings.common.recipes.recipeHelper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.interfaces.IUpgradeRecipeHelper;

public class TinyRecipeHelper implements IUpgradeRecipeHelper
{
	
	@Override
	public void handleUpgrade(ItemStack recipeOutput, int upgrade)
	{
		if(recipeOutput != null)
		{
			int id = recipeOutput.itemID;
			int meta = recipeOutput.getItemDamage();
			if(id == TinyItems.tinyChest.itemID || id == TinyItems.advTinyChest.itemID|| id == TinyItems.tinyTank.itemID 
					|| id == TinyItems.advTinyTank.itemID || id == TinyItems.tinyStorageCart.itemID || id == TinyItems.advTinyStorageCart.itemID)
			{
				recipeOutput.setItemDamage(meta + upgrade);
			}
		}
	}
	
	@Override
	public void handleDownGrade(ItemStack recipeOutput, int downgrade)
	{
		if(recipeOutput != null)
		{
			int id = recipeOutput.itemID;
			int meta = recipeOutput.getItemDamage();
			if(id == TinyItems.tinyChest.itemID || id == TinyItems.advTinyChest.itemID|| id == TinyItems.tinyTank.itemID 
					|| id == TinyItems.advTinyTank.itemID || id == TinyItems.tinyStorageCart.itemID || id == TinyItems.advTinyStorageCart.itemID)
			{
				recipeOutput.setItemDamage(meta - downgrade);
			}
		}
	}
	
	@Override
	public boolean canBeUpgraded(ItemStack recipeItem, int upgrade)
	{
		if(recipeItem != null)
		{
			int id = recipeItem.itemID;
			int meta = recipeItem.getItemDamage();
			if(id == TinyItems.tinyChest.itemID || id == TinyItems.advTinyChest.itemID|| id == TinyItems.tinyTank.itemID 
					|| id == TinyItems.advTinyTank.itemID || id == TinyItems.tinyStorageCart.itemID || id == TinyItems.advTinyStorageCart.itemID)
			{
				return meta + upgrade <= 8;
			}
		}
		return false;
	}
	
	@Override
	public boolean canBeDowngraded(ItemStack recipeItem, int downgrade)
	{
		if(recipeItem != null)
		{
			int id = recipeItem.itemID;
			int meta = recipeItem.getItemDamage();
			if(id == TinyItems.tinyChest.itemID || id == TinyItems.advTinyChest.itemID|| id == TinyItems.tinyTank.itemID 
					|| id == TinyItems.advTinyTank.itemID || id == TinyItems.tinyStorageCart.itemID || id == TinyItems.advTinyStorageCart.itemID)
			{
				return meta - downgrade >= 0;
			}
		}
		return false;
	}

	@Override
	public void handleResult(ItemStack result, ItemStack input)
	{
		if(result != null && input != null)
		{
			int id = result.itemID;
			int inputMeta = input.getItemDamage();
			if(id == TinyItems.advTinyChest.itemID || id == TinyItems.advTinyStorageCart.itemID || id == TinyItems.advTinyTank.itemID)
			{
				result.setItemDamage(inputMeta);
			}
			else if(id == TinyBlocks.storageBlock.blockID && result.getItemDamage() == 4)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("Metadata", inputMeta);
				result.setTagInfo("BarrelMeta", nbt);
			}
		}
	}
	
}
