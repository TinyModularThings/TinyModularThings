package speiger.src.spmodapi.common.items.hemp;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHempBucket extends ItemBucket
{
	
	public ItemHempBucket(int par1)
	{
		super(par1, APIBlocks.fluidHempResin.blockID);
		this.setCreativeTab(APIUtils.tabHemp);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int par1)
	{
		return TextureEngine.getTextures().getTexture(this, 0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack)
	{
		return "Hemp Resin Bucket";
	}
	
	
}
