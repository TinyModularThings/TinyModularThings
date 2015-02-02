package speiger.src.tinymodularthings.common.world;

import speiger.src.api.common.world.gen.ISpmodWorldGen;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.gen.BlueFlowerGen;
import speiger.src.tinymodularthings.common.world.gen.BauxitOreGen;
import speiger.src.tinymodularthings.common.world.gen.CopperOreGen;
import speiger.src.tinymodularthings.common.world.gen.IridiumOreGen;
import speiger.src.tinymodularthings.common.world.gen.LeadOreGen;
import speiger.src.tinymodularthings.common.world.gen.SilverOreGen;
import speiger.src.tinymodularthings.common.world.gen.TinOreGen;

public class WorldRegister
{
	public static void registerOres()
	{
		if(SpmodConfig.booleanInfos.get("APIOnly"))
		{
			return;
		}
		RegisterOre(new CopperOreGen());
		RegisterOre(new TinOreGen());
		RegisterOre(new SilverOreGen());
		RegisterOre(new BauxitOreGen());
		RegisterOre(new LeadOreGen());
		RegisterOre(new IridiumOreGen());
		RegisterOre(new BlueFlowerGen());
	}
	
	private static void RegisterOre(ISpmodWorldGen par1)
	{
		SpmodWorldGen.getWorldGen().registerWorldGen(par1);
	}
}
