package speiger.src.spmodapi.common.handler;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import speiger.src.api.common.data.utils.FluidData;
import speiger.src.api.common.data.utils.IFluidInfo;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.data.utils.ItemAData;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.animalgas.parts.EntitySpeed;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;
import speiger.src.api.common.registry.animalgas.parts.Resistence;
import speiger.src.api.common.registry.animalgas.parts.Resistence.ResistanceType;

public class GasHandler
{
	public static void init()
	{
		AnimalGasRegistry reg = AnimalGasRegistry.getInstance();
		EntityData data = new EntityData();
		reg.registerEntity(EntityPig.class, data);
		reg.registerEntity(EntityCow.class, data);
		reg.registerEntity(EntityMooshroom.class, data);
		reg.registerEntity(EntityChicken.class, data);
		reg.registerEntity(EntityWolf.class, data);
		reg.registerEntity(EntityVillager.class, data);
		reg.registerEntity(EntityOcelot.class, data);
		reg.registerEntity(EntitySheep.class, data);
		reg.registerEntity(EntityHorse.class, data);
		
		
	}
	
	
	public static class EntityData implements IEntityFoodInfo, IEntityDrinkInfo, IEntityGasInfo, IEntityResistenceInfo
	{
		@Override
		public EnumSet<Resistence> getEntityBaseResistence(EntityAgeable par1)
		{
			return null;
		}
		
		@Override
		public ResistanceType getResistanceType(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return ResistanceType.Medium;
			else if(par1 instanceof EntityCow) return ResistanceType.High;
			else if(par1 instanceof EntityMooshroom)return ResistanceType.Higher;
			else if(par1 instanceof EntityChicken)return ResistanceType.Weak;
			else if(par1 instanceof EntityWolf)return ResistanceType.Highest;
			else if(par1 instanceof EntityVillager)return ResistanceType.Normal;
			else if(par1 instanceof EntityOcelot)return ResistanceType.Higher;
			else if(par1 instanceof EntitySheep)return ResistanceType.Normal;
			else if(par1 instanceof EntityHorse)return ResistanceType.Medium;
			return null;
		}
		
		@Override
		public EntitySpeed getRegenSpeed(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return EntitySpeed.Medium;
			else if(par1 instanceof EntityCow)return EntitySpeed.Normal;
			else if(par1 instanceof EntityMooshroom)return EntitySpeed.Medium;
			else if(par1 instanceof EntityChicken)return EntitySpeed.VerySlow;
			else if(par1 instanceof EntityWolf)return EntitySpeed.Normal;
			else if(par1 instanceof EntityVillager)return EntitySpeed.Slow;
			else if(par1 instanceof EntityOcelot)return EntitySpeed.Fast;
			else if(par1 instanceof EntitySheep)return EntitySpeed.Medium;
			else if(par1 instanceof EntityHorse)return EntitySpeed.Normal;
			return null;
		}
		
		@Override
		public int getMinProducingGas(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 1;
			else if(par1 instanceof EntityCow)return 3;
			else if(par1 instanceof EntityMooshroom)return 3;
			else if(par1 instanceof EntitySheep)return 1;
			else if(par1 instanceof EntityHorse)return 4;
			return 0;
		}
		
		@Override
		public int getMaxProducingGas(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 3;
			else if(par1 instanceof EntityCow)return 5;
			else if(par1 instanceof EntityMooshroom)return 6;
			else if(par1 instanceof EntityChicken)return 1;
			else if(par1 instanceof EntityWolf)return 2;
			else if(par1 instanceof EntityVillager)return 3;
			else if(par1 instanceof EntityOcelot)return 2;
			else if(par1 instanceof EntitySheep)return 2;
			else if(par1 instanceof EntityHorse)return 9;
			return 0;
		}
		
		@Override
		public EntitySpeed getGasProductionSpeed(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return EntitySpeed.Medium;
			else if(par1 instanceof EntityCow)return EntitySpeed.Fast;
			else if(par1 instanceof EntityMooshroom)return EntitySpeed.Faster;
			else if(par1 instanceof EntityChicken)return EntitySpeed.Slow;
			else if(par1 instanceof EntityWolf)return EntitySpeed.Normal;
			else if(par1 instanceof EntityVillager)return EntitySpeed.Medium;
			else if(par1 instanceof EntityOcelot)return EntitySpeed.Normal;
			else if(par1 instanceof EntitySheep)return EntitySpeed.Medium;
			else if(par1 instanceof EntityHorse)return EntitySpeed.Fastest;
			return null;
		}
		
		@Override
		public List<IFluidInfo> getDrinks(EntityAgeable par1)
		{
			Fluid fluid = FluidRegistry.getFluid("milk");
			if(par1 instanceof EntityPig)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 20));
			else if(par1 instanceof EntityCow)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 25));
			else if(par1 instanceof EntityMooshroom)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 30));
			else if(par1 instanceof EntityChicken)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 5));
			else if(par1 instanceof EntityWolf)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 10));
			else if(par1 instanceof EntityVillager)return fluid != null ? Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 15), new FluidData(fluid, 10)) : Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 15));
			else if(par1 instanceof EntityOcelot)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 10));
			else if(par1 instanceof EntitySheep)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 25));
			else if(par1 instanceof EntityHorse)return Arrays.asList((IFluidInfo)new FluidData(FluidRegistry.WATER, 50));
			return null;
		}
		
		@Override
		public int getDrinkingAmount(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 4;
			else if(par1 instanceof EntityCow)return 5;
			else if(par1 instanceof EntityMooshroom)return 5;
			else if(par1 instanceof EntityChicken)return 1;
			else if(par1 instanceof EntityWolf)return 2;
			else if(par1 instanceof EntityVillager)return 3;
			else if(par1 instanceof EntityOcelot)return 2;
			else if(par1 instanceof EntitySheep)return 3;
			else if(par1 instanceof EntityHorse)return 10;
			return 0;
		}
		
		@Override
		public List<IStackInfo> getFoodItems(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return Arrays.asList((IStackInfo)new ItemAData(Item.carrot, 2));
			else if(par1 instanceof EntityCow)return Arrays.asList((IStackInfo)new ItemAData(Item.wheat, 5));
			else if(par1 instanceof EntityMooshroom)return Arrays.asList((IStackInfo)new ItemAData(Item.wheat, 5));
			else if(par1 instanceof EntityChicken)return Arrays.asList((IStackInfo)new ItemAData(Item.seeds, 5), new ItemAData(Item.pumpkinSeeds, 5), new ItemAData(Item.melonSeeds, 5));
			else if(par1 instanceof EntityWolf)return Arrays.asList((IStackInfo)new ItemData(Item.beefRaw), new ItemData(Item.porkRaw), new ItemData(Item.chickenRaw));
			else if(par1 instanceof EntityVillager)return Arrays.asList((IStackInfo)new ItemData(Item.bread), new ItemData(Item.carrot), new ItemData(Item.bakedPotato));
			else if(par1 instanceof EntityOcelot)return Arrays.asList((IStackInfo)new ItemData(Item.fishRaw));
			else if(par1 instanceof EntitySheep)return Arrays.asList((IStackInfo)new ItemAData(Item.wheat, 2));
			else if(par1 instanceof EntityHorse)return Arrays.asList((IStackInfo)new ItemAData(Item.appleRed, 5));
			return null;
		}
		
		@Override
		public int getEatingAmount(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 3;
			else if(par1 instanceof EntityCow)return 4;
			else if(par1 instanceof EntityMooshroom)return 4;
			else if(par1 instanceof EntityChicken)return 1;
			else if(par1 instanceof EntityWolf)return 1;
			else if(par1 instanceof EntityVillager)return 2;
			else if(par1 instanceof EntityOcelot)return 1;
			else if(par1 instanceof EntitySheep)return 2;
			else if(par1 instanceof EntityHorse)return 5;
			return 0;
		}
		
		@Override
		public int getMinimumReserves(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 1;
			else if(par1 instanceof EntityCow)return 5;
			else if(par1 instanceof EntityMooshroom)return 5;
			else if(par1 instanceof EntityChicken)return 2;
			else if(par1 instanceof EntityWolf)return 3;
			else if(par1 instanceof EntityVillager)return 10;
			else if(par1 instanceof EntityOcelot)return 2;
			else if(par1 instanceof EntitySheep)return 6;
			else if(par1 instanceof EntityHorse)return 20;
			return 0;
		}
		
		@Override
		public int getMaximumReserves(EntityAgeable par1)
		{
			if(par1 instanceof EntityPig)return 50;
			else if(par1 instanceof EntityCow)return 50;
			else if(par1 instanceof EntityMooshroom)return 50;
			else if(par1 instanceof EntityChicken)return 20;
			else if(par1 instanceof EntityWolf)return 35;
			else if(par1 instanceof EntityVillager)return 100;
			else if(par1 instanceof EntityOcelot)return 20;
			else if(par1 instanceof EntitySheep)return 40;
			else if(par1 instanceof EntityHorse)return 100;
			return 0;
		}


	}
}
