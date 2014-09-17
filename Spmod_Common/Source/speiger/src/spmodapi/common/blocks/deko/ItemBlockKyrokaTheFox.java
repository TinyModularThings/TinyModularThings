package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;

public class ItemBlockKyrokaTheFox extends ItemBlock implements LanguageItem
{

	public ItemBlockKyrokaTheFox(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}

	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), "kyroka", par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, 0)), "kyroka", par0);
	}
	
}
