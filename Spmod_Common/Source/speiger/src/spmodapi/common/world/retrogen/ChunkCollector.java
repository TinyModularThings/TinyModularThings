package speiger.src.spmodapi.common.world.retrogen;

import java.util.*;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;
import speiger.src.api.common.world.gen.ISpmodWorldGen;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class ChunkCollector
{
	
	private static long genHash = 0L;
	public static HashSet<Integer> dimBlackList = new HashSet<Integer>();
	private static ChunkCollector collect = new ChunkCollector();
	private static ArrayList<IWorldGenerator> generator = new ArrayList<IWorldGenerator>();
	
	public static ChunkCollector getInstance()
	{
		return collect;
	}
	
	@ForgeSubscribe
	public void onChunkSave(ChunkDataEvent.Save par1)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		if (SpmodConfig.booleanInfos.get("Retrogen"))
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
			feature = (nbt.getLong("Features") != genHash) && (SpmodConfig.booleanInfos.get("Retrogen"));
		}
		
		ChunkProsition chunk = new ChunkProsition(par1.getChunk());
		
		if (nbt == null && SpmodConfig.booleanInfos.get("Retrogen") && !par1.getData().getBoolean("SpmodAPI.Retrogen"))
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
	
	public void loadConfig()
	{
		SpmodConfig spmodConfig = SpmodConfig.getInstance();
		Configuration config = spmodConfig.getConfiguration();
		try
		{
			Set<IWorldGenerator> data = CodeProxy.getField(Set.class, new GameRegistry(), 1);
			String cResult = config.get(spmodConfig.getRetrogenCategorie(), "WorldGenerator", "", String.format("%s%n%s", "A Feature that allow to retrogen things from Other Mods. Simpy putin the Name and Split it with ':', Here is a possible list of WorldGenerators: ", CodeProxy.getListAsSimpleName(data))).getString();
			cResult+=":";
			List<String> result = Arrays.asList(cResult.split(":"));
			for(IWorldGenerator cu : data)
			{
				if(result.contains(cu.getClass().getSimpleName()))
				{
					generator.add(cu);
					addHash(cu.getClass().getSimpleName().hashCode());
				}
			}
		}
		catch(Exception e)
		{
		}
	}
	
	public void registerRetrogen(ISpmodWorldGen gen)
	{
		genHash += gen.getName().hashCode();
	}
	
	public void addHash(int amount)
	{
		genHash += amount;
	}
	
	public void generateWorld(Random rand, int chunkX, int chunkZ, World world, boolean retro)
	{
		if (retro && (!SpmodConfig.booleanInfos.get("Retrogen")))
		{
			return;
		}
		SpmodWorldGen.getWorldGen().generate(rand, chunkX, chunkZ, world, retro);
		if (!retro)
		{
			world.getChunkFromChunkCoords(chunkX, chunkZ).setChunkModified();
		}
		if(retro)
		{
			for(IWorldGenerator gen : generator)
			{
				gen.generate(rand, chunkX, chunkZ, world, world.getChunkProvider(), world.getChunkProvider());
			}
		}
	}
	
}
