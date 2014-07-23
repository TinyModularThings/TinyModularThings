package speiger.src.spmodapi.common.creativeTabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.items.crafting.ItemGear;
import speiger.src.spmodapi.common.items.crafting.ItemGear.GearType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	public ItemStack getIconItemStack()
	{
		return ItemGear.getGearFromType(GearType.Redstone);
	}
}
