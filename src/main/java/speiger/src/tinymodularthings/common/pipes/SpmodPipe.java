package speiger.src.tinymodularthings.common.pipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.tinymodularthings.TinyModularThings;
import buildcraft.core.CreativeTabBuildCraft;
import buildcraft.transport.ItemPipe;

public class SpmodPipe extends ItemPipe implements LanguageItem
{
	String languageName;
	public SpmodPipe(String name)
	{
		super(CreativeTabBuildCraft.PIPES);
		languageName = name;
	}
	@Override
	public String getItemStackDisplayName(ItemStack itemstack)
	{
		return getDisplayName(itemstack, TinyModularThings.instance); 
	}
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), this.languageName, par0);
	}
	@Override
	public void registerItems(Item id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, TinyModularThings.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), this.languageName, par0);
	}
	
	
	
}
