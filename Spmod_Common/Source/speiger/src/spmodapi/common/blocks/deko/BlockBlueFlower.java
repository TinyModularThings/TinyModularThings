package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.BlockFlower;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBlueFlower extends BlockFlower
{
	
	public BlockBlueFlower(int par1)
	{
		super(par1);
		setHardness(0.0F);
		setStepSound(soundGrassFootstep);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TextureEngine.getTextures().getTexture(this, 0);
	}
	
	
	
}
