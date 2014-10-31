package speiger.src.spmodapi.common.blocks.cores;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.interfaces.ITextureRequester;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodBlockBase extends Block implements ITextureRequester
{
	public boolean[] ignoreRightClick = new boolean[16];
	public boolean[] removeBasicDrops = new boolean[16];
	public boolean[] canSilkHarvestme = new boolean[16];
	public boolean[] monsterSpawnSave = new boolean[16];
	public boolean[] isARenderedBlock = new boolean[16];
	public boolean[] ItemNoRightClick = new boolean[16];
	
	public SpmodBlockBase(int par1, Material par2Material)
	{
		super(par1, par2Material);
		this.setHardness(4.0F);
		this.setResistance(4.0F);
	}
	
	public SpmodBlockBase setRightClickDoesNothing()
	{
		for(int i = 0;i<16;i++)
		{
			this.ItemNoRightClick[i] = true;
		}
		return this;
	}
	
	public SpmodBlockBase setRightClickDoesNothing(int meta)
	{
		this.ItemNoRightClick[meta] = true;
		return this;
	}
	
	public SpmodBlockBase setIsAbnormal()
	{
		for(int i = 0;i<16;i++)
		{
			this.isARenderedBlock[i] = true;
		}
		return this;
	}
	
	public SpmodBlockBase setIsAbnormal(int meta)
	{
		this.isARenderedBlock[meta] = true;
		return this;
	}
	
	public SpmodBlockBase dissableDrops()
	{
		for(int i = 0;i<16;i++)
		{
			removeBasicDrops[i] = true;
		}
		return this;
	}
	
	public SpmodBlockBase dissableDrops(int meta)
	{
		this.removeBasicDrops[meta] = true;
		return this;
	}
	
	public SpmodBlockBase setMonsterSpawnSave()
	{
		for(int i = 0;i<16;i++)
		{
			this.monsterSpawnSave[i] = true;
		}
		return this;
	}
	
	public SpmodBlockBase setIgnoreRightClick(int meta)
	{
		ignoreRightClick[meta] = true;
		return this;
	}
	
	public SpmodBlockBase setSilkEffect(int meta)
	{
		canSilkHarvestme[meta] = true;
		return this;
	}
	
	public SpmodBlockBase setMonsterSpawnSave(int meta)
	{
		monsterSpawnSave[meta] = true;
		return this;
	}
	
	public SpmodBlockBase setIgnoreRighClick()
	{
		for(int i = 0;i<16;i++)
		{
			this.ignoreRightClick[i] = true;
		}
		return this;
	}
	
	public SpmodBlockBase setSilkEffect()
	{
		for(int i = 0;i<16;i++)
		{
			this.canSilkHarvestme[i] = true;
		}
		return this;
	}
	
	
		
	public void registerTextures(TextureEngine par1)
	{
		
	}
	
	

	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if(par5.isSneaking() && !this.ignoreRightClick[meta])
		{
			return false;
		}
		if(this.ItemNoRightClick[meta])
		{
			return false;
		}
		
		if(!par1.isRemote)
		{
			int guiID = getGuiIDForMeta(meta);
			if(guiID != -1)
			{
				SpmodMod mod = getModInstance(meta);
				if(mod == null)
				{
					mod = SpmodAPI.instance;
				}
				par5.openGui(mod, guiID, par1, par2, par3, par4);
				return true;
			}
		}
		return false;
	}
	
	public int getGuiIDForMeta(int meta)
	{
		return -1;
	}
	
	public SpmodMod getModInstance(int meta)
	{
		return null;
	}

	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return !this.isARenderedBlock[meta];
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(type == EnumCreatureType.monster && this.monsterSpawnSave[meta])
		{
			return false;
		}
		return super.canCreatureSpawn(type, world, x, y, z);
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return canSilkHarvestme[meta];
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
	}
	
	public float getBlockHardness(int meta)
	{
		return this.blockHardness;
	}
	
	public float getBlockResistance(Entity par1, int meta)
	{
		return this.getExplosionResistance(par1);
	}
	
	
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		if(this.removeBasicDrops[par1])
		{
			return 0;
		}
		return super.idDropped(par1, par2Random, par3);
	}

	@Override
	public int damageDropped(int par1)
	{
		if(this.removeBasicDrops[par1])
		{
			return 0;
		}
		return super.damageDropped(par1);
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random)
	{
		if(this.removeBasicDrops[meta])
		{
			return 0;
		}
		return super.quantityDropped(meta, fortune, random);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> result = super.getBlockDropped(world, x, y, z, metadata, fortune);
		if(this.removeBasicDrops[metadata])
		{
			if(onDrop(metadata, fortune) != null)
			{
				result.addAll(onDrop(metadata, fortune));
			}
		}
		return result;
	}

	public ArrayList<ItemStack> onDrop(int meta, int fortune)
	{
		return null;
	}

	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return getLightOpacity(meta);
	}
	
	public int getLightOpacity(int meta)
	{
		return lightOpacity[this.blockID];
	}
	
	

	

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		int light = getLightValue(world.getBlockMetadata(x, y, z));
		if(light > 0)
		{
			return light;
		}
		return super.getLightValue(world, x, y, z);
	}
	
	public int getLightValue(int meta)
	{
		return 0;
	}
	
	public boolean isSolidOnSide(int meta, int side)
	{
		return true;
	}
	
	

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return isSolidOnSide(meta, side.ordinal());
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return getBlockResistance(par1Entity, meta);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		return getBlockHardness(meta);
	}
	
}
