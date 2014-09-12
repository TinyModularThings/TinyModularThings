package speiger.src.tinymodularthings.common.items.core;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public abstract class TinyItem extends Item implements LanguageItem
{
	
	public TinyItem(int par1)
	{
		super(par1);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, TinyModularThings.instance);
	}
	
	@Override
	public abstract String getDisplayName(ItemStack par1, SpmodMod par0);
	
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
	
	public String getModID()
	{
		return TinyModularThingsLib.ModID.toLowerCase();
	}
	
}
