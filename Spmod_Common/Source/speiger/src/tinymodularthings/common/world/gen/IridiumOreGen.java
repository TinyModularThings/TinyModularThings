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

public class IridiumOreGen implements ISpmodWorldGen
{
	
	boolean normal;
	boolean retro;
	
	@Override
	public void onMinecraftStart(IConfigHelper config)
	{
		normal = config.getConfigBoolean(config.getWorldGenCategorie(), "Generate Iridium", true).getResult(config.getConfiguration());
		retro = config.getConfigBoolean(config.getRetrogenCategorie(), "Retrogen Iridium", false).getResult(config.getConfiguration());
	}
	
	@Override
	public boolean doGenerate(boolean retroGen)
	{
		return retroGen ? retro : normal;
	}
	
	@Override
	public void generate(World world, int chunkX, int chunkZ, Random rand, boolean retrogen)
	{
		for (int i = 0; i < WorldProxy.getGenCounts(5, world); i++)
		{
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			int y = 5 + rand.nextInt(2 * WorldProxy.getBaseHight(world));
			new MetaOreGenerator(5, new BlockStack(TinyBlocks.ores, 5)).generate(world, rand, x, y, z);
		}
	}
	
	@Override
	public String getName()
	{
		return "Generate Iridium";
	}
	
	@Override
	public SpmodMod getMod()
	{
		return TinyModularThings.instance;
	}
}