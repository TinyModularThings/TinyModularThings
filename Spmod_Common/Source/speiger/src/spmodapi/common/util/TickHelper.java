package speiger.src.spmodapi.common.util;

import java.util.ArrayList;

import speiger.src.api.nbt.DataStorage;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.Ticks.ITickReader;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class TickHelper implements ITickReader
{
	private static TickHelper helper = new TickHelper();
	private static int cDelay = 0;
	
	public static TickHelper getInstance()
	{
		return helper;
	}
	
	public ArrayList<OreReplacer> regainOre = new ArrayList<OreReplacer>();
	
	public boolean isCloseToBackup()
	{
		return cDelay + 1000 >= SpmodConfig.savingDelay;
	}
	
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
		if (cDelay > SpmodConfig.savingDelay)
		{
			cDelay = 0;
			DataStorage.write(FMLCommonHandler.instance().getMinecraftServerInstance(), true);
		}
		if (cDelay > 40 && DataStorage.hasRequest())
		{
			cDelay -= 40;
			DataStorage.write(FMLCommonHandler.instance().getMinecraftServerInstance(), false);
		}
	}
	
	@Override
	public void onTick(SpmodMod sender, Side side)
	{
		if (side != side.SERVER)
		{
			return;
		}
		tick();
	}
	
	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}
	
}
