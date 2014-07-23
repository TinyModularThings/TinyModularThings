package speiger.src.spmodapi.common.util;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;

public class BlockData
{
	private static HashMap<BlockProsition, NBTTagCompound> blocks = new HashMap<BlockProsition, NBTTagCompound>();
	
	public static void addData(BlockProsition block, NBTTagCompound data)
	{
		blocks.put(block, data);
	}
	
	public static NBTTagCompound getOrCreateData(BlockProsition par0)
	{
		NBTTagCompound nbt = blocks.remove(par0);
		if (nbt == null)
		{
			nbt = new NBTTagCompound();
		}
		return nbt;
	}
	
	public static class BlockProsition
	{
		int xCoord;
		int yCoord;
		int zCoord;
		int dimID;
		
		public BlockProsition(int dim, int x, int y, int z)
		{
			dimID = dim;
			xCoord = x;
			yCoord = y;
			zCoord = z;
		}
	}
}
