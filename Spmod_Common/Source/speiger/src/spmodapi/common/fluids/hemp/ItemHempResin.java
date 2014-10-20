package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.items.DisplayItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHempResin extends SpmodItem
{
	
	public ItemHempResin(int par1)
	{
		super(par1);
	}
	
	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if (!SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		LanguageRegister.getLanguageName(new DisplayItem(id), "hemp.resin", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(new DisplayItem(par1.itemID), "hemp.resin", par0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getSpriteNumber()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(APIBlocks.fluidHempResin);
	}
	
	

	
	
}
