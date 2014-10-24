package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHempDekoBase extends SpmodBlockBase
{
	
	public BlockHempDekoBase(int par1)
	{
		super(par1, Material.cloth);
		this.setCreativeTab(APIUtils.tabHempDeko);
		MinecraftForge.setBlockHarvestLevel(this, 0, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 2, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 3, "pickaxe", 0);
		this.setMonsterSpawnSave(4);
		this.setMonsterSpawnSave(5);
		this.setMonsterSpawnSave(6);
		this.setMonsterSpawnSave(7);
	}
	
	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4) % 1;
		return meta == 0 ? 1F : 2F;
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0 ? 3F : 8F;
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0 ? 100 : 0;
	}
	
	@Override
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0 ? 50 : 0;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		int meta = world.getBlockMetadata(x, y, z) % 1;
		return meta == 0;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		int meta = par2 % 4;
		return TextureEngine.getTextures().getTexture(this, meta);
	}
	
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return blockID;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		return par1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 8; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
}
