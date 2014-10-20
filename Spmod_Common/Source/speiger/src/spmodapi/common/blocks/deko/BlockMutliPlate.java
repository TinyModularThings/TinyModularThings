package speiger.src.spmodapi.common.blocks.deko;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockMutliPlate extends BlockContainer
{
	
	public BlockMutliPlate(int par1)
	{
		super(par1, Material.cloth);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new MultiPlate();
	}
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
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
	
	public void onNeighborBlockChange(World par0, int par1, int par2, int par3, int par5)
	{
		MultiPlate tile = (MultiPlate) par0.getBlockTileEntity(par1, par2, par3);
		if (tile != null)
		{
			int facing = tile.getFacing();
			boolean drop = false;
			if (facing == ForgeDirection.DOWN.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1, par2 + 1, par3, ForgeDirection.UP))
				{
					drop = true;
				}
			}
			if (facing == ForgeDirection.UP.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1, par2 - 1, par3, ForgeDirection.DOWN))
				{
					drop = true;
				}
			}
			if (facing == ForgeDirection.EAST.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1 - 1, par2, par3, ForgeDirection.WEST))
				{
					drop = true;
				}
			}
			if (facing == ForgeDirection.WEST.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1 + 1, par2, par3, ForgeDirection.EAST))
				{
					drop = true;
				}
			}
			if (facing == ForgeDirection.NORTH.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1, par2, par3 + 1, ForgeDirection.SOUTH))
				{
					drop = true;
				}
			}
			if (facing == ForgeDirection.SOUTH.ordinal())
			{
				if (!par0.isBlockSolidOnSide(par1, par2, par3 - 1, ForgeDirection.NORTH))
				{
					drop = true;
				}
			}
			
			if (drop)
			{
				par0.setBlock(par1, par2, par3, 0);
				if (!par0.isRemote)
				{
					// this.dropBlockAsItem_do(par0, par1, par2, par3,
					// APIItems.multiSign.getItemStackFromString(tile.getName()));
				}
				par0.markTileEntityForDespawn(tile);
			}
		}
		
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		int meta = ((MultiPlate) par1iBlockAccess.getBlockTileEntity(par2, par3, par4)).getFacing();
		float i = 0.055F;
		if (meta == 0)
		{
			this.setBlockBounds(0.0F, (1F - i), 0.0F, 1.0F, 1.0F, 1.0F);
		}
		else if (meta == 1)
		{
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + i, 1.0F);
		}
		else if (meta == 2)
		{
			setBlockBounds(0.0F, 0.0F, (1F - i), 1.0F, 1.0F, 1.0F);
		}
		else if (meta == 3)
		{
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + i);
		}
		else if (meta == 4)
		{
			setBlockBounds((1F - i), 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
		else if (meta == 5)
		{
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + i, 1.0F, 1.0F);
		}
		else
		{
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			
		}
	}
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		int meta = ((MultiPlate) par1World.getBlockTileEntity(par2, par3, par4)).getFacing();
		double i = 0.055D;
		if (meta == 0)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3 + (1D - i), par4, par2 + 1, par3 + 1, par4 + 1);
		}
		else if (meta == 1)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2 + 1, par3 + i, par4 + 1);
		}
		else if (meta == 2)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4 + (1D - i), par2 + 1, par3 + 1, par4 + 1);
		}
		else if (meta == 3)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2 + 1, par3 + 1, par4 + i);
		}
		else if (meta == 4)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2 + (1D - i), par3, par4, par2 + 1, par3 + 1, par4 + 1);
		}
		else if (meta == 5)
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2 + i, par3 + 1, par4 + 1);
		}
		else
		{
			return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2 + 1, par3 + 1, par4 + 1);
			
		}
		
	}
	
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
	{
		ForgeDirection dir = ForgeDirection.getOrientation(par5);
		return (dir == ForgeDirection.DOWN && par1World.isBlockSolidOnSide(par2, par3 + 1, par4, ForgeDirection.DOWN)) || (dir == ForgeDirection.UP && par1World.isBlockSolidOnSide(par2, par3 - 1, par4, ForgeDirection.UP)) || (dir == ForgeDirection.NORTH && par1World.isBlockSolidOnSide(par2, par3, par4 + 1, ForgeDirection.NORTH)) || (dir == ForgeDirection.SOUTH && par1World.isBlockSolidOnSide(par2, par3, par4 - 1, ForgeDirection.SOUTH)) || (dir == ForgeDirection.WEST && par1World.isBlockSolidOnSide(par2 + 1, par3, par4, ForgeDirection.WEST)) || (dir == ForgeDirection.EAST && par1World.isBlockSolidOnSide(par2 - 1, par3, par4, ForgeDirection.EAST));
	}
	
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		return null;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		if (par5.isSneaking())
		{
			MultiPlate hanf = (MultiPlate) par1.getBlockTileEntity(par2, par3, par4);
			if (hanf != null)
			{
				hanf.setRotation(hanf.setNextRotation());
				return true;
				
			}
			return false;
		}
		return true;
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}
	
}
