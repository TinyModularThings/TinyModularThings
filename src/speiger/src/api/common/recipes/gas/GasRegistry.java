package speiger.src.api.common.recipes.gas;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import speiger.src.api.common.recipes.gas.IGasEntity.GasInfo;

public class GasRegistry
{
	private static List<IGasEntity> list = new ArrayList<IGasEntity>();
	
	public static void registerGasHandler(IGasEntity par1)
	{
		list.add(par1);
	}
	
	public static GasInfo getGasInfo(EntityAgeable par1)
	{
		List<GasInfo> infos = new ArrayList<GasInfo>();
		for(IGasEntity info : list)
		{
			GasInfo gas = info.getGasInfo(par1);
			if(gas != null)
			{
				infos.add(gas);
			}
		}
		int min = 0;
		int max = 0;
		
		for(GasInfo info : infos)
		{
			min = Math.max(info.getMin(), min);
			max = Math.max(info.getMax(), max);
		}
		if(max <= 0)
		{
			return null;
		}
		return new GasInfo(min, max).setResult(par1.getRNG());
	}
	
	public static GasInfo getDeathGasInfo(EntityAgeable par1)
	{
		List<GasInfo> infos = new ArrayList<GasInfo>();
		for(IGasEntity info : list)
		{
			GasInfo gas = info.getGasInfo(par1);
			if(gas != null)
			{
				infos.add(gas);
			}
		}
		int max = 0;
		for(GasInfo info : infos)
		{
			max = Math.max(max, info.getMax());
		}
		if(max <= 0)
		{
			return null;
		}
		return new GasInfo(0, max).setResult(par1.getRNG());
	}
}
