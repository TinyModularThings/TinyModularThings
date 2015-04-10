package speiger.src.compactWindmills.common.blocks;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergySourceInfo;
import ic2.api.info.IC2Classic;
import ic2.api.network.INetworkDataProvider;
import ic2.api.network.INetworkUpdateListener;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.items.IRotorItem;
import speiger.src.api.common.world.items.IRotorItem.RotorWeight;
import speiger.src.api.common.world.tiles.interfaces.IWindmill;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.client.gui.GuiWindmill;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WindMill extends FacedInventory implements IInventory,
		IEnergySource, IWindmill, IWrenchable, IEnergySourceInfo, INetworkDataProvider, INetworkUpdateListener
{
	public boolean loaded = false;
	public boolean damageActive = false;
	public WindmillType type;
	public double storedEnergy = 0.0D;
	public double maxEnergy = 0.0D;
	
	public boolean rotorInited = false;
	public boolean basicRotation = false;
	public int rotorID = 0;
	public int rotorMeta = 0;
	public RotorWeight weight = null;
	
	public float speed = 0.0F;
	public float requestedSpeed = 0.0F;
	
	public float Rotated = 0F;
	
	public WindMill()
	{
		this(WindmillType.Nothing);
	}
	
	public WindMill(WindmillType par1)
	{
		super(1);
		type = par1;
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side == 0 && side != getFacing())
		{
			return getEngine().getTexture(CompactWindmills.windmill, type.ordinal(), 0);
		}
		
		if(side == getFacing())
		{
			return getEngine().getTexture(CompactWindmills.windmill, type.ordinal(), 1);
		}
		
		return getEngine().getTexture(CompactWindmills.windmill, type.ordinal(), 2);
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public AdvContainer getInventory(InventoryPlayer par1)
	{
		return new ContainerWindmill(par1, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiInventoryCore getGui(InventoryPlayer par1)
	{
		return new GuiWindmill(par1, this);
	}
		
	@Override
	public String getInvName()
	{
		return "WindMill";
	}
		
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return itemstack != null && itemstack.getItem() instanceof IRotorItem;
	}
	
	@Override
	public void onTick()
	{
		if (!loaded && worldObj != null)
		{
			if (worldObj.isRemote)
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			else
			{
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
			}
			loaded = true;
		}
		
		if (worldObj.isRemote)
		{
			return;
		}
		
		if(getClockTime() % 20 == 0)
		{
			if(inv[0] == null && rotorInited)
			{
				clearRotor();
			}
			else if(inv[0] != null && !rotorInited)
			{
				initRotor();
			}
			else if(inv[0] != null && rotorID != inv[0].itemID && rotorMeta != inv[0].getItemDamage())
			{
				refreshRotor();
			}
		}
		if(hasRotor() && rotorInited)
		{
			if(getClockTime() % 64 == 0)
			{
				if(this.basicRotation)
				{
					updateWindSpeed();
				}
				if(!damageActive && rand.nextBoolean())
				{
					activateDamageRandomly();
				}
				getRotor().onRotorTick(this, worldObj, inv[0]);
				if(this.damageActive)
				{
					damageActive = false;
					int calculatedDamage = calculateDamage();
					if(calculatedDamage > 0)
					{
						getRotor().damageRotor(inv[0], calculatedDamage, this);
					}
					damageActive = false;
				}
			}
			adjustRotorSpeed();
			generateEnergy();
		}
	}
	
	public void activateDamageRandomly()
	{
		if(speed < 0.25F)
		{
			return;
		}
		if(speed >= 0.25F && speed <= 0.5F)
		{
			if(rand.nextInt(3) == 0)
			{
				damageActive = true;
			}
		}
		else if(speed >= 0.5F && speed <= 1.1F)
		{
			if(rand.nextBoolean())
			{
				damageActive = true;
			}
		}
		else
		{
			damageActive = true;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		damageActive = nbt.getBoolean("Damage");
		type = WindmillType.values()[nbt.getInteger("Type")];
		storedEnergy = nbt.getDouble("Stored");
		rotorInited = nbt.getBoolean("Inited");
		if(rotorInited)
		{
			basicRotation = nbt.getBoolean("BasicRotor");
			rotorID = nbt.getInteger("RotorID");
			rotorMeta = nbt.getInteger("RotorMeta");
			weight = RotorWeight.values()[nbt.getInteger("Weight")];
		}
		speed = nbt.getFloat("Speed");
		requestedSpeed = nbt.getFloat("RSpeed");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("Damage", damageActive);
		nbt.setInteger("Type", type.ordinal());
		nbt.setDouble("Stored", storedEnergy);
		if(rotorInited)
		{
			nbt.setBoolean("Inited", rotorInited);
			nbt.setBoolean("BasicRotor", basicRotation);
			nbt.setInteger("RotorID", rotorID);
			nbt.setInteger("RotorMeta", rotorMeta);
			nbt.setInteger("Weight", weight.ordinal());
		}
		nbt.setFloat("Speed", speed);
		nbt.setFloat("RSpeed", requestedSpeed);
	}
	
	@Override
	public List<String> getNetworkedFields()
	{
		List<String> list = new ArrayList<String>();
		list.add("type");
		list.add("rotorInited");
		list.add("basicRotation");
		list.add("rotorID");
		list.add("rotorMeta");
		list.add("weight");
		list.add("speed");
		list.add("requestedSpeed");
		list.add("facing");
		return list;
	}
	
	public double getProducingEnergy()
	{
		double maxProduce = (double)type.getOutput();
		maxProduce *= speed;
		return maxProduce;
	}
	
	public void handleClientSpeed()
	{
		Rotated += speed;
		if(Rotated >= 360F)
		{
			Rotated-=360F;
		}
	}
	
	@Override
	public void onNetworkUpdate(String field)
	{
		if(field.equals("facing"))
		{
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void init()
	{
		maxEnergy = type.getOutput() * 10;
	}

	public void generateEnergy()
	{
		if(storedEnergy >= maxEnergy)
		{
			return;
		}
		double maxProduce = (double)type.getOutput();
		maxProduce *= speed;
		storedEnergy = maxProduce;
		if(storedEnergy > maxEnergy)
		{
			storedEnergy = maxEnergy;
		}
		this.damageActive = true;
	}
	
	public int calculateDamage()
	{
		IRotorItem rotor = getRotor();
		if(rotor.isInfinite(inv[0]))
		{
			return 0;
		}
		float diff = requestedSpeed - speed;
		int damage = 0;
		if(diff >= 0.8F)
		{
			if(rand.nextBoolean())
			{
				damage = 2 + rand.nextInt(5);
			}
			else
			{
				damage = 2;
			}
		}
		else if(diff >= 0.4F)
		{
			if(rand.nextBoolean())
			{
				damage = 1 + rand.nextInt(5);
			}
			else
			{
				damage = 1;
			}
			
		}
		else if(diff < 0.4 && diff > -0.4F)
		{
			damage = 1;
		}
		else if(diff <= -0.4F)
		{
			if(rand.nextBoolean())
			{
				damage = 1 + rand.nextInt(5);
			}
			else
			{
				damage = 1;
			}
		}
		else
		{
			if(rand.nextBoolean())
			{
				damage = 2 + rand.nextInt(5);
			}
			else
			{
				damage = 2;
			}
		}
		if(speed > 1.1F && rand.nextBoolean() && weight != null && weight.ordinal() <= 2)
		{
			if(weight.ordinal() == 2)
			{
				damage += 2;
			}
			else
			{
				damage+=3;
			}
		}
		
		return damage;
	}
	
	public void updateWindSpeed()
	{
		this.requestedSpeed = getWindSpeed();
	}
	
	public void adjustRotorSpeed()
	{
		float oldSpeed = speed;
		if(requestedSpeed > speed)
		{
			speed+=weight.getSpeedChange();
		}
		else if(requestedSpeed < speed)
		{
			speed-=weight.getSpeedChange();
		}
		if(oldSpeed != speed)
		{
			IC2.network.updateTileEntityField(this, "speed");
		}
	}
	
	public void initRotor()
	{
		rotorInited = true;
		rotorID = inv[0].itemID;
		rotorMeta = inv[0].getItemDamage();
		basicRotation = !getRotor().hasCustomSpeedMath(this, inv[0]);
		weight = getRotor().getRotorWeight(this, inv[0]);
		if(weight == null)
		{
			clearRotor();
			return;
		}
		IC2.network.updateTileEntityField(this, "rotorInited");
		IC2.network.updateTileEntityField(this, "rotorID");
		IC2.network.updateTileEntityField(this, "rotorMeta");
		IC2.network.updateTileEntityField(this, "basicRotation");
		IC2.network.updateTileEntityField(this, "requestedSpeed");
		IC2.network.updateTileEntityField(this, "weight");
		IC2.network.updateTileEntityField(this, "speed");

	}
	
	public void clearRotor()
	{
		rotorInited = false;
		rotorID = 0;
		rotorMeta = 0;
		basicRotation = false;
		weight = null;
		requestedSpeed = 0.0F;
		speed = 0.0F;
		IC2.network.updateTileEntityField(this, "rotorInited");
		IC2.network.updateTileEntityField(this, "rotorID");
		IC2.network.updateTileEntityField(this, "rotorMeta");
		IC2.network.updateTileEntityField(this, "basicRotation");
		IC2.network.updateTileEntityField(this, "requestedSpeed");
		IC2.network.updateTileEntityField(this, "speed");
	}
	
	public void refreshRotor()
	{
		clearRotor();
		initRotor();
	}
	
	public float getWeather()
	{
		if (worldObj.isThundering())
		{
			return 1.5F;
		}
		if (worldObj.isRaining())
		{
			return 1.2F;
		}
		return 1.0F;
	}
	
	public int getNoneAirBlocks()
	{
		int radius = 12;
		int totalBlocks = 0;
		for (int x = xCoord - radius; x <= xCoord + radius; x++)
		{
			for (int y = yCoord - radius; y <= yCoord + radius; y++)
			{
				for (int z = zCoord - radius; z <= zCoord + radius; z++)
				{
					if (!worldObj.isAirBlock(x, y, z))
					{
						totalBlocks++;
					}
				}
			}
		}
		totalBlocks -= 11;
		if(totalBlocks < 0)
		{
			totalBlocks = 0;
		}
		return totalBlocks;
	}
	
	@Override
	public void invalidate()
	{
		if (!worldObj.isRemote)
		{
			EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(unloadEvent);
		}
		super.invalidate();
	}
	
	public boolean hasRotor()
	{
		if (inv[0] == null)
		{
			return false;
		}
		if (inv[0].getItem() == null)
		{
			return false;
		}
		if (!(inv[0].getItem() instanceof IRotorItem))
		{
			return false;
		}
		return true;
	}
	
	public IRotorItem getRotor()
	{
		return ((IRotorItem) inv[0].getItem());
	}
	
	public WindmillType getType()
	{
		return type;
	}
	
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return true;
	}
	
	@Override
	public double getOfferedEnergy()
	{
		return Math.min(storedEnergy, type.getOutput());
	}
	
	@Override
	public void drawEnergy(double amount)
	{
		storedEnergy-=amount;
	}
	
	@Override
	public void onChunkUnload()
	{
		if (!worldObj.isRemote)
		{
			EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent(this);
			MinecraftForge.EVENT_BUS.post(unloadEvent);
		}
		super.onChunkUnload();
	}
	
	@Override
	public TileEntity getWindmill()
	{
		return this;
	}
	
	@Override
	public ChunkCoordinates getChunkCoordinates()
	{
		return new ChunkCoordinates(xCoord, yCoord, zCoord);
	}
	
	@Override
	public int getTier()
	{
		return type.ordinal();
	}
	
	@Override
	public void distroyRotor()
	{
		if (!worldObj.isRemote)
		{
			inv[0] = null;
			this.clearRotor();
			this.onInventoryChanged();
		}
	}
	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		if (side != getFacing() && !entityPlayer.isSneaking())
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return entityPlayer.isSneaking();
	}
	
	@Override
	public float getWrenchDropRate()
	{
		return 100F;
	}
	
	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new BlockStack(this).asItemStack();
	}

	@Override
	public boolean isFake()
	{
		return false;
	}

	@Override
	public int getMaxEnergyAmount()
	{
		return type.getOutput();
	}

	@Override
	public void setNewSpeed(float amount)
	{
		requestedSpeed = amount;
	}

	@Override
	public float getWindSpeed()
	{
		float wind = IC2Classic.enabledCustoWindNetwork() ? IC2Classic.getWindNetwork().getWindStrenght(worldObj) : IC2.windStrength;
		float speed = (wind * 4.8F) / 100F;
		speed *= (((float)15625F - (float)getNoneAirBlocks()) / (float)15625F);
		speed *= getWeather();
		return speed;
	}
	
	public float getHeightEffect()
	{
		int bestHeight = IC2.getSeaLevel(worldObj) + 20;
		return Math.min(((float)yCoord / (float)bestHeight), 1.5F);
	}

	@Override
	public float getActualSpeed()
	{
		return speed;
	}

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(CompactWindmills.windmill, 1, 0);
	}

	
	
}
