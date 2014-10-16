package speiger.src.spmodapi.common.blocks.cores;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.util.TextureEngine;

public class SpmodBlockBase extends Block implements ITextureRequester
{

	public SpmodBlockBase(int par1, Material par2Material)
	{
		super(par1, par2Material);
	}
	
	
	public void registerTextures(TextureEngine par1)
	{
		
	}
	
	public boolean onTextureAfterRegister(TextureEngine par1)
	{
		return true;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TextureEngine.getTextures().getTexture(this);
	}
	
	
	
	
}
