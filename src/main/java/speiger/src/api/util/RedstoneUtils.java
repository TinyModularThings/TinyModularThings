package speiger.src.api.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class RedstoneUtils
{
	
	public static boolean isBlockGettingPoweredFromSpecialSide(TileEntity tile, int facing)
	{
		return isBlockGettingPoweredFromSpecailSide(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, facing);
	}
	
	public static boolean isBlockGettingPoweredFromSpecailSide(World par0, int par1, int par2, int par3, int facing)
	{
		ForgeDirection face = ForgeDirection.getOrientation(facing);
		int x = par1 + face.offsetX;
		int y = par2 + face.offsetY;
		int z = par3 + face.offsetZ;
		
		if (par0.getIndirectPowerLevelTo(x, y, z, facing) > 0)
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isBlockGettingPowered(TileEntity tile)
	{
		return isBlockGettingPowered(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public static boolean isBlockGettingPowered(World world, int x, int y, int z)
	{
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			ForgeDirection direction = ForgeDirection.getOrientation(i);
			int xCoord = x + direction.offsetX;
			int yCoord = y + direction.offsetY;
			int zCoord = z + direction.offsetZ;
			if (world.getIndirectPowerLevelTo(xCoord, yCoord, zCoord, i) > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public static int getRotationMatrixForHopper(int facing, int rotation)
	{
		switch (facing)
		{
			case 0:
			{
				switch (rotation)
				{
					case 0:
						return 3;
					case 1:
						return 2;
					case 2:
						return 1;
					case 3:
						return 0;
					case 4:
						return 5;
					case 5:
						return 4;
				}
			}
			case 1:
			{
				switch (rotation)
				{
					case 0:
						return 2;
					case 1:
						return 3;
					case 2:
						return 0;
					case 3:
						return 1;
					case 4:
						return 5;
					case 5:
						return 4;
				}
			}
			case 2:
			{
				switch (rotation)
				{
					case 0:
						return 1;
					case 1:
						return 0;
					case 2:
						return 3;
					case 3:
						return 2;
					case 4:
						return 4;
					case 5:
						return 5;
				}
			}
			case 3:
			{
				switch (rotation)
				{
					case 0:
						return 1;
					case 1:
						return 0;
					case 2:
						return 2;
					case 3:
						return 3;
					case 4:
						return 5;
					case 5:
						return 4;
				}
			}
			case 4:
			{
				switch (rotation)
				{
					case 0:
						return 1;
					case 1:
						return 0;
					case 2:
						return 5;
					case 3:
						return 4;
					case 4:
						return 3;
					case 5:
						return 2;
				}
			}
			case 5:
			{
				switch (rotation)
				{
					case 0:
						return 1;
					case 1:
						return 0;
					case 2:
						return 4;
					case 3:
						return 5;
					case 4:
						return 2;
					case 5:
						return 3;
				}
			}
		}
		
		return 0;
	}
}
