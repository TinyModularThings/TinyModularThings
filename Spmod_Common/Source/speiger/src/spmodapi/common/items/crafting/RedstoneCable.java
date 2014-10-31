package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.item.ItemStack;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;

public class RedstoneCable extends SpmodItem
{
	
	public RedstoneCable(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabCrafing);
	}

	@Override
	public void registerItems(int par1, SpmodMod par0)
	{
		
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return "Redstone Cable";
	}
	
}
