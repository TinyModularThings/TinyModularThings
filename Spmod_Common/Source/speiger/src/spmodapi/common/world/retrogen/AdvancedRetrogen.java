package speiger.src.spmodapi.common.world.retrogen;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class AdvancedRetrogen
{
	static AdvancedRetrogen load = new AdvancedRetrogen(SpmodConfig.getInstance());
	
	private ArrayList<IWorldGenerator> allowedClasses = new ArrayList<IWorldGenerator>();
	private HashMap<Integer, ArrayList<ChunkData>> todoList = new HashMap<Integer, ArrayList<ChunkData>>();
	private HashSet<Integer> added = new HashSet<Integer>();
	
	private SpmodConfig spmodConfig;
	
	private boolean work = false;
	private boolean configWork = false;
	private int delay = 20;
	private int totalShutdownDelay = 600;
	
	public static AdvancedRetrogen getInstance()
	{
		if(load.canTick())
		{
			return load;
		}
		return null;
	}
	
	private AdvancedRetrogen(SpmodConfig par1)
	{
		spmodConfig = par1;
		loadPreConfig();
	}
	
	public void loadPreConfig()
	{
		Configuration config = spmodConfig.getConfiguration();
		try
		{
			ConfigBoolean canWork = new ConfigBoolean(spmodConfig.getRetrogenCategorie(), "Advanced Retrogen", false).setComment("Directly Retrogens Every Chunk in the World. NOTE: Extreme Laggy but do not create new Chunks just retrogens the exsisting ones. ANOTHER NOTE: IT CAN DISTRO/CURRUPT WORLD SO BECAREFULL!");
			work = canWork.getResult(config);
			canWork.changeResult(config, false);
			int dd = Integer.parseInt(config.get(spmodConfig.general, "Advanced Retrogen Speed", 20, "The Advanced Retrogen Proccess a lot of Chunks thats why its running slow. 1 Chunk Per Sek. You can Change the Speed if you want. Number = ticks it waits 20 ticks = 1 sec. Because its loading Every Chunk in the World. IMPORTANT INFORMATION: As Faster you retrogenerate the Bigger the chance is that a World Currupts. So Slower make it better! 1 Seconds should be save").getString());
			if(dd <= 0)
			{
				dd = 1;
			}
			delay = dd;
			if(work)
			{
				SpmodConfig.retogen = true;
			}
			configWork = work;
		}
		catch(Exception e)
		{
		}
		finally
		{
			config.save();
		}
	}
	
	public static void onAfterConfig()
	{
		load.work = true;
	}
	
	public static void setRetrogenState(boolean par1)
	{
		load.work = par1;
	}
	
	public void initConfig()
	{
		Configuration config = spmodConfig.getConfiguration();
		try
		{
			Set<IWorldGenerator> list = CodeProxy.getField(Set.class, new GameRegistry(), 1);
			String added = config.get(spmodConfig.getRetrogenCategorie(), "Retrogen List", "", String.format("%s%n%s", "set yourself the classes which should be in the advanced Retrogen. List of all Possible World generators, Split it with ':', NOTE: do not add anything if you have no clue about it!: ", CodeProxy.getListAsSimpleName(list))).getString();
			added+=":";
			List<String> result = Arrays.asList(added.split(":"));
			for(IWorldGenerator gen : list)
			{
				if(result.contains(gen.getClass().getSimpleName()))
				{
					if(!allowedClasses.contains(gen))
					{
						allowedClasses.add(gen);
					}
				}
			}
			work = configWork;
			if(allowedClasses.size() <= 0)
			{
				work = false;
			}
		}
		catch(Exception e)
		{
			FMLLog.getLogger().info("Problem: ");
			for(StackTraceElement el : e.getStackTrace())
			{
				FMLLog.getLogger().info(""+el);
			}
		}
		finally
		{
			config.save();
		}
	}
	
	public void initDimensions(World world)
	{
		File folder = new File(getRegionPathFromID(world.provider.dimensionId));
		File worldFile = new File(getDimensionPathFromID(world.provider.dimensionId));
		if(folder != null && folder.exists() && folder.isDirectory())
		{
			File[] fileArray = folder.listFiles();
			if(fileArray != null)
			{
				for(File file : fileArray)
				{
					String name = file.getName();
					name = name.substring(2);
					int size = name.length();
					name = name.substring(0, size-4);
					
					String key = name.substring(0, name.indexOf("."));
					String value = name.substring(name.indexOf(".")+1);
					
					int[] xz = new int[]{Integer.parseInt(key), Integer.parseInt(value)};
					loadChunks(new AnvilChunkLoader(worldFile), world, xz);
				}
			}
		}
	}
	
	public void loadChunks(AnvilChunkLoader loader, World world, int[] chunkSizes)
	{
		int xCoord = chunkSizes[0] << 5;
		int zCoord = chunkSizes[1] << 5;
		try
		{
			for(int x = xCoord;x<xCoord+32;x++)
			{
				for(int z = zCoord;z<zCoord+32;z++)
				{
					Chunk chunk = loader.loadChunk(world, x, z);
					if(chunk != null)
					{
						addChunk(loader, world, chunk);
					}
				}
			}
		}
		catch(Exception e)
		{
			FMLLog.getLogger().info("Could not load Chunks for Retrogen: x:"+xCoord+" z:"+zCoord);
		}
	}
	
	public void addChunk(AnvilChunkLoader loader, World world, Chunk chunk)
	{
		if(!todoList.containsKey(world.provider.dimensionId))
		{
			todoList.put(world.provider.dimensionId, new ArrayList<ChunkData>());
		}
		todoList.get(world.provider.dimensionId).add(new ChunkData(loader, chunk, world));
	}
	
	public String getRegionPathFromID(int id)
	{
		MinecraftServer server = MinecraftServer.getServer();
		String path = "";
		if(!server.isDedicatedServer())
		{
			path += "saves/";
		}
		path += server.getFolderName()+"/";
		if(id != 0)
		{
			path+= "DIM"+id+"/";
		}
		path+="region";
		
		return path;
	}
	
	public String getDimensionPathFromID(int id)
	{
		MinecraftServer server = MinecraftServer.getServer();
		String path = "";
		if(!server.isDedicatedServer())
		{
			path += "saves/";
		}
		path += server.getFolderName()+"/";
		if(id != 0)
		{
			path+= "DIM"+id;
		}
		return path;
	}
	
	@ForgeSubscribe
	public void onWorldLoad(WorldEvent.Load par1)
	{
		if(configWork)
		{
			if(!added.contains(par1.world.provider.dimensionId))
			{
				added.add(par1.world.provider.dimensionId);
				initDimensions(par1.world);
			}
		}
	}
	
	@ForgeSubscribe
	public void onWorldUnload(WorldEvent.Unload par1)
	{
		todoList.remove(par1.world.provider.dimensionId);
	}
	
	public HashMap<Integer, ArrayList<ChunkData>> getData()
	{
		return todoList;
	}
	
	public void onWorldTick(World world)
	{
		if(world.getWorldTime() % delay == 0)
		{
			ArrayList<ChunkData> chunks = todoList.get(world.provider.dimensionId);
			if(chunks != null && chunks.size() > 0)
			{
				ChunkData data = chunks.remove(0);
				Chunk c = data.getChunk();
				Random rand = new Random(world.getSeed());
				long xSeed = rand.nextLong() >> 3;
				long zSeed = rand.nextLong() >> 3;
				rand.setSeed(xSeed * c.xPosition + zSeed * c.zPosition ^ world.getSeed());
				for(IWorldGenerator gen : allowedClasses)
				{
					gen.generate(rand, c.xPosition, c.zPosition, world, world.getChunkProvider(), world.getChunkProvider());
				}
				data.saveChunk(world);
				todoList.put(world.provider.dimensionId, chunks);
				FMLLog.getLogger().info("Retrogened Chunk: X:"+c.xPosition+" Z:"+c.zPosition);
			}
			else if(chunks != null)
			{
				todoList.remove(world.provider.dimensionId);
			}
			
			if(todoList.isEmpty())
			{
				totalShutdownDelay--;
				if(totalShutdownDelay == 0)
				{
					FMLLog.getLogger().info("Server Finished Retrogen. Its shutting down the Server to dissable the Retrogen.");
					MinecraftServer.getServer().getConfigurationManager().func_110459_a(LangProxy.getText("Finished Total World Retrogen. Shutting down Server now. This is a Requirement!", EnumChatFormatting.DARK_RED), true);
					MinecraftServer.getServer().initiateShutdown();
				}
			}
			else
			{
				totalShutdownDelay = 600;
			}
		}
	}
	
	public boolean canTick()
	{
		return work;
	}
	
	public static class ChunkData
	{
		AnvilChunkLoader par1;
		Chunk par2;
		World par3;
		
		public ChunkData(AnvilChunkLoader loader, Chunk chunk, World world)
		{
			par1 = loader;
			par2 = chunk;
			par3 = world;
		}
		
		public Chunk getChunk()
		{
			return par2;
		}
		
		public void saveChunk(World world)
		{
			try
			{
				par1.saveChunk(world, par2);
			}
			catch(Exception e)
			{
			}
		}
	}

	public static boolean getConfigWork()
	{
		return load.configWork;
	}

	public int getTickSpeed()
	{
		return delay;
	}
}
