package speiger.src.spmodapi.common.blocks.gas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.data.EntityProcessor;

public class AnimalChunkLoader extends AdvTile implements INBTReciver
{
	public List<EntityAgeable> storedEntities = new ArrayList<EntityAgeable>();
	public HashMap<EntityAgeable, EntityProcessor> entityData = new HashMap<EntityAgeable, EntityProcessor>();
	public static HashMap<UUID, NBTTagCompound> entityBackupData = new HashMap<UUID, NBTTagCompound>();
	
	
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
	
	public void onGasTick()
	{
		for(EntityAgeable target : storedEntities)
		{
			EntityProcessor pro = entityData.get(target);
			if(pro != null)
			{
				pro.onTick(target);
			}
			else
			{
				;//This never should happen. Else someone Hacket It
				this.removeEntity(target);
			}
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
		EntityProcessor processor = entityBackupData.containsKey(par1.getUniqueID()) ? EntityProcessor.loadProcessorFromNBT(entityBackupData.get(par1), par1).setAnimalChunkLoader(this) : getEntityProcessorFromEntity(par1);
		entityData.put(par1, processor);
	}
	
	private void removeEntity(EntityAgeable par1)
	{
		storedEntities.remove(par1);
		EntityProcessor processor = entityData.remove(par1);
		if(processor != null)
		{
			NBTTagCompound data = new NBTTagCompound();
			processor.writeToNBT(data);
			entityBackupData.put(par1.getUniqueID(), data);
		}
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

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		entityBackupData.clear();
		NBTTagList list = par1.getTagList("BackupList");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			UUID id = new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast"));
			entityBackupData.put(id, data.getCompoundTag("Data"));
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		Iterator<Entry<UUID, NBTTagCompound>> iter = entityBackupData.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<UUID, NBTTagCompound> entry = iter.next();
			NBTTagCompound nbt = new NBTTagCompound();
			UUID id = entry.getKey();
			nbt.setLong("UUIDMost", id.getMostSignificantBits());
			nbt.setLong("UUIDLeast", id.getLeastSignificantBits());
			nbt.setCompoundTag("Data", entry.getValue());
			list.appendTag(nbt);
		}
		par1.setTag("BackupList", list);
	}

	@Override
	public void finishLoading()
	{
		
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "animalGasData";
	}
	
	public static void onEntityDeath(EntityAgeable par1)
	{
		AnimalGasRegistry reg = AnimalGasRegistry.getInstance();
		UUID id = par1.getUniqueID();
		if(id != null && entityBackupData.containsKey(id))
		{
			entityBackupData.remove(id);
		}
	}
	
}
