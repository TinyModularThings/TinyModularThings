package speiger.src.compactWindmills.common.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import speiger.src.api.common.world.tiles.interfaces.IWindmill;

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
	public void distroyRotor()
	{
		
	}
	
	@Override
	public short getFacing()
	{
		return -1;
	}

	@Override
	public boolean isFake()
	{
		return true;
	}

	@Override
	public void setNewSpeed(float amount)
	{
	}

	@Override
	public float getWindSpeed()
	{
		return 0;
	}

	@Override
	public float getActualSpeed()
	{
		return 0;
	}
	
}
