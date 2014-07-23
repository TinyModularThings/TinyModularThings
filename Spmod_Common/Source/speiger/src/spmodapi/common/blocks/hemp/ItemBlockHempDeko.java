package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempDeko.HempBlockInformation;
import speiger.src.spmodapi.common.enums.EnumColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHempDeko extends ItemBlock implements LanguageItem
{
	
	public String[] colorsnames = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "Light Gray", "Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White" };
	
	public ItemBlockHempDeko(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
	public HempBlockInformation getHempInfo(BlockStack par1)
	{
		return ((BlockHempDeko) par1.getBlock()).getInfos();
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		HempBlockInformation infos = getHempInfo(new BlockStack(par1));
		return LanguageRegister.getLanguageName(new InfoStack(), EnumColor.values()[par1.getItemDamage()].getName(), par0)+ " "+ LanguageRegister.getLanguageName(new BlockStack(par1), "hemp." + infos.getName(), par0);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		
		HempBlockInformation infos = getHempInfo(new BlockStack(id));
		for (int i = 0; i < colorsnames.length; i++)
		{
			LanguageRegister.getLanguageName(new BlockStack(id, i), "hemp." + infos.getName(), par0);
		}
		LanguageRegister.getLanguageName(new InfoStack(), "monster.spawn.not", par0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1, EntityPlayer par2EntityPlayer, List par3, boolean par4)
	{
		HempBlockInformation info = getHempInfo(new BlockStack(par1));
		if (!info.canMonsterSpawn())
		{
			par3.add(LanguageRegister.getLanguageName(new InfoStack(), "monster.spawn.not", SpmodAPI.instance));
		}
	}
	
}
