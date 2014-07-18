package speiger.src.tinymodularthings.common.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.config.IConfigHelper;
import speiger.src.api.world.ISpmodWorldGen;
import speiger.src.spmodapi.common.util.proxy.WorldProxy;
import speiger.src.spmodapi.common.world.genHelper.MetaOreGenerator;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class CopperOreGen implements ISpmodWorldGen
{
	
	boolean generateCopper;
	boolean retrogenerateCopper;
	
	@Override
	public void onMinecraftStart(IConfigHelper config)
	{
		generateCopper = config.getConfigBoolean(config.getWorldGenCategorie(), "Generate Copper", true).setComment("Set to false to disable CopperGenerating").getResult(config.getConfiguration());
		retrogenerateCopper = config.getConfigBoolean(config.getRetrogenCategorie(), "Retrogen Copper", false).setComment("Require a Retrogen activation").getResult(config.getConfiguration());
	}
	
	@Override
	public boolean doGenerate(boolean retroGen)
	{
		return retroGen ? retrogenerateCopper : generateCopper;
	}
	
	@Override
	public void generate(World world, int chunkX, int chunkZ, Random rand, boolean retrogen)
	{
		if (world.provider.dimensionId == 1 || world.provider.dimensionId == -1)
		{
			return;
		}
		for (int i = 0; i < WorldProxy.getGenCounts(20, world); i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = rand.nextInt(40 * WorldProxy.getBaseHight(world)) + rand.nextInt(20 * WorldProxy.getBaseHight(world)) + 10 * WorldProxy.getBaseHight(world);
			world.provider.getAverageGroundLevel();
			MetaOreGenerator gen = new MetaOreGenerator(6, new BlockStack(TinyBlocks.ores, 0));
			
			gen.generate(world, rand, x, y, z);
			
		}
	}
	
	@Override
	public String getName()
	{
		return "CopperGen";
	}
	
	@Override
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
	
}
