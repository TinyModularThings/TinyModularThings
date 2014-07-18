package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHempDekoBase extends Block
{

	public BlockHempDekoBase()
	{
		super(Material.cloth);
		this.setCreativeTab(APIUtils.tabHempDeko);
	}
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta >= 4)
		{
			if(type == EnumCreatureType.monster)
			{
				return false;
			}
		}
		return true;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0 ? 100 : 0;
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0 ? 50 : 0;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0;
	}
	
	IIcon[] textures = new IIcon[4];
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		int meta = par2 % 4;
		return textures[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		for(int i = 0;i<4;i++)
		{
			textures[i] = par1IconRegister.registerIcon(SpmodAPILib.ModID.toLowerCase()+":hemp/HempBase_"+i);
		}
	}

	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for(int i = 0;i<8;i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	
	
}
