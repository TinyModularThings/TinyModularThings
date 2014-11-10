package speiger.src.spmodapi.common.world.retrogen;

import net.minecraft.world.chunk.Chunk;

public class ChunkProsition
{
	int chunkX;
	int chunkZ;
	
	public ChunkProsition(Chunk par1)
	{
		chunkX = par1.xPosition;
		chunkZ = par1.zPosition;
	}
	
	public int getChunkX()
	{
		return chunkX;
	}
	
	public int getChunkZ()
	{
		return chunkZ;
	}

	@Override
	public boolean equals(Object arg0)
	{
		if(arg0 == null)
		{
			return false;
		}
		if(!(arg0 instanceof ChunkProsition))
		{
			return false;
		}
		ChunkProsition chunk = (ChunkProsition)arg0;
		if(chunk.getChunkX() != getChunkX())
		{
			return false;
		}
		if(chunk.getChunkZ() != getChunkZ())
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		return chunkX + chunkZ;
	}
	
	
}
