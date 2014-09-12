package speiger.src.api.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;
import buildcraft.api.power.IPowerReceptor;

public class WorldReading
{
	public static boolean hasTileEntity(World world, int x, int y, int z)
	{
		return world.getBlockTileEntity(x, y, z) != null;
	}
	
	public static boolean hasInventory(World world, int x, int y, int z)
	{
		return hasTileEntity(world, x, y, z) && world.getBlockTileEntity(x, y, z) instanceof IInventory;
	}
	
	public static boolean hasTank(World world, int x, int y, int z)
	{
		return hasTileEntity(world, x, y, z) && world.getBlockTileEntity(x, y, z) instanceof IFluidHandler;
	}
	
	public static boolean hasPowerProvider(World world, int x, int y, int z)
	{
		return hasTileEntity(world, x, y, z) && (world.getBlockTileEntity(x, y, z) instanceof IEnergyProvider || world.getBlockTileEntity(x, y, z) instanceof IPowerReceptor);
	}
	
	public static IInventory getInventory(World world, int x, int y, int z)
	{
		return ((IInventory) world.getBlockTileEntity(x, y, z));
	}
	
	public static IFluidHandler getFluidTank(World world, int x, int y, int z)
	{
		return ((IFluidHandler) world.getBlockTileEntity(x, y, z));
	}
	
	public static int getBlockId(World par0, int par1, int par2, int par3, int side)
	{
		if (side == 0)
		{
			return par0.getBlockId(par1, par2 - 1, par3);
		}
		else if (side == 1)
		{
			return par0.getBlockId(par1, par2 + 1, par3);
		}
		else if (side == 2)
		{
			return par0.getBlockId(par1, par2, par3 - 1);
		}
		else if (side == 3)
		{
			return par0.getBlockId(par1, par2, par3 + 1);
		}
		else if (side == 4)
		{
			return par0.getBlockId(par1 - 1, par2, par3);
		}
		else
		{
			return par0.getBlockId(par1 + 1, par2, par3);
		}
	}
	
	public static TileEntity getTileEntity(World par0, int par1, int par2, int par3, int side)
	{
		if (side == 0)
		{
			return par0.getBlockTileEntity(par1, par2 - 1, par3);
		}
		else if (side == 1)
		{
			return par0.getBlockTileEntity(par1, par2 + 1, par3);
		}
		else if (side == 2)
		{
			return par0.getBlockTileEntity(par1, par2, par3 - 1);
		}
		else if (side == 3)
		{
			return par0.getBlockTileEntity(par1, par2, par3 + 1);
		}
		else if (side == 4)
		{
			return par0.getBlockTileEntity(par1 - 1, par2, par3);
		}
		else
		{
			return par0.getBlockTileEntity(par1 + 1, par2, par3);
		}
	}
	
	public static int getLookingDirectionFromEnitty(EntityLivingBase par0)
	{
		int direction = -1;
		int facing = MathHelper.floor_double(par0.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		int var8 = Math.round(par0.rotationPitch);
		if (var8 > 57)
		{
			direction = ForgeDirection.UP.ordinal();
		}
		else if (var8 < -57)
		{
			direction = ForgeDirection.DOWN.ordinal();
		}
		else
		{
			if (facing == 0)
			{
				direction = ForgeDirection.NORTH.ordinal();
			}
			else if (facing == 1)
			{
				direction = ForgeDirection.EAST.ordinal();
			}
			else if (facing == 2)
			{
				direction = ForgeDirection.SOUTH.ordinal();
			}
			else if (facing == 3)
			{
				direction = ForgeDirection.WEST.ordinal();
			}
		}
		return direction;
	}
	
	public static ArrayList<TileEntity> getTileWithAABB(int xCoord, int yCoord, int zCoord, World world, int range, int blockID, int meta)
	{
		ArrayList<TileEntity> tile = new ArrayList<TileEntity>();
		int minX = xCoord - range;
		int minY = yCoord - range;
		int minZ = zCoord - range;
		int maxX = xCoord + range;
		int maxY = yCoord + range;
		int maxZ = zCoord + range;
		for (int x = minX; x < maxX; x++)
		{
			for (int y = minY; y < maxY; y++)
			{
				for (int z = minZ; z < maxZ; z++)
				{
					BlockPosition pos = new BlockPosition(world, x, y, z);
					if (pos.doesBlockExsist() && pos.isThisBlock(new BlockStack(blockID, meta), true) && pos.hasTileEntity())
					{
						tile.add(pos.getTileEntity());
					}
				}
			}
		}
		
		return tile;
	}
	
	public static AdvTile getAdvTile(IBlockAccess par1, int par2, int par3, int par4)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
		if (tile == null)
		{
			return null;
		}
		if (!(tile instanceof AdvTile))
		{
			return null;
		}
		
		return (AdvTile) tile;
	}
	
	public static boolean isBlockBlocked(World world, int xCoord, int yCoord, int zCoord, boolean optional)
	{
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			ForgeDirection cu = ForgeDirection.VALID_DIRECTIONS[i];
			int x = xCoord + cu.offsetX;
			int y = yCoord + cu.offsetY;
			int z = zCoord + cu.offsetZ;
			int id = world.getBlockId(x, y, z);
			Block block = Block.blocksList[id];
			if (block == null)
			{
				return false;
			}
			if (block.isAirBlock(world, x, y, z))
			{
				return false;
			}
			if (!block.isOpaqueCube() && !optional)
			{
				return false;
			}
			
		}
		return true;
	}
	
	public static boolean isSorunded(IBlockAccess world, int xCoord, int yCoord, int zCoord, Class<? extends TileEntity>... tile)
	{
		
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			ForgeDirection cu = ForgeDirection.getOrientation(i);
			int x = xCoord + cu.offsetX;
			int y = yCoord + cu.offsetY;
			int z = zCoord + cu.offsetZ;
			int id = world.getBlockId(x, y, z);
			Block block = Block.blocksList[id];
			if (block == null)
			{
				return false;
			}
			
			if (world.getBlockTileEntity(x, y, z) != null)
			{
				
				TileEntity tiles = world.getBlockTileEntity(x, y, z);
				if (tiles != null)
				{
					String name = tiles.getClass().getSimpleName();
					boolean tileMatch = false;
					for (Class<? extends TileEntity> key : tile)
					{
						if (name.equalsIgnoreCase(key.getSimpleName()))
						{
							tileMatch = true;
							break;
						}
					}
					if (!tileMatch)
					{
						return false;
					}
				}
			}
			else
			{
				if (world.isAirBlock(x, y, z))
				{
					return false;
				}
				if (!block.isOpaqueCube())
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	public static ForgeDirection[] getHDirections()
	{
		return new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };
	}
	
	public static ForgeDirection[] getVDirections()
	{
		return new ForgeDirection[] { ForgeDirection.DOWN, ForgeDirection.UP };
	}
}
