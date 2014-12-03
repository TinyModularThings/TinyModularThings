package speiger.src.spmodapi.client.render.core;

import speiger.src.spmodapi.common.util.TextureEngine;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlocksSpmod extends RenderBlocks
{
	Icon[] overrideIcons = new Icon[6];
	boolean overrides = false;
	
	public RenderBlocksSpmod(RenderBlocks par1)
	{
		super(par1.blockAccess);
	}
	
	public RenderBlocksSpmod(IBlockAccess par1)
	{
		super(par1);
	}
	
	public void setOverrideIcon(Icon...icons)
	{
		overrides = true;
		for(int i = 0;i<overrideIcons.length;i++)
		{
			overrideIcons[i] = icons[i];
		}
	}
	
	public void clearOverrideIcons()
	{
		for(int i = 0;i<overrideIcons.length;i++)
		{
			overrideIcons[i] = null;
		}
		overrides = false;
	}

	@Override
	public Icon getBlockIcon(Block par1Block, IBlockAccess par2iBlockAccess, int par3, int par4, int par5, int par6)
	{
		if(overrides)
		{
			return TextureEngine.getTextures().getIconSafe(overrideIcons[par6]);
		}
		return super.getBlockIcon(par1Block, par2iBlockAccess, par3, par4, par5, par6);
	}

	@Override
	public Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3)
	{
		if(overrides)
		{
			return TextureEngine.getTextures().getIconSafe(overrideIcons[par2]);
		}
		return super.getBlockIconFromSideAndMetadata(par1Block, par2, par3);
	}

	@Override
	public Icon getBlockIconFromSide(Block par1Block, int par2)
	{
		if(overrides)
		{
			return TextureEngine.getTextures().getIconSafe(overrideIcons[par2]);
		}
		return super.getBlockIconFromSide(par1Block, par2);
	}
	
	
	
	
}
