package speiger.src.spmodapi.common.blocks.gas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.data.EntityProcessor;

public class AnimalChunkLoader extends AdvTile
{
	public List<EntityAgeable> storedEntities = new ArrayList<EntityAgeable>();
	public HashMap<EntityAgeable, EntityProcessor> entityData = new HashMap<EntityAgeable, EntityProcessor>();
	
	public int entityUpdateCheck = 100;
	public static int maxRange = 1;
	public static int nextCheckTime = 100;
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote)
		{
			return;
		}
		if(entityUpdateCheck <= 0)
		{
			entityUpdateCheck = nextCheckTime;
			updateEntities();
		}
	}
	
	
	
	
	
	public void updateEntities()
	{
		List<EntityAgeable> livingThings = worldObj.selectEntitiesWithinAABB(EntityAgeable.class, AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1).expand((maxRange*10), (maxRange*10), (maxRange*10)), AnimalGasRegistry.getInstance().getAnimalGasSelector());
		for(EntityAgeable target : livingThings)
		{
			if(!storedEntities.contains(target))
			{
				addEntity(target);
				continue;
			}
		}
		for(EntityAgeable target : storedEntities)
		{
			if(!livingThings.contains(target))
			{
				removeEntity(target);
				continue;
			}
		}
	}
	
	private void addEntity(EntityAgeable par1)
	{
		storedEntities.add(par1);
		entityData.put(par1, getEntityProcessorFromEntity(par1));
	}
	
	private void removeEntity(EntityAgeable par1)
	{
		storedEntities.remove(par1);
		entityData.remove(par1);
	}
	
	public EntityProcessor getEntityProcessorFromEntity(EntityAgeable par1)
	{
		AnimalGasRegistry registry = AnimalGasRegistry.getInstance();
		EntityProcessor pro = new EntityProcessor(registry.getFoodInfo(par1.getClass()), registry.getDrinkInfo(par1.getClass()), registry.getGasInfo(par1.getClass()), registry.getResistanceInfo(par1.getClass()));
		pro.setAnimalChunkLoader(this);
		return pro;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		storedEntities.clear();
		entityData.clear();
		NBTTagList list = par1.getTagList("EntityInfos");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			EntityAgeable target = (EntityAgeable)worldObj.getEntityByID(nbt.getInteger("EntityID"));
			EntityProcessor pro = EntityProcessor.loadProcessorFromNBT(nbt, target);
			pro.setAnimalChunkLoader(this);
			storedEntities.add(target);
			entityData.put(target, pro);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagList list = new NBTTagList();
		for(EntityAgeable target : storedEntities)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("EntityID", target.entityId);
			entityData.get(target).writeToNBT(nbt);
			list.appendTag(nbt);
		}
		par1.setTag("EntityInfos", list);
	}
	
	

	

	
}
