package speiger.src.spmodapi.common.util;

import java.util.ArrayList;

import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler.OreReplacer;

public class TickHelper
{
	private static TickHelper helper = new TickHelper();
	
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
	}
	
}
