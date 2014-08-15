package speiger.src.api.blocks;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class AdvancedPosition
{
	public int dim;
	public int xCoord;
	public int yCoord;
	public int zCoord;
	
	public AdvancedPosition(World par1, int x, int y, int z)
	{
		this(par1.provider.dimensionId, x, y, z);
	}
	
	public AdvancedPosition(int dim, int x, int y, int z)
	{
		this.dim = dim;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}
	
	public boolean setBlock(int block, int meta)
	{
		return DimensionManager.getWorld(dim).setBlock(xCoord, yCoord, zCoord, block, meta, 3);
	}
	
}
