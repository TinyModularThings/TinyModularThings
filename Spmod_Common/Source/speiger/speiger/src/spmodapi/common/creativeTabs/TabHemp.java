package speiger.src.spmodapi.common.creativeTabs;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class TabHemp extends CreativeTabs
{

	public TabHemp()
	{
		super("hempTab");
//		setBackgroundImageName("item_search.png");
	}

	@Override
	public boolean hasSearchBar()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return LanguageRegister.getLanguageName(this, "hemp", SpmodAPI.instance);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(APIItems.hemp);
	}
	
	
	
}
