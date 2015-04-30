package speiger.src.spmodapi.common.blocks.gas;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.data.nbt.INBTReciver;
import speiger.src.api.common.event.EntityGasOverloadEvent;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.data.EntityProcessor;
import speiger.src.spmodapi.common.util.data.Infection;
/**
 * 
 * @author Speiger
 *
 * Big project which will be made sooner or later...
 * Anyway when i make this then it will effect a lot of the game. When you use it...
 * I just readed that code after 5 months again and now thinking of that is a cool project.
 * But stupid thinking of it...
 *
 */
public class AnimalChunkLoader extends AdvTile implements INBTReciver
{
	public List<EntityAgeable> storedEntities = new ArrayList<EntityAgeable>();
	public HashMap<EntityAgeable, EntityProcessor> entityData = new HashMap<EntityAgeable, EntityProcessor>();
	public static HashMap<UUID, NBTTagCompound> entityBackupData = new HashMap<UUID, NBTTagCompound>();
	
	
	public int entityUpdateCheck = 100;
	public static int maxRange = 1;
	public static int nextCheckTime = 100;
	
	public AnimalChunkLoader()
	{
		maxRange = SpmodConfig.integerInfos.get("AnimalChunkLoaderRange");
	}
	
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
		onEntityTick();
	}
	
	public void onEntityTick()
	{
		for(int i = 0;i<storedEntities.size();i++)
		{
			EntityProcessor pro = entityData.get(storedEntities.get(i));
			if(pro != null)
			{
				pro.onTick(storedEntities.get(i));
			}
			else
			{
				;//This never should happen. Else someone Hacket It
				this.removeEntity(storedEntities.get(i));
				i--;
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
		for(int i = 0;i<storedEntities.size();i++)
		{
			EntityAgeable target = storedEntities.get(i);
			if(!livingThings.contains(target))
			{
				removeEntity(target);
				i--;
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
		if(processor != null && !par1.isDead)
		{
			NBTTagCompound data = new NBTTagCompound();
			processor.writeToNBT(data);
			entityBackupData.put(par1.getUniqueID(), data);
		}
	}
	
	public EntityProcessor getEntityProcessorFromEntity(EntityAgeable par1)
	{
		AnimalGasRegistry registry = AnimalGasRegistry.getInstance();
		EntityProcessor pro = new EntityProcessor(registry.getFoodInfo(par1.getClass()), registry.getDrinkInfo(par1.getClass()), registry.getGasInfo(par1.getClass()), registry.getResistanceInfo(par1.getClass()), registry.getCustomInfo(par1.getClass()));
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

	public boolean hasFood(ItemStack item)
	{
		//TODO NEED TO FUNCTION
		return false;
	}

	public void useFood(ItemStack item)
	{
		
	}

	public void playEatingSound(EntityAgeable par1)
	{
		SpmodAPI.soundEngine.playSound(worldObj, (int)par1.posX, (int)par1.posY, (int)par1.posZ, "random.burp", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}
	
	public void playDrinkingSound(EntityAgeable par1)
	{
		SpmodAPI.soundEngine.playSound(worldObj, (int)par1.posX, (int)par1.posY, (int)par1.posZ, "random.drink", 0.5F, worldObj.rand.nextFloat() * 0.1F + 0.9F);
	}
	
	public boolean isFeedingTime()
	{
		return false;
	}
	
	public boolean isControlledFeeding()
	{
		return false;
	}

	public boolean hasDrink(FluidStack fluid)
	{		
		return false;
	}
	
	public void useDrink(FluidStack fluid)
	{
		
	}

	public boolean onEntityProduceGas(EntityAgeable par1, int level)
	{
		int x = (int)par1.posX;
		int y = ((int)par1.posY)+1;
		int z = (int)par1.posZ;
		int id = worldObj.getBlockId(x, y, z);
		if(id == 0 || Block.blocksList[id].isAirBlock(worldObj, x, y, z) || Block.blocksList[id].isBlockReplaceable(worldObj, x, y, z))
		{
			return worldObj.setBlock(x, y, z, APIBlocks.animalGas.blockID, level, 3);
		}
		return false;
	}

	public void onEntityOverloadGas(EntityAgeable par1)
	{
		Explosion blowup = worldObj.newExplosion(par1, par1.posX, par1.posY, par1.posZ, 6F, true, true);
		if(blowup != null)
		{
			par1.setDead();
			EntityProcessor pro = entityData.get(par1);
			EntityGasOverloadEvent evt = new EntityGasOverloadEvent(par1, blowup, pro);
			MinecraftForge.EVENT_BUS.post(evt);
		}
	}

	public boolean hasMedic(Infection par1)
	{
		return false;
	}

	public double getMedic(Infection par1)
	{
		return 0;
	}

	@Override
	public ItemStack getItemDrop()
	{
		return null;
	}
	
}
