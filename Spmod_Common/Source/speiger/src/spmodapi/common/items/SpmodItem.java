package speiger.src.spmodapi.common.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SpmodItem extends Item implements LanguageItem, ITextureRequester
{
	
	public boolean isMissingTexture = false;

	public SpmodItem(int par1)
	{
		super(par1);
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return getDisplayName(par1ItemStack, SpmodAPI.instance);
	}
	
	@Override
	public abstract String getDisplayName(ItemStack par1, SpmodMod par0);
	
	public SpmodMod getMod()
	{
		return SpmodAPI.instance;
	}
	
	public String getModID()
	{
		return SpmodAPILib.ModID.toLowerCase();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, par1);
	}
	
	public void registerTexture(TextureEngine par1)
	{
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	}

	@Override
	public boolean onTextureAfterRegister(TextureEngine par1)
	{
		return true;
	}
	
}
