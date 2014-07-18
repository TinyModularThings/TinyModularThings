package speiger.src.spmodapi.common.world.retrogen;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.blocks.AdvancedPosition;
import speiger.src.api.blocks.BlockStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class RetroGenTickHandler
{
	
	private static RetroGenTickHandler ticks = new RetroGenTickHandler();
	
	public static RetroGenTickHandler getTicks()
	{
		return ticks;
	}
	
	public HashMap<Integer, ArrayList<ChunkProsition>> chunks = new HashMap<Integer, ArrayList<ChunkProsition>>();
	
	@SubscribeEvent
	public void tickEnd(TickEvent.WorldTickEvent event)
	{
		if (event.side != Side.SERVER || event.phase != TickEvent.Phase.END)
		{
			return;
		}

		World world = event.world;
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
		
	}

	public static class OreReplacer
	{
		BlockStack block;
		int delay;
		int cuDelay = 0;
		AdvancedPosition pos;
		
		public OreReplacer(BlockStack block, int delay, AdvancedPosition pos)
		{
			this.block = block;
			this.delay = delay;
			this.pos = pos;
		}
		
		public boolean generate()
		{
			if(DimensionManager.getWorld(pos.dim).isRemote)
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
				return pos.setBlock(block.getBlock(), block.getMeta());
			}
		}
		
	}
	
}
