package speiger.src.compactWindmills.common.blocks;

import static speiger.src.compactWindmills.common.core.CWPreference.ModID;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySource;

import java.util.HashMap;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.items.IRotorItem;
import speiger.src.api.tiles.IWindmill;
import speiger.src.api.util.InventoryUtil;
import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.client.gui.GuiWindmill;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.common.tile.TileFacing;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WindMill extends TileFacing implements IInventory, IEnergySource,
		IWindmill
{
	private static HashMap<WindmillType, Icon[]> textures = new HashMap<WindmillType, Icon[]>();
	public ItemStack[] inv = new ItemStack[1];
	public boolean loaded = false;
	public boolean damageActive = false;
	public WindmillType type;
	public int cuOutput = 0;
	
	public WindMill()
	{
		type = WindmillType.Nothing;
	}
	
	public WindMill(WindmillType par1)
	{
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
		Icon[] tex = textures.get(type);
		if(tex != null)
		{
			if (type == null || type == type.Nothing)
			{
				return null;
			}
			if (side == 0 && getFacing() != 0)
			{
				return tex[1];
			}
			if (side == getFacing())
			{
				return tex[0];
			}
			return tex[2];
		}
		return null;
	}
	
	
	
	@Override
	public void onPlaced(int facing)
	{
		this.setFacing(facing);
	}

	
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new ContainerWindmill(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiWindmill(par1, this);
	}

	@Override
	public void registerIcon(IconRegister par1)
	{
		Icon[] tex;
		for (WindmillType type : WindmillType.getValidValues())
		{
			tex = new Icon[3];
			tex[0] = par1.registerIcon(ModID.toLowerCase() + ":" + type.getTextureName() + "_front");
			tex[1] = par1.registerIcon(ModID.toLowerCase() + ":" + type.getTextureName() + "_bottom");
			tex[2] = par1.registerIcon(ModID.toLowerCase() + ":" + type.getTextureName() + "_side");
			textures.put(type, tex);
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	public ItemStack getStackInSlot(int par1)
	{
		return this.inv[par1];
	}
	
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.inv[par1] != null)
		{
			ItemStack itemstack;
			
			if (this.inv[par1].stackSize <= par2)
			{
				itemstack = this.inv[par1];
				this.inv[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inv[par1].splitStack(par2);
				
				if (this.inv[par1].stackSize == 0)
				{
					this.inv[par1] = null;
				}
				
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.inv[par1] != null)
		{
			ItemStack itemstack = this.inv[par1];
			this.inv[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.inv[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public String getInvName()
	{
		return "WindMill";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public void openChest()
	{
	}
	
	@Override
	public void closeChest()
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;//itemstack != null && itemstack.getItem() instanceof IRotorItem && ((IRotorItem)itemstack.getItem()).ignoreTier(itemstack) ? true : ((IRotorItem)itemstack.getItem()).canWorkWithWindmillTier(itemstack, type.ordinal());
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
		
		if (!worldObj.isRemote)
		{
			if (inv[0] != null)
			{
				
				if (worldObj.getWorldTime() % 64 == 0)
				{
					int oldOutput = cuOutput;
					((IRotorItem) inv[0].getItem()).onRotorTick(this, this.worldObj, inv[0]);
					cuOutput = getCurrentOutput();
//					FMLLog.getLogger().info("Damage: ");
					if(oldOutput != cuOutput)
					{
						PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, worldObj.provider.dimensionId, this.getDescriptionPacket());
					}
				}
			}
			else
			{
				int oldOutput = cuOutput;
				cuOutput = 0;
				if(oldOutput != cuOutput)
				{
					PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, worldObj.provider.dimensionId, this.getDescriptionPacket());
				}
			}
		}
	}
	
	public int getCurrentOutput()
	{
		int blocks = getNoneAirBlocks();
		float weather = getWeather();
		//Needed for SuperFlat Worlds. Testing Stuff and co. You know Aroma :P
		int heigt = worldObj.provider.getAverageGroundLevel();
		float totalEfficiency = Math.min(1.0F, (yCoord - heigt - blocks / type.getRadius() / 37.5F)) * weather;
		float energy = type.getOutput() * totalEfficiency;
		if(!CompactWindmills.oldIC2)
		{
			energy *= rotorUpdate();
		}
		else
		{
			energy *= 0.5F + worldObj.rand.nextFloat() / 2;
		}
		if(energy > type.getOutput())
		{
			energy = type.getOutput();
		}
		
		if((int)energy < 0)
		{
			return 0;
		}
		return (int)energy;
	}
	
	public float rotorUpdate()
	{
		if(hasRotor())
		{
			IRotorItem rotor = getRotor();
			float effiziens = rotor.getRotorEfficeny(inv[0]);
			if(!rotor.isInfinite(inv[0]) && damageActive)
			{
				int damage = calculateDamage();
				
				rotor.damageRotor(inv[0], damage, this);
				this.onInventoryChanged();
			}
			damageActive = false;
			return effiziens;
		}
		return 0.0F;
	}
	
	public int calculateDamage()
	{
		int Wtier = getType().ordinal();
		int RTier = getRotor().getTier(inv[0]);
		
		if(getRotor().isAdvancedRotor(inv[0]))
		{
			if(Wtier > RTier)
			{
				int damage = (Wtier - RTier);
				return 1 + (worldObj.rand.nextBoolean() ? damage : 0);
			}
			else if(Wtier == RTier)
			{
				return 1;
			}
			else
			{
				double chance = ((double)Wtier / (double)RTier) * 100;
				int rounded = (int)chance;
				int damage = 0;
				if(worldObj.rand.nextInt(100) < chance)
				{
					damage = 1;
				}
				return damage;
			}
		}
		else
		{
			return 1;
		}
	}
	
	public float getWeather()
	{
		if(worldObj.isThundering())
		{
			return 1.5F;
		}
		if(worldObj.isRaining())
		{
			return 1.2F;
		}
		return 1.0F;
	}
	
	
	
	public int getNoneAirBlocks()
	{
		int radius = type.getRadius();
		int totalBlocks = 0;
		for(int x = xCoord - radius;x<=xCoord + radius;x++)
		{
			for(int y = yCoord - radius;y<=yCoord + radius;y++)
			{
				for(int z = zCoord - radius;z<=zCoord + radius;z++)
				{
					if(!worldObj.isAirBlock(x, y, z))
					{
						totalBlocks++;
					}
				}
			}
		}
		return totalBlocks - type.getRadius() - 1;
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
	
	@Override
	public void onBreaking()
	{
		if (!worldObj.isRemote)
		{
			InventoryUtil.dropInventory(worldObj, xCoord, yCoord, zCoord, this);
		}
	}
	
	public boolean hasRotor()
	{
		if(inv[0] == null)
		{
			return false;
		}
		if(inv[0].getItem() == null)
		{
			return false;
		}
		if(!(inv[0].getItem() instanceof IRotorItem))
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
		return cuOutput;
	}
	
	@Override
	public void drawEnergy(double amount)
	{
		if (amount > 0)
		{
			damageActive = true;
		}
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
	public int getRadius()
	{
		return type.getRadius();
	}

	@Override
	public void distroyRotor()
	{
		if(!worldObj.isRemote)
		{
			inv[0] = null;
			this.onInventoryChanged();
		}
	}
	
	

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 100, worldObj.provider.dimensionId, this.getDescriptionPacket());
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
        NBTTagList nbttaglist = nbt.getTagList("Items");
        this.inv = new ItemStack[this.getSizeInventory()];
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inv.length)
            {
                this.inv[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        cuOutput = nbt.getInteger("Output");
        damageActive = nbt.getBoolean("Damage");
        type = WindmillType.values()[nbt.getInteger("Type")];
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.inv.length; ++i)
        {
            if (this.inv[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inv[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
        nbt.setInteger("Output", cuOutput);
        nbt.setBoolean("Damage", damageActive);
        nbt.setInteger("Type", type.ordinal());
	}
	
	
	
	
	
}
