package speiger.src.api.common.utils.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class BlockCoord implements ICoord
{
	int x;
	int y;
	int z;
	
	public BlockCoord(ICoord coord)
	{
		this(coord.getX(), coord.getY(), coord.getZ());
	}
	
	public BlockCoord(BlockCoord coord)
	{
		this(coord.x, coord.y, coord.z);
	}
	
	public BlockCoord(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public int getX()
	{
		return x;
	}
	
	@Override
	public int getY()
	{
		return y;
	}
	
	@Override
	public int getZ()
	{
		return z;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof BlockCoord)
		{
			BlockCoord coords = (BlockCoord)obj;
			return coords.x == x && coords.y == y && coords.z == z;
		}
		else if(obj instanceof ICoord)
		{
			ICoord coord = (ICoord)obj;
			return coord.getX() == x && coord.getY() == y && coord.getZ() == z;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return x + y << 8 + z << 16;
	}
	
	public int getDistanceTo(Entity entity)
	{
		return getDistanceTo(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
	}
	
	public int getDistanceTo(TileEntity tile)
	{
		return getDistanceTo(tile.xCoord, tile.yCoord, tile.zCoord);
	}
	
	public int getDistanceTo(int xCoord, int yCoord, int zCoord)
	{
		int newX = x - xCoord;
		int newY = y - yCoord;
		int newZ = z - zCoord;
		if(newX < 0) newX = -newX;
		if(newY < 0) newY = -newY;
		if(newZ < 0) newZ = -newZ;
		
		return newX + newY + newZ;
	}
}
