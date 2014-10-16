package speiger.src.tinymodularthings.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTinyItem extends TinyItem
{
	
	String Name;
	
	public ItemTinyItem(int par1, String name)
	{
		super(par1);
		Name = name;
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.getItem()), Name, Start);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), Name, par0);
		
	}
	
}
