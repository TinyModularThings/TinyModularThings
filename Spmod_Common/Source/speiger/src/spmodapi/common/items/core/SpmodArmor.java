package speiger.src.spmodapi.common.items.core;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.util.TextureEngine;

import com.google.common.base.Strings;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SpmodArmor extends ItemArmor
{
    public EnumRarity rarity;

	public SpmodArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial, int par3, int par4)
	{
		super(par1, par2EnumArmorMaterial, par3, par4);
	}

	@Override
	public String getItemDisplayName(ItemStack par1)
	{
		String name = getName(par1);
		if(Strings.isNullOrEmpty(name))
		{
			return "No Name";
		}
		return name;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack)
	{
		if(rarity != null)
		{
			return rarity;
		}
		return super.getRarity(par1ItemStack);
	}

	public SpmodArmor setRarity(EnumRarity par1)
	{
		rarity = par1;
		return this;
	}
	
	public abstract String getName(ItemStack par1);

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, par1);
	}
	
	
	
}
