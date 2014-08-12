package speiger.src.api.util;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;

public class Ticks
{
	static ArrayList<ITickReader> ticks = new ArrayList<ITickReader>();
	
	public static void tick(SpmodMod sender, Side side)
	{
		if(!SpmodModRegistry.isModRegistered(sender))
		{
			return;
		}
		try
		{
			for(ITickReader tick : ticks)
			{
				if(SpmodModRegistry.isModRegistered(tick.getOwner()))
				{
					tick.onTick(sender, side);
				}
			}
		}
		catch (Exception e)
		{
		}
	}
	
	public static void registerTickReciver(ITickReader par1)
	{
		if(SpmodModRegistry.isModRegistered(par1.getOwner()))
		{
			ticks.add(par1);
		}
	}
	

	
	public static interface ITickReader
	{
		public void onTick(SpmodMod sender, Side side);
		
		public SpmodMod getOwner();
	}
}
