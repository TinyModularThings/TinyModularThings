package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;

public class RedstoneCable extends SpmodItem
{
	
	public RedstoneCable(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabCrafing);
	}

	
}
