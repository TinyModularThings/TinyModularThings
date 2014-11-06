package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMultiPlate extends ItemBlock
{
	
	public ItemBlockMultiPlate(int par1)
	{
		super(par1);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return "Multi Plate";
	}
	
}
