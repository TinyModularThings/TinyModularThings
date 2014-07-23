package speiger.src.spmodapi.common.blocks.deko;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.spmodapi.common.blocks.deko.TileLamp.EnumLampType;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.enums.EnumColor;
import speiger.src.spmodapi.common.enums.EnumColor.SpmodColor;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLightDeko extends BlockContainer
{

	double[][] sizes = new double[][]{
			{0.125D, 0D, 0.125D, 0.875D, 0.735D, 0.875D},
			{0.125D, 0.265D, 0.125D, 0.875D, 1D, 0.875D},
			{0.125D, 0.125D, 0D, 0.875D, 0.875D, 0.735D},
			{0.125D, 0.125D, 0.265D, 0.875D, 0.875D, 1D},
			{0D, 0.125D, 0.125D, 0.735D, 0.875D, 0.875D},
			{0.265D, 0.125D, 0.125D, 1D, 0.875D, 0.875D}
	};
	
	public BlockLightDeko(int par1)
	{
		super(par1, Material.glass);
		setCreativeTab(APIUtils.tabHempDeko);
		this.setHardness(4.0F);
		this.setResistance(4.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileLamp();
	}


	
	

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return super.getBlockHardness(par1World, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof TileLamp && ((TileLamp)tile).getType() != null)
		{
			TileLamp lamp = (TileLamp) tile;
			EnumLampType type = lamp.getType();
			int facing = lamp.getFacing();
			switch(type)
			{
				case FULL: return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2+1, par3+1, par4+1);
				case RP2CAGELAMP:

					double[] array = sizes[facing];
					return AxisAlignedBB.getAABBPool().getAABB(par2+array[0], par3+array[1], par4+array[2], par2+array[3], par3+array[4], par4+array[5]);
				default:
					break;
			}
		}
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof TileLamp && ((TileLamp)tile).getType() != null)
		{
			TileLamp lamp = (TileLamp) tile;
			EnumLampType type = lamp.getType();
			int facing = lamp.getFacing();
			
			switch(type)
			{
				case FULL: return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2+1, par3+1, par4+1);
				case RP2CAGELAMP:
					double[] array = sizes[facing];
					return AxisAlignedBB.getAABBPool().getAABB(par2+array[0], par3+array[1], par4+array[2], par2+array[3], par3+array[4], par4+array[5]);
				default:
					break;
			}
		}
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		TileEntity tile = par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof TileLamp)
		{
			TileLamp lamp = (TileLamp) tile;
			EnumLampType type = lamp.getType();
			int facing = lamp.getFacing();
			if(type == EnumLampType.RP2CAGELAMP)
			{
				switch(facing)
				{
					case 0:
						this.setBlockBounds(0.125F, 0F, 0.125F, 0.875F, 0.735F, 0.875F);
						break;
					case 1:
						this.setBlockBounds(0.125F, 0.265F, 0.125F, 0.875F, 1F, 0.875F);
						break;
					case 2:
						this.setBlockBounds(0.125F, 0.125F, 0F, 0.875F, 0.875F, 0.735F);
						break;
					case 3:
						this.setBlockBounds(0.125F, 0.125F, 0.265F, 0.875F, 0.875F, 1F);
						break;
					case 4:
						this.setBlockBounds(0F, 0.125F, 0.125F, 0.735F, 0.875F, 0.875F);
						break;
					case 5:
						this.setBlockBounds(0.265F, 0.125F, 0.125F, 1F, 0.875F, 0.875F);
						break;
					default:
						this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
				}
			}
			else
			{
				this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			}
			
		}
	}

	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}

	

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}

	@Override
	public int damageDropped(int par1)
	{
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileLamp)
		{
			return ((TileLamp)tile).onDrop(fortune);
		}
		return super.getBlockDropped(world, x, y, z, metadata, fortune);
	}

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileLamp)
		{
			TileLamp lamp = (TileLamp) tile;
			if((lamp.isActive() && !lamp.isInverted()) || (!lamp.isActive() && lamp.isInverted()))
			{
				return 15;
			}
		}
		return 0;
	}

	
	
	@Override
	public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
	{
		return super.shouldCheckWeakPower(world, x, y, z, side);
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof TileLamp)
		{
			TileLamp lamp = (TileLamp) tile;
			EnumLampType type = lamp.getType();
			if(type != null)
			{
				if(type == type.FULL)
				{
					return true;
				}
				else if(type == type.RP2CAGELAMP)
				{
					return side.ordinal() == lamp.getFacing();
				}
			}
		}
		return super.isBlockSolidOnSide(world, x, y, z, side);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile)tile).pickBlock(target);
		}
		return super.getPickBlock(target, world, x, y, z);
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return Block.leaves.getIcon(0, 0);
	}

	@Override
	@SideOnly(Side.CLIENT) // Item Side
	public int getRenderColor(int par1)
	{
		return EnumColor.values()[par1].getAsHex().intValue();
	}

	
	
	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if(!par1World.isRemote)
		{
			TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
			if(tile != null && tile instanceof AdvTile)
			{
				return ((AdvTile)tile).onActivated(par5EntityPlayer);
			}
		}
		return super.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, par6, par7, par8, par9);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		TileEntity tile = par1iBlockAccess.getBlockTileEntity(par2, par3, par4);
		if(tile != null && tile instanceof TileLamp)
		{
			SpmodColor color = ((TileLamp)tile).getFullColor();
			if(color != null)
			{
				return color.getHex();
			}
			
		}
		return EnumColor.WHITE.getAsHex().intValue();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<72;i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	
	
	
}
