package speiger.src.api.common.registry.animalgas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.entity.EntityLiving;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;

public class AnimalGasRegistry
{
	private static AnimalGasRegistry instance = new AnimalGasRegistry();
	
	public static AnimalGasRegistry getInstance()
	{
		return instance;
	}
	
	private List<Class<? extends EntityLiving>> registry = new ArrayList<Class<? extends EntityLiving>>();
	private Map<Class<? extends EntityLiving>, IEntityFoodInfo> foodData = new WeakHashMap<Class<? extends EntityLiving>, IEntityFoodInfo>();
	private Map<Class<? extends EntityLiving>, IEntityDrinkInfo> drinkData = new WeakHashMap<Class<? extends EntityLiving>, IEntityDrinkInfo>();
	private Map<Class<? extends EntityLiving>, IEntityGasInfo> gasData = new WeakHashMap<Class<? extends EntityLiving>, IEntityGasInfo>();
	private Map<Class<? extends EntityLiving>, IEntityResistenceInfo> resistanceData = new WeakHashMap<Class<? extends EntityLiving>, IEntityResistenceInfo>();
	private Map<Class<? extends EntityLiving>, IEntityInfo> customData = new WeakHashMap<Class<? extends EntityLiving>, IEntityInfo>();
	
	public void registerEntity(Class<? extends EntityLiving> entity, IEntityInfo...par1)
	{
		if(registry.contains(entity) || par1 == null || par1.length == 0)
		{
			return;
		}
		registry.add(entity);
		for(IEntityInfo info : par1)
		{
			if(info != null)
			{
				if(info instanceof IEntityDrinkInfo)
				{
					drinkData.put(entity, (IEntityDrinkInfo)info);
				}
				else if(info instanceof IEntityFoodInfo)
				{
					foodData.put(entity, (IEntityFoodInfo)info);
				}
				else if(info instanceof IEntityGasInfo)
				{
					gasData.put(entity, (IEntityGasInfo)info);
				}
				else if(info instanceof IEntityResistenceInfo)
				{
					resistanceData.put(entity, (IEntityResistenceInfo)info);
				}
				else
				{
					customData.put(entity, info);
				}
			}
		}
	}
	
	public void unregisterEntity(Class<? extends EntityLiving> par1)
	{
		registry.remove(par1);
		foodData.remove(par1);
		drinkData.remove(par1);
		gasData.remove(par1);
		resistanceData.remove(par1);
		customData.remove(par1);
	}
	
}
