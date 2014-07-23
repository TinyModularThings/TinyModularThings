package speiger.src.tinymodularthings.common.items.core;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.TinyModularThings;

public abstract class ItemBlockTinyChest extends ItemBlock implements
		LanguageItem
{
	
	public ItemBlockTinyChest(int par1)
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
	
}
