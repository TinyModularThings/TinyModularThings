package speiger.src.spmodapi.common.creativeTabs;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TabCrafting extends CreativeTabs
{

	public TabCrafting()
	{
		super("Crafting");
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
		return LanguageRegister.getLanguageName(this, "spmod.crafting", SpmodAPI.instance);
	}
	
	@Override
	public Item getTabIconItem()
	{
		return ItemGear.getGearFromType(GearType.Redstone).getItem();
	}
}
