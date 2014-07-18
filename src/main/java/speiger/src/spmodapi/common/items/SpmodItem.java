package speiger.src.spmodapi.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public abstract class SpmodItem extends Item implements LanguageItem
{
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	@Override
	public abstract String getDisplayName(ItemStack par1, SpmodMod par0);
	
	public SpmodMod getMod()
	{
		return SpmodAPI.instance;
	}
	
	public String getModID()
	{
		return SpmodAPILib.ModID.toLowerCase();
	}
	
}
