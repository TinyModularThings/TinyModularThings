package speiger.src.spmodapi.common.world.retrogen;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class RetroGenTickHandler implements ITickHandler
{
	
	private static RetroGenTickHandler ticks = new RetroGenTickHandler();
	
	public static RetroGenTickHandler getTicks()
	{
		return ticks;
	}
	
	public HashMap<Integer, ArrayList<ChunkProsition>> chunks = new HashMap<Integer, ArrayList<ChunkProsition>>();
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		World world = (World) tickData[0];
		int dim = world.provider.dimensionId;
		ArrayList<ChunkProsition> chunk = chunks.get(Integer.valueOf(dim));
		
		if (chunk != null && chunk.size() > 0)
		{
			ChunkProsition c = chunk.get(0);
			long worldSeed = world.getSeed();
			Random rand = new Random(worldSeed);
			long xSeed = rand.nextLong() >> 3;
			long zSeed = rand.nextLong() >> 3;
			rand.setSeed(xSeed * c.chunkX + zSeed * c.chunkZ ^ worldSeed);
			ChunkCollector.getInstance().generateWorld(rand, c.chunkX, c.chunkZ, world, true);
			chunk.remove(0);
			chunks.put(Integer.valueOf(dim), chunk);
		}
		
		AdvancedRetrogen gen = AdvancedRetrogen.getInstance();
		if(gen != null)
		{
			gen.onWorldTick(world);
		}
		
	}
	
	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.WORLD);
	}
	
	@Override
	public String getLabel()
	{
		return "SpmodAPI_Retorgen";
	}
	
	public static class OreReplacer
	{
		BlockStack block;
		int delay;
		int cuDelay = 0;
		BlockPosition pos;
		
		public OreReplacer(BlockStack block, int delay, BlockPosition pos)
		{
			this.block = block;
			this.delay = delay;
			this.pos = pos;
		}
		
		public boolean generate()
		{
			if (DimensionManager.getWorld(pos.getDimensionID()).isRemote)
			{
				return false;
			}
			if (delay > cuDelay)
			{
				cuDelay++;
				return false;
			}
			else
			{
				return pos.setBlock(block);
			}
		}
		
	}
	
}
