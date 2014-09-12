package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.transport.ItemPipe;

public class SpmodPipe extends ItemPipe implements LanguageItem
{
	String languageName;
	
	public SpmodPipe(int i, String name)
	{
		super(i);
		languageName = name;
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack)
	{
		return getDisplayName(itemstack, TinyModularThings.instance);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), this.languageName, par0);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), this.languageName, par0);
	}
	
}
