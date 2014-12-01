package speiger.src.spmodapi.common.blocks.gas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import speiger.src.api.common.registry.animalgas.AnimalGasRegistry;
import speiger.src.api.common.utils.RedstoneUtils;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BasicAnimalChunkLoader extends AdvTile
{
	static int maxRange = 1;
	ArrayList<EntityAgeable> storedEntities = new ArrayList<EntityAgeable>();
	HashMap<EntityAgeable, EntityCounter> poops = new HashMap<EntityAgeable, EntityCounter>();
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
		super.onIconMakerLoading();
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
		boolean powered = RedstoneUtils.isBlockGettingPowered(this);
		addEntities();
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
	}
	
	public void handleEntities()
	{
		for(EntityAgeable target : storedEntities)
		{
			if(target.isDead)
			{
				this.removeEntity(target);
				return;
			}
			
			if(worldObj.rand.nextInt(20) == 0)
			{
				poops.get(target).updateToNextID();
				continue;
			}
			if(poops.get(target).getCurrentID() >= 300 && worldObj.rand.nextBoolean())
			{
				poops.get(target).resetCounter();
				int x = (int) target.posX;
				int y = (int) (target.posY+1);
				int z = (int) target.posZ;
				int amount = Math.max(1, AnimalGasRegistry.getInstance().getGasInfo(target.getClass()).getMinProducingGas(target));
				if(worldObj.getBlockId(x, y, z) == 0)
				{
					worldObj.setBlock(x, y, z, APIBlocks.animalGas.blockID, amount, 3);
				}
				return;
			}
		}
	}
	
	public void removeEntity(EntityAgeable par1)
	{
		storedEntities.remove(par1);
		poops.remove(par1);
	}
	
	public void addEntity(EntityAgeable par1)
	{
		storedEntities.add(par1);
		poops.put(par1, new EntityCounter(0));
	}

	@Override
	public boolean canConnectToWire(int side)
	{
		return true;
	}
	

	
	
	
	
}
