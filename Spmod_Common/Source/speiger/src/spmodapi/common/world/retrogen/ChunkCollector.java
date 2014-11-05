package speiger.src.spmodapi.common.world.retrogen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;
import speiger.src.api.common.world.gen.ISpmodWorldGen;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.world.SpmodWorldGen;

public class ChunkCollector
{
	
	private static long genHash = 0L;
	public static HashSet<Integer> dimBlackList = new HashSet<Integer>();
	private static ChunkCollector collect = new ChunkCollector();
	
	public static ChunkCollector getInstance()
	{
		return collect;
	}
	
	@ForgeSubscribe
	public void onChunkSave(ChunkDataEvent.Save par1)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if (SpmodConfig.retogen)
		{
			nbt.setLong("Features", genHash);
		}
		
		par1.getData().setTag("SpmodAPIRetroGen", nbt);
	}
	
	@ForgeSubscribe
	public void onChunkLoad(ChunkDataEvent.Load par1)
	{
		int dim = par1.world.provider.dimensionId;
		
		if (dimBlackList.contains(Integer.valueOf(dim)))
		{
			return;
		}
		boolean regen = false;
		boolean feature = false;
		NBTTagCompound nbt = (NBTTagCompound) par1.getData().getTag("SpmodAPIRetroGen");
		
		if (nbt != null)
		{
			feature = (nbt.getLong("Features") != genHash) && (SpmodConfig.retogen);
		}
		
		ChunkProsition chunk = new ChunkProsition(par1.getChunk());
		
		if (nbt == null && SpmodConfig.retogen && !par1.getData().getBoolean("SpmodAPI.Retrogen"))
		{
			regen = true;
		}
		
		if (feature)
		{
			regen = true;
		}
		if (regen)
		{
			ArrayList<ChunkProsition> chunks = RetroGenTickHandler.getTicks().chunks.get(Integer.valueOf(dim));
			if (chunks == null)
			{
				RetroGenTickHandler.getTicks().chunks.put(Integer.valueOf(dim), new ArrayList<ChunkProsition>());
				chunks = RetroGenTickHandler.getTicks().chunks.get(Integer.valueOf(dim));
			}
			if (chunks != null)
			{
				chunks.add(chunk);
				RetroGenTickHandler.getTicks().chunks.put(Integer.valueOf(dim), chunks);
			}
		}
		
	}
	
	public void registerRetrogen(ISpmodWorldGen gen)
	{
		genHash += gen.getName().hashCode();
	}
	
	public void generateWorld(Random rand, int chunkX, int chunkZ, World world, boolean retro)
	{
		
		if (retro && (!SpmodConfig.retogen))
		{
			return;
		}
		
		SpmodWorldGen.getWorldGen().generate(rand, chunkX, chunkZ, world, retro);
		
		if (!retro)
		{
			world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
		}
		
	}
	
}
