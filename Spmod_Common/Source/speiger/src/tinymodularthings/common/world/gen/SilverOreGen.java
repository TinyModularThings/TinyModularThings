package speiger.src.tinymodularthings.common.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.utils.config.IConfigHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.gen.ISpmodWorldGen;
import speiger.src.spmodapi.common.util.proxy.WorldProxy;
import speiger.src.spmodapi.common.world.genHelper.MetaOreGenerator;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class SilverOreGen implements ISpmodWorldGen
{
	
	boolean normal;
	boolean retro;
	
	@Override
	public void onMinecraftStart(IConfigHelper config)
	{
		normal = config.getConfigBoolean(config.getWorldGenCategorie(), "Generate Silver", true).getResult(config.getConfiguration());
		retro = config.getConfigBoolean(config.getRetrogenCategorie(), "Retrogen Silver", false).getResult(config.getConfiguration());
	}
	
	@Override
	public boolean doGenerate(boolean retroGen)
	{
		return retroGen ? retro : normal;
	}
	
	@Override
	public void generate(World world, int chunkX, int chunkZ, Random rand, boolean retrogen)
	{
		if (world.provider.dimensionId == 1 || world.provider.dimensionId == -1)
		{
			return;
		}
		for (int i = 0; i < WorldProxy.getGenCounts(8, world); i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = 10 + rand.nextInt(10 * WorldProxy.getBaseHight(world));
			new MetaOreGenerator(4, new BlockStack(TinyBlocks.ores, 2)).generate(world, rand, x, y, z);
		}
		
	}
	
	@Override
	public String getName()
	{
		return "OreGenSilver";
	}
	
	@Override
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
}
