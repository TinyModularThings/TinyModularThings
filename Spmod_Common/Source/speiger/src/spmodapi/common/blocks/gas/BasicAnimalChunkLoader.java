package speiger.src.spmodapi.common.blocks.gas;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.registry.animalgas.parts.IEntityGasInfo;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BasicAnimalChunkLoader extends AdvTile
{
	static int maxRange = 1;
	ArrayList<EntityAgeable> storedEntities = new ArrayList<EntityAgeable>();
	WeakHashMap<UUID, EntityCounter> poops = new WeakHashMap<UUID, EntityCounter>();
	Icon[] textures = null;
	boolean rotate = false;
	
	public BasicAnimalChunkLoader()
	{
		maxRange = SpmodConfig.integerInfos.get("AnimalChunkLoaderRange");
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(textures == null)
		{
			if(rotate)
			{
				loadTexture();
			}
			else
			{
				List<Icon> tex = new ArrayList<Icon>();
				tex.addAll(Arrays.asList(TextureEngine.getIcon(APIBlocks.animalUtils, 0)));
				textures = new Icon[6];
				textures[0] = textures[1] = tex.remove(0);
				for(int i = 2;i<textures.length;i++)
				{
					textures[i] = tex.get(CodeProxy.getRandom().nextInt(tex.size()));
				}
			}
		}
		
		if(rotate)
		{
			if(clockCanTick())
			{
				onClockTick();
			}
			if(getClockTime() % 20 == 0)
			{
				onClockTick();
				Icon storage = textures[2];
				for(int i = 3;i<textures.length;i++)
				{
					textures[i-1] = textures[i];
				}
				textures[textures.length-1] = storage;
			}
		}
		
		return textures[side];
	}
	
	private void loadTexture()
	{
		TextureEngine engine = TextureEngine.getTextures();
		this.textures = new Icon[11];
		this.textures[0] = textures[1] = engine.getTexture(APIBlocks.animalUtils, 0);
		Icon[] result = engine.getTextureInRange(APIBlocks.animalUtils, 1, 10);
		for(int i = 0;i<result.length;i++)
		{
			textures[i+2] = result[i];
		}
	}

	@Override
	public void onIconMakerLoading()
	{
		rotate = true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Need to be Powered to work");
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("Basic AnimalChunkloader. It works like the Old one");
			par2.add("It works much slower as before. And the gain is much reduced");
			par2.add("But the difference between this one and the otherone and this works pretty basic.");
		}
		else
		{
			par2.add("Press Ctrl to get extra Infos");
		}
	}
	
	@Override
	public float getBlockHardness()
	{
		return 4F;
	}

	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 8F;
	}

	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote)
		{
			return;
		}
		boolean powered = this.isPowered();
		if(getClockTime() % 20 == 0)
		{
			addEntities();
		}
		if(powered)
		{
			handleEntities();
		}
	}
	
	public void addEntities()
	{
		List<EntityAgeable> list = worldObj.selectEntitiesWithinAABB(EntityAgeable.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord).expand(10*maxRange, 10*maxRange, 10*maxRange), AnimalGasRegistry.getInstance().getAnimalGasSelector());
		for(EntityAgeable target : list)
		{
			if(!storedEntities.contains(target))
			{
				addEntity(target);
			}
		}
		for(int i = 0;i<storedEntities.size();i++)
		{
			EntityAgeable target = storedEntities.get(i);
			if(!list.contains(target))
			{
				removeEntity(target);
				i--;
			}
		}
		WeakHashMap<UUID, EntityCounter> counter = new WeakHashMap<UUID, EntityCounter>();
		for(Entry<UUID, EntityCounter> entry : poops.entrySet())
		{
			UUID key = entry.getKey();
			EntityCounter value = entry.getValue();
			counter.put(key, value);
		}
		poops.clear();
		poops.putAll(counter);
	}
	
	public void handleEntities()
	{
		for(EntityAgeable target : new ArrayList<EntityAgeable>(storedEntities))
		{
			if(target.isDead)
			{
				this.removeEntity(target);
				return;
			}
//			FMLLog.getLogger().info("Rate: "+getCounter(target).getCurrentID());
			if(worldObj.rand.nextInt(20) == 0)
			{
				getCounter(target).updateToNextID();
				continue;
			}
			if(getCounter(target).getCurrentID() >= 150 && worldObj.rand.nextBoolean())
			{
				getCounter(target).resetCounter();
				int x = (int) target.posX;
				int y = (int) (target.posY+1);
				int z = (int) target.posZ;
				IEntityGasInfo gasData = AnimalGasRegistry.getInstance().getGasInfo(target.getClass());
				int min = Math.min(10, gasData.getMinProducingGas(target));
				int amount = Math.min(min, min + rand.nextInt(gasData.getMaxProducingGas(target)));
				if(worldObj.getBlockId(x, y, z) == 0)
				{
					worldObj.setBlock(x, y, z, APIBlocks.animalGas.blockID, amount, 3);
				}
				return;
			}
		}
	}
	
	private EntityCounter getCounter(EntityAgeable par1)
	{
		return poops.get(par1.getUniqueID());
	}
	
	public void removeEntity(EntityAgeable par1)
	{
		storedEntities.remove(par1);
		poops.remove(par1.getUniqueID());
	}
	
	public void addEntity(EntityAgeable par1)
	{
		storedEntities.add(par1);
		poops.put(par1.getUniqueID(), new EntityCounter(0));
	}
	
	public void addEntity(EntityAgeable par1, EntityCounter par2)
	{
		storedEntities.add(par1);
		poops.put(par1.getUniqueID(), par2);
	}

	@Override
	public boolean canConnectToWire(int side)
	{
		return true;
	}

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.animalUtils, 1, 0);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		NBTTagList list = par1.getTagList("PoopData");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			UUID ids = new UUID(data.getLong("MostUU"), data.getLong("LeastUU"));
			poops.put(ids, new EntityCounter(data.getInteger("Count")));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<storedEntities.size();i++)
		{
			EntityAgeable living = this.storedEntities.get(i);
			if(living.isDead)
			{
				continue;
			}
			UUID id = living.getUniqueID();
			if(!poops.containsKey(id))
			{
				continue;
			}
			NBTTagCompound data = new NBTTagCompound();
			data.setLong("MostUU", id.getMostSignificantBits());
			data.setLong("LeastUU", id.getLeastSignificantBits());
			data.setInteger("Count", poops.get(id).getCurrentID());
			list.appendTag(data);
		}
		par1.setTag("PoopData", list);
	}
	

	
	
	
	
}
