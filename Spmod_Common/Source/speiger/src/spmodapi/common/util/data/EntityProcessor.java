package speiger.src.spmodapi.common.util.data;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.animalgas.parts.IEntityDrinkInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityFoodInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.registry.animalgas.parts.IEntityResistenceInfo;
import speiger.src.spmodapi.common.blocks.gas.AnimalChunkLoader;

public class EntityProcessor
{
	IEntityFoodInfo food;
	IEntityDrinkInfo drink;
	IEntityGasInfo gas;
	IEntityResistenceInfo gen;
	AnimalChunkLoader tileEntity;
	
	
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
	
	public void setAnimalChunkLoader(AnimalChunkLoader par1)
	{
		tileEntity = par1;
	}
	
	public void onTick(EntityAgeable par1)
	{
		
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		
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
