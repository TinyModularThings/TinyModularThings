package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IconRegister;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
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
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.blockIcon = par1IconRegister.registerIcon(SpmodAPILib.ModID.toLowerCase()+":flowers/IngoFlower");
	}
	
	
}
