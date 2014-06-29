package speiger.src.spmodapi.common.util;

import java.util.Arrays;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.blocks.BlockStack;

public class BlockPosition
{
	// World
	public World worldID;
	
	// xCoord
	public int xCoord;
	
	// yCoord
	public int yCoord;
	
	// zCoord
	public int zCoord;
	
	public ForgeDirection facing;
	
	public BlockPosition(int dimID, int x, int y, int z)
	{
		this(DimensionManager.getWorld(dimID), x, y, z);
	}
	
	public BlockPosition(World world, int x, int y, int z, ForgeDirection face)
	{
		this(world, x, y, z);
		facing = face;
	}
	
	public BlockPosition(World world, int x, int y, int z)
	{
		this(x, y, z);
		worldID = world;
	}
	
	public BlockPosition(int x, int y, int z)
	{
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}
	
	public BlockStack getAsBlockStack()
	{
		return new BlockStack(worldID, xCoord, yCoord, zCoord);
	}
	
	public List<Integer> getAsList()
	{
		return Arrays.asList(worldID.provider.dimensionId, xCoord, yCoord, zCoord);
	}
	
	public boolean doesBlockExsist()
	{
		return getBlockID() > 0;
	}
	
	public int getBlockMetadata()
	{
		return getWorld().getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	public int getBlockID()
	{
		return getWorld().getBlockId(xCoord, yCoord, zCoord);
	}
	
	public boolean isThisBlock(BlockStack block, boolean meta)
	{
		if (meta)
		{
			return getBlockID() == block.getBlockID() && getBlockMetadata() == block.getMeta();
		}
		else
		{
			return getBlockID() == block.getBlockID();
		}
	}
	
	public boolean hasTileEntity()
	{
		return getTileEntity() != null;
	}
	
	public int getXCoord()
	{
		return xCoord;
	}
	
	public int getYCoord()
	{
		return yCoord;
	}
	
	public int getZCoord()
	{
		return zCoord;
	}
	
	public ForgeDirection getFacing()
	{
		return facing;
	}
	
	public TileEntity getTileEntity()
	{
		return getWorld().getBlockTileEntity(xCoord, yCoord, zCoord);
	}
	
	public World getWorld()
	{
		return worldID;
	}

	public void remove()
	{
		worldID.setBlockToAir(xCoord, yCoord, zCoord);
	}
}
