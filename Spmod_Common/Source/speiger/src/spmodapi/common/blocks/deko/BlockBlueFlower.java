package speiger.src.spmodapi.common.blocks.deko;

import java.lang.reflect.Field;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;

import com.jcraft.jorbis.Block;

import cpw.mods.fml.common.FMLLog;
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
