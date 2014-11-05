package speiger.src.spmodapi.common.creativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TabHemp extends CreativeTabs
{
	
	public TabHemp()
	{
		super("hempTab");
		// setBackgroundImageName("item_search.png");
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
		return "Hemp Items";
	}
	
	@Override
	public ItemStack getIconItemStack()
	{
		return new ItemStack(APIItems.hemp);
	}
	
}
