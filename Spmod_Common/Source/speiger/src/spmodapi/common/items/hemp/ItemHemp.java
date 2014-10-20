package speiger.src.spmodapi.common.items.hemp;

import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;

public class ItemHemp extends SpmodItem
{
	
	public ItemHemp(int par1)
	{
		super(par1);
		this.setCreativeTab(APIUtils.tabHemp);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		
		LanguageRegister.getLanguageName(new DisplayItem(id), "hemp", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), "hemp", par0);
	}
	
}
