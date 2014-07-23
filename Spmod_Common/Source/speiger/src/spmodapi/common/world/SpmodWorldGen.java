package speiger.src.spmodapi.common.world;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.api.world.ISpmodWorldGen;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.world.gen.BlueFlowerGen;
import speiger.src.spmodapi.common.world.retrogen.ChunkCollector;
import cpw.mods.fml.common.IWorldGenerator;

public class SpmodWorldGen implements IWorldGenerator
{
	
	private static SpmodWorldGen init = new SpmodWorldGen();
	
	public static SpmodWorldGen getWorldGen()
	{
		return init;
	}
	
	private ArrayList<ISpmodWorldGen> spmodGens = new ArrayList<ISpmodWorldGen>();
	
	public void registerWorldGen(ISpmodWorldGen par1)
	{
		if (!SpmodModRegistry.isModRegistered(par1.getMod()))
		{
			return;
		}
		ChunkCollector.getInstance().registerRetrogen(par1);
		spmodGens.add(par1);
	}
	
	public void init(SpmodConfig par1)
	{
		for (int i = 0; i < spmodGens.size(); i++)
		{
			ISpmodWorldGen gen = spmodGens.get(i);
			if (gen != null)
			{
				gen.onMinecraftStart(par1);
			}
		}
		SpmodConfig.config.save();
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		this.generate(random, chunkX, chunkZ, world, false);
	}
	
	public void generate(Random rand, int chunkX, int chunkZ, World world, boolean retrogen)
	{
		for (int i = 0; i < spmodGens.size(); i++)
		{
			ISpmodWorldGen gen = spmodGens.get(i);
			if (gen != null)
			{
				if (gen.doGenerate(retrogen))
				{
					gen.generate(world, chunkX, chunkZ, rand, retrogen);
				}
			}
		}
	}
	
	static
	{
		init.registerWorldGen(new BlueFlowerGen());
	}
	
}
