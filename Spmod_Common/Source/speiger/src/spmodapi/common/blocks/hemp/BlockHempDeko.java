package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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

public class BlockHempDeko extends SpmodBlockBase
{
	HempBlockInformation info;
	
	public BlockHempDeko(int par1, HempBlockInformation par2)
	{
		super(par1, par2.getMaterial());
		info = par2;
		setCreativeTab(APIUtils.tabHempDeko);
		par2.registerToForge(this);
		this.setRightClickDoesNothing();
		if(!par2.canMonsterSpawn())
			setMonsterSpawnSave();
	}
	
	public HempBlockInformation getInfos()
	{
		return info;
	}
	
	@Override
	public void registerTextures(TextureEngine par1)
	{
		String[] textures = new String[16];
		for(int i = 0;i<16;i++)
		{
			textures[i] = info.getTexture()+"_"+i;
		}
		par1.registerTexture(this, textures);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TextureEngine.getTextures().getTexture(this, par2);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		return info.isHemp() ? 100 : 0;
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
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 16; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		return info.isHemp() ? 50 : 0;
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		return info.isHemp();
	}
	
	public static class HempBlockInformation
	{
		Material material;
		boolean axe;
		String name;
		String textureName;
		boolean isSave;
		
		public HempBlockInformation(Material par1, boolean axe, boolean par2, String textureName, String name)
		{
			material = par1;
			this.axe = axe;
			this.textureName = textureName;
			this.name = name;
			isSave = par2;
		}
		
		public boolean canMonsterSpawn()
		{
			return !isSave;
		}
		
		public String getName()
		{
			return name;
		}
		
		public boolean isHemp()
		{
			return axe;
		}
		
		public String getTexture()
		{
			return textureName;
		}
		
		public void registerToForge(Block par1)
		{
			MinecraftForge.setBlockHarvestLevel(par1, axe ? "axe" : "pickaxe", 0);
			par1.setHardness(axe ? 1F : 2F);
			par1.setResistance(axe ? 3F : 8F);
		}
		
		public Material getMaterial()
		{
			return material;
		}
	}
	
}
