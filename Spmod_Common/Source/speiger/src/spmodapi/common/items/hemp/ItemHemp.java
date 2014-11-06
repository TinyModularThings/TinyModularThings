package speiger.src.spmodapi.common.items.hemp;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;

public class ItemHemp extends SpmodItem
{
	
	public ItemHemp(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabHemp);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Hemp";
	}
	
}
