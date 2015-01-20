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
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.data.utils.IFluidInfo;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.animalgas.parts.EntitySpeed;
import speiger.src.api.common.registry.animalgas.parts.IEntityCustomInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityLogic;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;
import speiger.src.api.common.registry.animalgas.parts.Resistence;
import speiger.src.api.common.registry.animalgas.parts.Resistence.ResistanceType;
import speiger.src.api.common.utils.DoubleCounter;
import speiger.src.spmodapi.common.blocks.gas.AnimalChunkLoader;

public final class EntityProcessor implements IEntityLogic
{
	static Random rand = new Random("SpeigersEntityHumanitySystem".hashCode());
	
	IEntityFoodInfo food;
	IEntityDrinkInfo drink;
	IEntityGasInfo gas;
	IEntityResistenceInfo gen;
	IEntityCustomInfo info;
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
	Map<Resistence, DoubleCounter> resistances = new WeakHashMap<Resistence, DoubleCounter>();
	double healingProgress = 0D;
	
	//Gas
	double currentLevel = 0D;
	EntitySpeed boost = null;
	double extraGas = 0D;
	
	public EntityProcessor(IEntityFoodInfo par1, IEntityDrinkInfo par2, IEntityGasInfo par3, IEntityResistenceInfo par4, IEntityCustomInfo par5)
	{
		food = par1;
		drink = par2;
		gas = par3;
		gen = par4;
		info = par5;
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
		if(info != null && info.canUpdate())
		{
			info.onPreTick(par1, this);
		}
		handleFood(par1);
		handleDrink(par1);
		handleGene(par1);
		handleGas(par1);
		if(info != null && info.canUpdate())
		{
			info.onPostTick(par1, this);
		}
	}
	
	public void handleGene(EntityAgeable par1)
	{
		if(rand.nextInt(10000) == 0 && rand.nextBoolean())
		{
			Infection[] inv = Infection.getSadValues();
			Infection randEffect = inv[rand.nextInt(inv.length)];
			if(resistances.get(randEffect).get() > 5D)
			{
				resistances.get(randEffect).removeOne();
			}
		}
		
		if(currentEffection != Infection.Nothing)
		{
			currentEffection.handleEffect(par1);
			if(rand.nextInt(25) == 0)
			{
				EntitySpeed regen = gen.getRegenSpeed(par1);
				if(regen == null)
				{
					regen = EntitySpeed.Normal;
				}
				healingProgress+=regen.getSpeed();
				par1.heal(0.1F);
			}
			if(rand.nextInt(5) == 0 && tileEntity.hasMedic(currentEffection))
			{
				double added = tileEntity.getMedic(currentEffection);
				healingProgress+=added;
				par1.heal(1F);
			}
			if(healingProgress >= 100)
			{
				healingProgress = 0;
				Infection inf = currentEffection;
				Resistence res = inf.getResistance();
				currentEffection = Infection.Nothing;
				if(res != null)
				{
					DoubleCounter count = resistances.get(res);
					if(count.get() < 95D)
					{
						ResistanceType type = gen.getResistanceType(par1);
						if(type == null)
						{
							type = ResistanceType.Normal;
						}
						count.add(type.getValue());
					}
					resistances.put(res, count);
				}
				if(inf != Infection.Fever && (rand.nextInt(10) == 0 || inf == Infection.HardFever))
				{
					currentEffection = Infection.Fever;
					healingProgress = 75D;
				}
			}
		}
		if(tileEntity.worldObj.getRainStrength(1.0F) > 0.2F && (currentEffection == Infection.Nothing || currentEffection == Infection.Fever))
		{
			int height = tileEntity.worldObj.getChunkFromBlockCoords((int)par1.posX, (int)par1.posZ).getPrecipitationHeight((int)par1.posX, (int)par1.posZ);
			int y = (int)(par1.posY+par1.height);
			if(height-1 <= y)
			{
				int resist = rand.nextInt((int)this.resistances.get(Resistence.Weather).get());
				int effect = rand.nextInt(100);
				if(currentEffection == Infection.Nothing)
				{
					if(effect >= resist && rand.nextInt(50) == 0)
					{
						currentEffection = Infection.Fever;
						healingProgress = 0.0D;
					}
				}
				else
				{
					resist /= 2;
					if(resist < effect && rand.nextInt(20) == 0)
					{
						currentEffection = Infection.HardFever;
						healingProgress = 0.0D;
					}
				}
			}
		}
		if(currentEffection == Infection.Nothing)
		{
			boolean stop = checkOil(par1);
			if(stop)
			{
				return;
			}
		}
	}
	
	private boolean checkOil(EntityAgeable par1)
	{
        double d0 = par1.posY + (double)par1.getEyeHeight();
        int i = MathHelper.floor_double(par1.posX);
        int j = MathHelper.floor_float((float)MathHelper.floor_double(d0));
        int k = MathHelper.floor_double(par1.posZ);
        int l = par1.worldObj.getBlockId(i, j, k);
        Fluid oil = FluidRegistry.getFluid("oil");
        if(oil != null && oil.getBlockID() == l)
        {
        	if(rand.nextBoolean())
        	{
        		currentEffection = Infection.Poisen;
        		healingProgress = 0.0D;
        	}
        	else
        	{
        		currentEffection = Infection.Squits;
        		healingProgress = 0.0D;
        	}
        	return true;
        }
        return false;
	}
	
	public void handleGas(EntityAgeable par1)
	{
		if(extraGas > 100D && rand.nextInt(10) == 0)
		{
			extraGas -= 100;
			currentLevel++;
		}
		if(currentLevel > 100D && rand.nextInt(5) == 0)
		{
			int amount = Math.max(Math.max(gas.getMinProducingGas(par1), 1), rand.nextInt(gas.getMaxProducingGas(par1)+1));
			boolean farted = tileEntity.onEntityProduceGas(par1, amount);
			if(farted)
			{
				currentLevel-= 100;
				return;
			}
		}
		if(rand.nextBoolean())
		{
			EntitySpeed speed = gas.getGasProductionSpeed(par1);
			if(speed == null)
			{
				speed = EntitySpeed.Normal;
			}
			currentLevel += speed.getSpeed();
		}
		if(currentLevel > 1000)
		{
			tileEntity.onEntityOverloadGas(par1);
		}
	}
	
	public void handleDrink(EntityAgeable par1)
	{
		nextDrinkTime-= par1.worldObj.provider.isHellWorld ? 50 : 1;
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
				resistances.put(cu, new DoubleCounter(100));
			}
		}
		for(Resistence cu : Resistence.values())
		{
			if(!resistances.containsKey(cu))
			{
				resistances.put(cu, new DoubleCounter(0));
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
		Iterator<Entry<Resistence, DoubleCounter>> iter = resistances.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<Resistence, DoubleCounter> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("Key", entry.getKey().ordinal());
			data.setDouble("Value", entry.getValue().get());
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
			resistances.put(Resistence.values()[data.getInteger("Key")], new DoubleCounter(data.getDouble("Value")));
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

