package speiger.src.compactWindmills.common.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import speiger.src.api.tiles.IWindmill;

public class WindmillFake implements IWindmill
{
	public static WindmillFake fake = new WindmillFake();
	
	@Override
	public TileEntity getWindmill()
	{
		return null;
	}
	
	@Override
	public ChunkCoordinates getChunkCoordinates()
	{
		return null;
	}
	
	@Override
	public int getTier()
	{
		return 0;
	}
	
	@Override
	public int getRadius()
	{
		return 0;
	}
	
	@Override
	public void distroyRotor()
	{
		
	}
	
	@Override
	public short getFacing()
	{
		return -1;
	}
	
}
