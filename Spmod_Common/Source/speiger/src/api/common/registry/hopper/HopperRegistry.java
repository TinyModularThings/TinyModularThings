package speiger.src.api.common.registry.hopper;

import java.util.ArrayList;
import java.util.HashMap;

import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;

public class HopperRegistry
{
	static HashMap<String, HopperUpgrade> upgrades = new HashMap<String, HopperUpgrade>();
	
	public static void registerHopperUpgrade(HopperUpgrade par1)
	{
		upgrades.put(par1.getNBTName(), par1);
	}
	
	public static boolean canApplyUpgrade(HopperUpgrade par1, ArrayList<HopperUpgrade> par2)
	{
		if (par1 == null)
		{
			return false;
		}
		int maxSize = par1.getMaxStackSize();
		int cuSize = 0;
		
		if (!upgrades.containsKey(par1.getNBTName()))
		{
			return false;
		}
		
		for (int i = 0; i < par2.size(); i++)
		{
			HopperUpgrade cu = par2.get(i);
			if (cu.getNBTName().equalsIgnoreCase(par1.getNBTName()))
			{
				cuSize++;
			}
		}
		if (maxSize <= cuSize)
		{
			return false;
		}
		
		return true;
	}
	
	public static HopperUpgrade getHopperUpgradeFromNBT(String name)
	{
		try
		{
			return upgrades.get(name).getClass().newInstance();
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	
	public static enum HopperEffect
	{
		Speed(EffectType.Else, 0), AllSlots(EffectType.Else, 0);
		
		int number;
		EffectType type;
		
		private HopperEffect(EffectType par0, int numbers)
		{
			number = numbers;
			type = par0;
		}
		
		public int getBoost()
		{
			return number;
		}
		
		public EffectType getUpgradeType()
		{
			return type;
		}
		
		public static enum EffectType
		{
			Item, Fluid, Energy, Else;
		}
	}
	
}
