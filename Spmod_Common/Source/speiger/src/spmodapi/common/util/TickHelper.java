package speiger.src.spmodapi.common.util;

import java.util.ArrayList;

import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;
import cpw.mods.fml.common.FMLCommonHandler;

public class TickHelper
{
	private static TickHelper helper = new TickHelper();
	private static int cDelay = 0;
	
	
	public static TickHelper getInstance()
	{
		return helper;
	}
	
	public ArrayList<OreReplacer> regainOre = new ArrayList<OreReplacer>();
	
	public void tick()
	{
		for (int i = 0; i < regainOre.size(); i++)
		{
			OreReplacer cuOre = regainOre.remove(i);
			
			if (!cuOre.generate())
			{
				regainOre.add(cuOre);
			}
		}
		cDelay++;
		if(cDelay>SpmodConfig.savingDelay)
		{
			cDelay = 0;
			StructureStorage.instance.writeStructureDataToNBT(FMLCommonHandler.instance().getMinecraftServerInstance());
		}
	}
	
}
