package speiger.src.tinymodularthings.common.interfaces;

import net.minecraft.item.ItemStack;

public interface IUpgradeRecipeHelper
{
	//Upgrading Block
	//That simply means when you upgrade the space or something.
	
	public void handleUpgrade(ItemStack recipeOutput, int upgrade);
	
	public void handleDownGrade(ItemStack recipeOutput, int downgrade);
	
	public boolean canBeUpgraded(ItemStack recipeItem, int upgrade);
	
	public boolean canBeDowngraded(ItemStack recipeItem, int downgrade);
	
	//Transforming Block
	//That simply that you change a block but the upgradelevel should be the same
	public void handleResult(ItemStack result, ItemStack input);
}
