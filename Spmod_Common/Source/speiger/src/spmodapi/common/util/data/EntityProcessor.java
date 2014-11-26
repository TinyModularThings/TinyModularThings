package speiger.src.spmodapi.common.util.data;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.data.utils.IFluidInfo;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.animalgas.parts.EntitySpeed;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;
import speiger.src.api.common.registry.animalgas.parts.Resistence;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.common.blocks.gas.AnimalChunkLoader;

public class EntityProcessor
{
	static Random rand = new Random("SpeigersEntityHumanitySystem".hashCode());
	
	IEntityFoodInfo food;
	IEntityDrinkInfo drink;
	IEntityGasInfo gas;
	IEntityResistenceInfo gen;
	AnimalChunkLoader tileEntity;
	
	boolean needsInit = true;
	
	//Food
	int reserves = 0;
	int foodLevel = 50;
	int nextFoodUse = 2000;
	
	//Drink
	int drinkLevel = 100;
	int nextDrinkTime = 1250;
	
	//Resistance
	Infection currentEffection = Infection.Nothing;
	Map<Resistence, EntityCounter> resistances = new WeakHashMap<Resistence, EntityCounter>();
	double healingProgress = 0D;
	
	//Gas
	double currentLevel = 0D;
	EntitySpeed boost = null;
	double extraGas = 0D;
	
	public EntityProcessor(IEntityFoodInfo par1, IEntityDrinkInfo par2, IEntityGasInfo par3, IEntityResistenceInfo par4)
	{
		food = par1;
		drink = par2;
		gas = par3;
		gen = par4;
	}
	
	private EntityProcessor()
	{
		
	}
	
	public EntityProcessor setAnimalChunkLoader(AnimalChunkLoader par1)
	{
		tileEntity = par1;
		return this;
	}
	
	public void onTick(EntityAgeable par1)
	{
		if(needsInit)
		{
			initEntity(par1);
			return;
		}
		handleFood(par1);
		handleDrink(par1);
//		handleGene(par1);
//		handleGas(par1);
	}
	
	public void handleDrink(EntityAgeable par1)
	{
		nextDrinkTime--;
		if(nextDrinkTime <= 0)
		{
			nextDrinkTime = 1250;
			drinkLevel-=drink.getDrinkingAmount(par1);
		}
		if(drinkLevel < 100 && rand.nextInt(10) == 0)
		{
			boolean hasDrink = false;
			for(IFluidInfo info : drink.getDrinks(par1))
			{
				if(hasDrink)
				{
					break;
				}
				FluidStack fluid = info.getResult();
				hasDrink = tileEntity.hasDrink(fluid);
				if(hasDrink)
				{
					tileEntity.useDrink(fluid);
					drinkLevel++;
					tileEntity.playDrinkingSound(par1);
				}
			}
		}
	}
	
	public void handleFood(EntityAgeable par1)
	{
		nextFoodUse--;
		if(nextFoodUse <= 0)
		{
			nextFoodUse = 2000;
			foodLevel-= food.getEatingAmount(par1);
			if(foodLevel <= 0)
			{
				foodLevel = 50;
				reserves--;
			}
		}
		if(foodLevel >= 100 && rand.nextInt(5) == 0)
		{
			foodLevel-=50;
			reserves++;
		}
		
		if(tileEntity.isFeedingTime())
		{
			if(rand.nextBoolean())
			{
				if(rand.nextInt(tileEntity.isControlledFeeding() ? 10 : 50) == 0)
				{
					boolean hasFood = false;
					for(IStackInfo pFood : food.getFoodItems(par1))
					{
						if(hasFood)
						{
							break;
						}
						ItemStack between = pFood.getResult();
						hasFood = tileEntity.hasFood(between);
						if(hasFood)
						{
							tileEntity.useFood(between);
							foodLevel++;
							tileEntity.playEatingSound(par1);
						}
					}
				}
			}
		}
	}
	
	void initEntity(EntityAgeable par1)
	{
		needsInit = false;
		reserves = food.getMaximumReserves(par1) / 2;
		EnumSet<Resistence> res = gen.getEntityBaseResistence(par1);
		if(res != null && !res.isEmpty())
		{
			for(Resistence cu : res)
			{
				resistances.put(cu, new EntityCounter(100));
			}
		}
		for(Resistence cu : Resistence.values())
		{
			if(!resistances.containsKey(cu))
			{
				resistances.put(cu, new EntityCounter(0));
			}
		}
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound food = new NBTTagCompound();
		food.setInteger("Reserves", reserves);
		food.setInteger("FoodLevel", foodLevel);
		food.setInteger("NextFoodUse", nextFoodUse);
		nbt.setCompoundTag("Food", food);
		NBTTagCompound drink = new NBTTagCompound();
		drink.setInteger("DrinkLevel", drinkLevel);
		drink.setInteger("DrinkNextUse", nextDrinkTime);
		nbt.setCompoundTag("Drink", drink);
		NBTTagCompound gas = new NBTTagCompound();
		gas.setDouble("GasLevel", currentLevel);
		gas.setInteger("GasSpeedModifire", boost == null ? -1 : boost.ordinal());
		gas.setDouble("ExtraGas", extraGas);
		nbt.setCompoundTag("Gas", gas);
		NBTTagCompound resis = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		Iterator<Entry<Resistence, EntityCounter>> iter = resistances.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<Resistence, EntityCounter> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("Key", entry.getKey().ordinal());
			data.setInteger("Value", entry.getValue().getCurrentID());
			list.appendTag(data);
		}
		resis.setTag("Resistances", list);
		resis.setDouble("HealingState", healingProgress);
		nbt.setCompoundTag("Resistance", resis);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		resistances.clear();
		NBTTagCompound food = nbt.getCompoundTag("Food");
		reserves = food.getInteger("Reserves");
		foodLevel = food.getInteger("FoodLevel");
		nextFoodUse = food.getInteger("NextFoodUse");
		NBTTagCompound drink = nbt.getCompoundTag("Drink");
		drinkLevel = drink.getInteger("DrinkLevel");
		nextDrinkTime = drink.getInteger("DrinkNextUse");
		NBTTagCompound gas = nbt.getCompoundTag("Gas");
		currentLevel = gas.getDouble("GasLevel");
		int dater = gas.getInteger("GasSpeedModifire");
		boost = dater == -1 ? null : EntitySpeed.values()[dater];
		extraGas = nbt.getDouble("ExtraGas");
		NBTTagCompound resis = nbt.getCompoundTag("Resistance");
		NBTTagList list = resis.getTagList("Resistances");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			resistances.put(Resistence.values()[data.getInteger("Key")], new EntityCounter(data.getInteger("Value")));
		}
		healingProgress = resis.getDouble("HealinState");
		needsInit = false;
	}
	
	public static EntityProcessor loadProcessorFromNBT(NBTTagCompound nbt, EntityAgeable par1)
	{
		EntityProcessor pro = new EntityProcessor();
		pro.init(par1);
		pro.readFromNBT(nbt);
		return pro;
	}
	
	void init(EntityAgeable par1)
	{
		AnimalGasRegistry reg = AnimalGasRegistry.getInstance();
		food = reg.getFoodInfo(par1.getClass());
		drink = reg.getDrinkInfo(par1.getClass());
		gas = reg.getGasInfo(par1.getClass());
		gen = reg.getResistanceInfo(par1.getClass());
	}
	
}

