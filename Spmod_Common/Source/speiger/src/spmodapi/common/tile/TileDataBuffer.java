package speiger.src.spmodapi.common.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.core.SafeTimeTracker;

public class TileDataBuffer
{
	
	private int blockID = 0;
	private int meta = 0;
	private TileEntity tile;
	private final SafeTimeTracker tracker = new SafeTimeTracker();
	private final World world;
	final int x, y, z;
	private final boolean loadUnloaded;
	
	public TileDataBuffer(World world, int x, int y, int z, boolean loadUnloaded)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.loadUnloaded = loadUnloaded;
		
		refresh();
	}
	
	public final void refresh()
	{
		tile = null;
		blockID = 0;
		if(!loadUnloaded && !world.blockExists(x, y, z))
		{
			return;
		}
		blockID = world.getBlockId(this.x, this.y, this.z);
		meta = world.getBlockMetadata(this.x, this.y, this.z);
		
		Block block = Block.blocksList[blockID];
		if(block != null && block.hasTileEntity(meta))
		{
			tile = world.getBlockTileEntity(this.x, this.y, this.z);
		}
	}
	
	public void set(int blockID, int meta, TileEntity tile)
	{
		this.blockID = blockID;
		this.tile = tile;
		this.meta = meta;
		tracker.markTime(world);
	}
	
	public int getBlockID()
	{
		if(tracker.markTimeIfDelay(world, 20))
		{
			refresh();
		}
		return blockID;
	}
	
	public int getMeta()
	{
		if(tracker.markTimeIfDelay(world, 20))
		{
			refresh();
		}
		return meta;
	}
	
	public TileEntity getTile()
	{
		if(tracker.markTimeIfDelay(world, 20))
		{
			refresh();
		}
		
		if(tile != null && !tile.isInvalid())
		{
			return tile;
		}
		return null;
	}
	
	public boolean exists()
	{
		if(tile != null && !tile.isInvalid())
		{
			return true;
		}
		return world.blockExists(x, y, z);
	}
	
	public static TileDataBuffer[] makeBuffer(World world, int x, int y, int z, boolean loadUnloaded)
	{
		TileDataBuffer[] buffer = new TileDataBuffer[6];
		for(int i = 0;i < 6;i++)
		{
			ForgeDirection d = ForgeDirection.getOrientation(i);
			buffer[i] = new TileDataBuffer(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, loadUnloaded);
		}
		return buffer;
	}
}
