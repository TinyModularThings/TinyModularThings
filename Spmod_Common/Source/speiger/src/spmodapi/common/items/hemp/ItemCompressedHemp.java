package speiger.src.spmodapi.common.items.hemp;

import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;

public class ItemCompressedHemp extends SpmodItem
{
	
	public ItemCompressedHemp(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabHemp);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Compressed Hemp";
	}
	
}
