package speiger.src.tinymodularthings.common.blocks.crafting;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCrafting extends ItemBlock
{

	public ItemBlockCrafting(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		switch(par1ItemStack.getItemDamage())
		{
			case 0: return "Advanced Workbench";
			case 1: return "Oredictionary Crafter";
			default: return "";
		}
	}
	
	
	
}
