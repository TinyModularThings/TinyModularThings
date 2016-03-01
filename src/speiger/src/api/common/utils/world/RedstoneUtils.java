package speiger.src.api.common.utils.world;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneUtils
{
	
	public static boolean isSideRedstonePowered(TileEntity tile, ForgeDirection dir)
	{
		return isSideRedstonePowered(tile, dir, 0);
	}
	
	public static boolean isSideRedstonePowered(World world, int x, int y, int z, ForgeDirection dir)
	{
		return isSideRedstonePowered(world, x, y, z, dir, 0);
	}
	
	public static boolean isSideRedstonePowered(TileEntity tile, ForgeDirection dir, int min)
	{
		return isSideRedstonePowered(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, dir, min);
	}
	
	public static boolean isSideRedstonePowered(World world, int x, int y, int z, ForgeDirection dir, int min)
	{
		return getRedstoneLevel(world, x, y, z, dir) > min;
	}
	
	public static boolean isRedstonePowered(TileEntity tile, List<Integer> exlusion)
	{
		return isRedstonePowered(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, 0, exlusion);
	}
	
	public static boolean isRedstonePowered(TileEntity tile)
	{
		return isRedstonePowered(tile, 0);
	}
	
	public static boolean isRedstonePowered(World world, int x, int y, int z)
	{
		return isRedstonePowered(world, x, y, z, 0);
	}
	
	public static boolean isRedstonePowered(TileEntity tile, int min, List<Integer> exlusion)
	{
		return isRedstonePowered(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, min, exlusion);
	}
	
	public static boolean isRedstonePowered(TileEntity tile, int min)
	{
		return isRedstonePowered(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, min);
	}
	
	public static boolean isRedstonePowered(World world, int x, int y, int z, int min)
	{
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(getRedstoneLevel(world, x, y, z, dir) > min)
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isRedstonePowered(World world, int x, int y, int z, int min, List<Integer> exlusion)
	{
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(exlusion.contains(dir.ordinal()))
			{
				continue;
			}
			if(getRedstoneLevel(world, x, y, z, dir) > min)
			{
				return true;
			}
		}
		return false;
	}
	
	public static int getRedstoneLevel(TileEntity par1)
	{
		return getRedstoneLevel(par1.getWorldObj(), par1.xCoord, par1.yCoord, par1.zCoord);
	}
	
	public static int getRedstoneLevel(World world, int x, int y, int z)
	{
		int min = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			min = Math.min(15, Math.max(min, getRedstoneLevel(world, x, y, z, dir)));
		}
		return min;
	}
	
	public static int getRedstoneLevel(TileEntity tile, ForgeDirection side)
	{
		return getRedstoneLevel(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, side);
	}
	
	public static int getRedstoneLevel(World world, int x, int y, int z, ForgeDirection side)
	{
		return world.getIndirectPowerLevelTo(x+side.offsetX, y+side.offsetY, z+side.offsetZ, side.ordinal());
	}
	
	public static int getRedstoneLevel(TileEntity tile, List<Integer> exlusions)
	{
		return getRedstoneLevel(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, exlusions);
	}
	
	public static int getRedstoneLevel(World world, int x, int y, int z, List<Integer> exlusions)
	{
		int min = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(exlusions.contains(dir.ordinal()))
			{
				continue;
			}
			min = Math.min(15, Math.max(min, getRedstoneLevel(world, x, y, z, dir)));
		}
		return min;
	}
}
