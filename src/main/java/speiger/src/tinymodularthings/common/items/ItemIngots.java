package speiger.src.tinymodularthings.common.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.common.items.core.TinyItem;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.utils.TinyTextureHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemIngots extends TinyItem
{
	
	String[] ingots = new String[] { "ingotCopper", "ingotTin", "ingotAluminium", "ingotSilver", "ingotLead", "ingotBronze", "ingotIridium" };
	
	IIcon[] ingotTextures = new IIcon[ingots.length];
	
	public ItemIngots()
	{
		setHasSubtypes(true);
		setMaxStackSize(64);
		setCreativeTab(CreativeTabs.tabFood);
		OreDictionary.registerOre("ingotCopper", new ItemStack(this, 1, 0));
		OreDictionary.registerOre("ingotTin", new ItemStack(this, 1, 1));
		OreDictionary.registerOre("ingotSilver", new ItemStack(this, 1, 3));
		OreDictionary.registerOre("ingotLead", new ItemStack(this, 1, 4));
		OreDictionary.registerOre("ingotBronze", new ItemStack(this, 1, 5));
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod Start)
	{
		return LanguageRegister.getLanguageName(new DisplayStack(par1), ingots[par1.getItemDamage()], Start);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < ingots.length; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public void registerItems(Item id, SpmodMod par0)
	{
		if (!par0.getName().equals(TinyModularThingsLib.Name))
		{
			return;
		}
		for (int i = 0; i < ingots.length; i++)
		{
			LanguageRegister.getLanguageName(new DisplayStack(new ItemStack(id, 1, i)), ingots[i], par0);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int par1)
	{
		return ingotTextures[par1];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1)
	{
		ingotTextures[0] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotCopper"));
		ingotTextures[1] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotTin"));
		ingotTextures[2] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotAluminium"));
		ingotTextures[3] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotSilver"));
		ingotTextures[4] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotLead"));
		ingotTextures[5] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotBronze"));
		ingotTextures[6] = par1.registerIcon(TinyTextureHelper.getTextureStringFromName("ingots/IngotIridium"));
	}
	
}
