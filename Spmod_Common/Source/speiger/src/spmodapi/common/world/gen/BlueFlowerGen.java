package speiger.src.spmodapi.common.world.gen;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.config.IConfigHelper;
import speiger.src.api.world.ISpmodWorldGen;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;

public class BlueFlowerGen implements ISpmodWorldGen
{

	
	@Override
	public boolean doGenerate(boolean retroGen)
	{
		return true;
	}
	
	@Override
	public void generate(World world, int chunkX, int chunkZ, Random rand, boolean retrogen)
	{
		BiomeGenBase biom = world.getWorldChunkManager().getBiomeGenAt(chunkX*16+16, chunkZ*16+16);
		int tries = 0;
	    if (biom == BiomeGenBase.jungle) tries = 1;
	    else if (biom == BiomeGenBase.jungleHills) tries = 1;
	    else if (biom == BiomeGenBase.forest) tries = 1;
	    else if (biom == BiomeGenBase.plains) tries = 4;

		for(int i = 0;i<tries;i++)
		{      
			int x = chunkX * 16 + rand.nextInt(16) + 8;
			int y = rand.nextInt(128);
			int z = chunkZ * 16 + rand.nextInt(16) + 8;
			new WorldGenFlowers(APIBlocks.blueFlower.blockID).generate(world, rand, x, y, z);
		}
	}
	
	@Override
	public String getName()
	{
		return "BlueFlowers";
	}
	
	@Override
	public SpmodMod getMod()
	{
		return SpmodAPI.instance;
	}

	@Override
	public void onMinecraftStart(IConfigHelper config)
	{		
	}
	
}
