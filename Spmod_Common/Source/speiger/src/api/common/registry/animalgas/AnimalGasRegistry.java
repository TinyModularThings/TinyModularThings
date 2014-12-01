package speiger.src.api.common.registry.animalgas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.api.common.data.utils.FluidData;
import speiger.src.api.common.data.utils.IFluidInfo;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.registry.animalgas.parts.EntitySpeed;
import speiger.src.api.common.registry.animalgas.parts.IEntityCustomInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;
import speiger.src.api.common.registry.animalgas.parts.Resistence;
import speiger.src.api.common.registry.animalgas.parts.Resistence.ResistanceType;

public final class AnimalGasRegistry
{
	private static final AnimalGasRegistry instance = new AnimalGasRegistry();
	final IEntitySelector animalGasSelector = new EntitySelectorAnimalGas();
	
	public static AnimalGasRegistry getInstance()
	{
		return instance;
	}
	
	private List<Class<? extends EntityAgeable>> registry = new ArrayList<Class<? extends EntityAgeable>>();
	private Map<Class<? extends EntityAgeable>, IEntityFoodInfo> foodData = new WeakHashMap<Class<? extends EntityAgeable>, IEntityFoodInfo>();
	private Map<Class<? extends EntityAgeable>, IEntityDrinkInfo> drinkData = new WeakHashMap<Class<? extends EntityAgeable>, IEntityDrinkInfo>();
	private Map<Class<? extends EntityAgeable>, IEntityGasInfo> gasData = new WeakHashMap<Class<? extends EntityAgeable>, IEntityGasInfo>();
	private Map<Class<? extends EntityAgeable>, IEntityResistenceInfo> resistanceData = new WeakHashMap<Class<? extends EntityAgeable>, IEntityResistenceInfo>();
	private Map<Class<? extends EntityAgeable>, IEntityCustomInfo> customData = new WeakHashMap<Class<? extends EntityAgeable>, IEntityCustomInfo>();
	
	public void registerEntity(Class<? extends EntityAgeable> entity, IEntityInfo...par1)
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
				if(info instanceof IEntityFoodInfo)
				{
					foodData.put(entity, (IEntityFoodInfo)info);
				}
				if(info instanceof IEntityGasInfo)
				{
					gasData.put(entity, (IEntityGasInfo)info);
				}
				if(info instanceof IEntityResistenceInfo)
				{
					resistanceData.put(entity, (IEntityResistenceInfo)info);
				}
				if(info instanceof IEntityCustomInfo)
				{
					customData.put(entity, (IEntityCustomInfo)info);
				}
			}
		}
	}
	
	public IEntitySelector getAnimalGasSelector()
	{
		return animalGasSelector;
	}
	
	public IEntityFoodInfo getFoodInfo(Class<? extends EntityAgeable> par1)
	{
		if(!foodData.containsKey(par1))
		{
			return new DefaultInfo();
		}
		return foodData.get(par1);
	}
	
	public IEntityDrinkInfo getDrinkInfo(Class<? extends EntityAgeable> par1)
	{
		if(!drinkData.containsKey(par1))
		{
			return new DefaultInfo();
		}
		return drinkData.get(par1);
	}
	
	public IEntityGasInfo getGasInfo(Class<? extends EntityAgeable> par1)
	{
		if(gasData.containsKey(par1))
		{
			return new DefaultInfo();
		}
		return gasData.get(par1);
	}
	
	public IEntityResistenceInfo getResistanceInfo(Class<? extends EntityAgeable> par1)
	{
		if(resistanceData.containsKey(par1))
		{
			return new DefaultInfo();
		}
		return resistanceData.get(par1);
	}
	
	public IEntityCustomInfo getCustomInfo(Class<? extends EntityAgeable> par1)
	{
		return customData.get(par1);
	}
	
	public boolean isValidEntity(EntityAgeable par1)
	{
		Class<? extends EntityAgeable> living = par1.getClass();
		return isValidEntity(living);
	}
	
	public boolean isValidEntity(Class<? extends EntityAgeable> par1)
	{
		return registry.contains(par1);
	}
	
	public void unregisterEntity(Class<? extends EntityAgeable> par1)
	{
		registry.remove(par1);
		foodData.remove(par1);
		drinkData.remove(par1);
		gasData.remove(par1);
		resistanceData.remove(par1);
	}
	
	public static final class DefaultInfo implements IEntityFoodInfo, IEntityDrinkInfo, IEntityGasInfo, IEntityResistenceInfo
	{

		@Override
		public EnumSet<Resistence> getEntityBaseResistence(EntityAgeable par1)
		{
			return null;
		}

		@Override
		public ResistanceType getResistanceType(EntityAgeable par1)
		{
			return ResistanceType.Normal;
		}
		
		@Override
		public EntitySpeed getRegenSpeed(EntityAgeable par1)
		{
			return EntitySpeed.Normal;
		}

		@Override
		public int getMinProducingGas(EntityAgeable par1)
		{
			return 1;
		}

		@Override
		public int getMaxProducingGas(EntityAgeable par1)
		{
			return 3;
		}

		@Override
		public EntitySpeed getGasProductionSpeed(EntityAgeable par1)
		{
			return EntitySpeed.Normal;
		}

		@Override
		public List<IFluidInfo> getDrinks(EntityAgeable par1)
		{
			IFluidInfo info = new FluidData(FluidRegistry.WATER, 10);
			return Arrays.asList(info);
		}

		@Override
		public int getDrinkingAmount(EntityAgeable par1)
		{
			return 1;
		}

		@Override
		public List<IStackInfo> getFoodItems(EntityAgeable par1)
		{
			IStackInfo info = new ItemData(Item.wheat);
			return Arrays.asList(info);
		}

		@Override
		public int getEatingAmount(EntityAgeable par1)
		{
			return 1;
		}

		@Override
		public int getMinimumReserves(EntityAgeable par1)
		{
			return 5;
		}

		@Override
		public int getMaximumReserves(EntityAgeable par1)
		{
			return 20;
		}

	}
	
	static class EntitySelectorAnimalGas implements IEntitySelector
	{

		@Override
		public boolean isEntityApplicable(Entity entity)
		{
			if(entity == null || entity.isDead)
			{
				return false;
			}
			if(entity instanceof EntityAgeable)
			{
				return AnimalGasRegistry.getInstance().isValidEntity((EntityAgeable)entity);
			}
			return false;
		}
		
	}


	
}
