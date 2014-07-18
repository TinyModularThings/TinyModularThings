package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.items.DisplayStack;
import speiger.src.api.items.InfoStack;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHempDekoBase extends ItemBlock implements LanguageItem
{
	String[] names = new String[]{
		"hemp.base",
		"hemp.brick",
		"hemp.base.nice",
		"hemp.brick.nice"
	};
	public ItemBlockHempDekoBase(int par1)
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
		return LanguageRegister.getLanguageName(new BlockStack(par1), names[par1.getItemDamage() % names.length], par0);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(!SpmodModRegistry.areModsEqual(par0, SpmodAPI.instance))
		{
			return;
		}
		for(int i = 0;i<names.length;i++)
		{
			ItemStack par1 = new ItemStack(id, 1, i);
			LanguageRegister.getLanguageName(new BlockStack(par1), names[par1.getItemDamage()], par0);
		}
		LanguageRegister.getLanguageName(new InfoStack(), "monster.spawn.not", par0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if(par1ItemStack.getItemDamage() >= 4)
		{
			par3List.add(LanguageRegister.getLanguageName(new InfoStack(), "monster.spawn.not", SpmodAPI.instance));
		}
	}
	
	
	
	
	
}
