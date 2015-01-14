package speiger.src.tinymodularthings.common.world;

import speiger.src.api.common.world.gen.ISpmodWorldGen;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.tinymodularthings.common.world.gen.*;

public class WorldRegister
{
	public static void registerOres()
	{
		RegisterOre(new CopperOreGen());
		RegisterOre(new TinOreGen());
		RegisterOre(new SilverOreGen());
		RegisterOre(new BauxitOreGen());
		RegisterOre(new LeadOreGen());
		RegisterOre(new IridiumOreGen());
	}
	
	private static void RegisterOre(ISpmodWorldGen par1)
	{
		SpmodWorldGen.getWorldGen().registerWorldGen(par1);
	}
}
