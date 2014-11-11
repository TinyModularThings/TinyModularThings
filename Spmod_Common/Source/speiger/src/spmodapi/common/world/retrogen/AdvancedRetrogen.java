package speiger.src.spmodapi.common.world.retrogen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import speiger.src.spmodapi.common.world.retrogen.AdvancedRetrogen.ChunkData;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class AdvancedRetrogen implements ITickHandler
{
	static AdvancedRetrogen load = new AdvancedRetrogen();
	
	private ArrayList<IWorldGenerator> allowedClasses = new ArrayList<IWorldGenerator>();
	private HashSet<Integer> added = new HashSet<Integer>();
	private HashMap<Integer, ArrayList<ChunkData>> todoList = new HashMap<Integer, ArrayList<ChunkData>>();
	
	private SpmodConfig spmodConfig;
	
	private boolean work = false;
	private boolean paused = false;
	private int delay = 20;
	private int totalShutdownDelay = 2000;
	
	public static AdvancedRetrogen getInstance()
	{
		return load;
	}
	
	private AdvancedRetrogen()
	{
		spmodConfig = SpmodConfig.getInstance();
	}
	
	public void loadConfig()
	{
		Configuration config = spmodConfig.getConfiguration();
		try
		{
			ConfigBoolean canWork = new ConfigBoolean(spmodConfig.getRetrogenCategorie(), "Full World Retrogen", false).setComment("Directly Retrogens Every Chunk in the World. NOTE: Extreme Laggy but do not create new Chunks just retrogens the exsisting ones. ANOTHER NOTE: IT CAN DISTRO/CURRUPT WORLD SO BECAREFULL!");
			work = canWork.getResult(config);
			canWork.changeResult(config, false);
			int dd = Integer.parseInt(config.get(spmodConfig.general, "Full World Retrogen Speed", 20, "The Advanced Retrogen Proccess a lot of Chunks thats why its running slow. 1 Chunk Per Sek. You can Change the Speed if you want. Number = ticks it waits 20 ticks = 1 sec. Because its loading Every Chunk in the World. IMPORTANT INFORMATION: As Faster you retrogenerate the Bigger the chance is that a World Currupts. So Slower make it better! 1 Seconds should be save").getString());
			if(dd <= 0)
			{
				dd = 1;
			}
			delay = dd;
			Set<IWorldGenerator> list = CodeProxy.getField(Set.class, new GameRegistry(), 1);
			String added = config.get(spmodConfig.getRetrogenCategorie(), "Retrogen List", "", String.format("%s%n%s", "set yourself the classes which should be in the advanced Retrogen. List of all Possible World generators, Split it with ':', NOTE: do not add anything if you have no clue about it!: ", CodeProxy.getListAsSimpleName(list))).getString() + ":";
			List<String> result = Arrays.asList(added.split(":"));
			for(IWorldGenerator gen : list)
			{
				if(result.contains(gen.getClass().getSimpleName()) && !allowedClasses.contains(gen))
				{
					allowedClasses.add(gen);
				}
			}
			if(allowedClasses.size() <= 0)
			{
				work = false;
			}
		}
		catch(Exception e)
		{
			SpmodAPI.log.print("Problem: "+e);
			for(StackTraceElement el : e.getStackTrace())
			{
				SpmodAPI.log.print(""+el);
			}
		}
		finally
		{
			config.save();
		}
		
		if(work)
		{
			MinecraftForge.EVENT_BUS.register(this);
			TickRegistry.registerTickHandler(this, Side.SERVER);
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
					name = name.substring(2, name.length()-4);
					String key = name.substring(0, name.indexOf("."));
					String value = name.substring(name.indexOf(".")+1);
					addChunkFile(world, new ChunkData(new AnvilChunkLoader(worldFile), Integer.parseInt(key), Integer.parseInt(value)));
				}
			}
		}
	}
	
	public void addChunkFile(World world, ChunkData par1)
	{
		if(!todoList.containsKey(world.provider.dimensionId))
		{
			todoList.put(world.provider.dimensionId, new ArrayList<ChunkData>());
		}
		todoList.get(world.provider.dimensionId).add(par1);
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
		if(work)
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
	
	public Chunk getChunk(World world, ChunkData data)
	{
		try
		{
			if(data.hasNextChunk())
			{
				return data.getNextChunk(world);
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{

	}
	
	public void setAdvancedRetrogenState(boolean par1)
	{
		paused = par1;
	}
	
	public boolean doesRetrogenWork()
	{
		return work;
	}
	
	public int getTickSpeed()
	{
		return delay;
	}
	
	public HashMap<Integer, ArrayList<ChunkData>> getData()
	{
		return todoList;
	}
	
	public void retrogenerateChunk(World world, Chunk chunk)
	{
		for(IWorldGenerator gen : allowedClasses)
		{
			gen.generate(world.rand, chunk.xPosition, chunk.zPosition, world, world.getChunkProvider(), world.getChunkProvider());
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		if(paused)
		{
			return;
		}
		
		World world = (World)tickData[0];
		boolean flag = delay == 1;
		
		if(world.getWorldTime() % delay == 0 || flag)
		{
			ArrayList<ChunkData> list = todoList.get(world.provider.dimensionId);
			if(list != null)
			{
				if(list.size() > 0)
				{
					ChunkData data = list.get(0);
					Chunk target = getChunk(world, data);
					if(target != null)
					{
						this.retrogenerateChunk(world, target);
						data.saveChunk(world, target);
					}
					else if(!data.hasNextChunk())
					{
						list.remove(0);
					}
				}
				else
				{
					todoList.remove(world.provider.dimensionId);
				}
			}
		}
		
		if(world.getWorldTime() % 200 == 0)
		{
			SpmodAPI.log.print("World: "+world.provider.getDimensionName()+" Has still "+(todoList.get(world.provider.dimensionId) != null ? getSizeFromList(todoList.get(world.provider.dimensionId)) : "0") + " Chunks to Retrogenerate");
		}
		
		if(todoList.isEmpty() || todoList.size() <= 0)
		{
			if(this.totalShutdownDelay == 2000)
			{
				MinecraftServer.getServer().getConfigurationManager().func_110459_a(LangProxy.getText("Chunk Retrogening is Finish initing Shutdown", EnumChatFormatting.DARK_RED), false);
			}
			this.totalShutdownDelay--;
			if(totalShutdownDelay <= 0)
			{
				MinecraftServer.getServer().initiateShutdown();
			}
		}
		else
		{
			this.totalShutdownDelay = 2000;
		}
	}
	
	public int getSizeFromList(ArrayList<ChunkData> data)
	{
		if(data == null)
		{
			return 0;
		}
		int count = 0;
		for(ChunkData cu : data)
		{
			count += cu.getSize();
		}
		return count;
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return "AdvancedRetrogen";
	}
	
	
	
	
	
	public static class ChunkData
	{
		AnvilChunkLoader loader;
		int xCoord;
		int zCoord;
		int xOffset;
		int zOffset;
		
		public ChunkData(AnvilChunkLoader par1, int x, int z)
		{
			loader = par1;
			xCoord = x << 5;
			zCoord = z << 5;
			xOffset = 0;
			zOffset = 0;
		}
		
		public boolean hasNextChunk()
		{
			return xOffset <= 31 && zOffset <= 31;
		}
		
		public Chunk getNextChunk(World world) throws IOException
		{
			for(;xOffset < 32;xOffset++)
			{
				for(;zOffset < 32;zOffset++)
				{
					Chunk chunk = loader.loadChunk(world, xCoord + xOffset, zCoord + zOffset);
					if(chunk != null)
					{
						return chunk;
					}
				}
				if(zOffset >= 31 && xOffset < 31)
				{
					zOffset = 0;
				}
			}
			return null;
		}
		
		public int getSize()
		{
			return (31 - xOffset) * (31 - zOffset);
		}
		
		public void saveChunk(World world, Chunk chunk)
		{
			try
			{
				loader.saveChunk(world, chunk);
			}
			catch(Exception e)
			{
			}
		}
	}

}
