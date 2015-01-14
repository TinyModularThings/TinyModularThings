package speiger.src.spmodapi.common.items.core;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.PathProxy;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SpmodItem extends Item implements ITextureRequester
{
	
	public SpmodItem(int par1)
	{
		super(par1);
	}
	
	public SpmodMod getMod()
	{
		return SpmodAPI.instance;
	}
	
	public String getModID()
	{
		return SpmodAPILib.ModID.toLowerCase();
	}
	
	public ItemStack getMetaLessItem()
	{
		return new ItemStack(this, 1, PathProxy.getRecipeBlankValue());
	}

	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		String name = "";
		try
		{
			name = getName(par1ItemStack);
		}
		catch(Exception e)
		{
		}
		if(Strings.isNullOrEmpty(name))
		{
			name = "No Name";
		}
		return name;
	}
	
	public abstract String getName(ItemStack par1);

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
