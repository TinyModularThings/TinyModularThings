package speiger.src.tinymodularthings.common.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.config.IConfigHelper;
import speiger.src.api.world.ISpmodWorldGen;
import speiger.src.spmodapi.common.util.proxy.WorldProxy;
import speiger.src.spmodapi.common.world.genHelper.MetaOreGenerator;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;

public class BauxitOreGen implements ISpmodWorldGen
{
	boolean normal;
	boolean retro;
	
	@Override
	public void onMinecraftStart(IConfigHelper config)
	{
		normal = config.getConfigBoolean(config.getWorldGenCategorie(), "Generate Bauxit", true).getResult(config.getConfiguration());
		retro = config.getConfigBoolean(config.getRetrogenCategorie(), "Retrogen Bauxit", false).getResult(config.getConfiguration());
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
		for (int i = 0; i < WorldProxy.getGenCounts(20, world); i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = 30 + rand.nextInt(64 * WorldProxy.getBaseHight(world));
			MetaOreGenerator gen = new MetaOreGenerator(15, new BlockStack(TinyBlocks.bauxitOre));
			gen.setBlockToReplace(Block.dirt);
			gen.forcePlacing();
			gen.generate(world, rand, x, y, z);
		}
		
	}
	
	@Override
	public String getName()
	{
		return "Generate Bauxit";
	}
	
	@Override
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
	
}
