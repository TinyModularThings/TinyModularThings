package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.util.Icon;
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
