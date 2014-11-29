package speiger.src.spmodapi.common.items.gas;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.items.core.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;

public class ItemGasCell extends SpmodItem
{
	boolean big;
	public ItemGasCell(int par1, boolean par2)
	{
		super(par1);
		big = par2;
	}

	@Override
	public String getName(ItemStack par1)
	{
		return big ? "Compressed Gas Cell" : "Gas Cell";
	}
	
	
	
}
