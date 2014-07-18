package speiger.src.spmodapi.common.creativeTabs;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TabHempDeko extends CreativeTabs
{

	public TabHempDeko()
	{
		super("Hemp Deko");
		setBackgroundImageName("item_search.png");
	}
	
	@Override
	public boolean hasSearchBar()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return LanguageRegister.getLanguageName(this, "hemp.deko", SpmodAPI.instance);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(APIBlocks.hempBlock);
	}
	
	
	
}
