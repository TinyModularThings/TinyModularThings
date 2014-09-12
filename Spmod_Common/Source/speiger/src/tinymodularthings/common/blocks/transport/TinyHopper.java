package speiger.src.tinymodularthings.common.blocks.transport;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.hopper.CopiedFluidTank;
import speiger.src.api.hopper.CopiedIInventory;
import speiger.src.api.hopper.HopperRegistry;
import speiger.src.api.hopper.HopperRegistry.HopperEffect;
import speiger.src.api.hopper.HopperRegistry.HopperEffect.EffectType;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IHopperInventory;
import speiger.src.api.hopper.IHopperUpgradeItem;
import speiger.src.api.inventory.IFilteredInventory;
import speiger.src.api.inventory.IOwner;
import speiger.src.api.inventory.IOwnerProvider;
import speiger.src.api.inventory.TankSlot;
import speiger.src.api.pipes.IAdvancedPipeProvider;
import speiger.src.api.util.FluidUtils;
import speiger.src.api.util.InventoryUtil;
import speiger.src.spmodapi.common.handler.FakePlayer;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.transport.TinyHopperGui;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.utils.HopperType;
import speiger.src.tinymodularthings.common.utils.slot.InventoryFilter;
import speiger.src.tinymodularthings.common.utils.slot.InventoryHopperUpgrades;
import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinyHopper extends TileFacing implements IFluidHandler, IHopper,
		ISidedInventory, IPowerReceptor, IHopperInventory,
		IAdvancedPipeProvider, IFilteredInventory, IPowerEmitter,
		IOwnerProvider
{
	public HopperType type;
	public boolean redstone;
	public boolean speed = false;
	public boolean allSlots = false;
	public ArrayList<EntityPlayer> users = new ArrayList<EntityPlayer>();
	public ItemStack[] inventory = new ItemStack[0];
	public InventoryFilter filter = new InventoryFilter(0);
	public InventoryHopperUpgrades installedUpgrades = new InventoryHopperUpgrades(this);
	public int InventoryMode = -1;
	public AdvancedFluidTank[] tanks = new AdvancedFluidTank[0];
	public EnergyProvider energy = new EnergyProvider(this, 25000).setPowerLoss(1);
	public int[] transferlimits = { 1, 10, 10 };
	public String owner = "None";
	public EntityPlayer fakePlayer = null;
	
	int[][] SlotX = { new int[0], { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
	
	public TinyHopper()
	{
		type = HopperType.Nothing;
		redstone = false;
	}
	
	public TinyHopper(HopperType ADV, boolean adv)
	{
		type = ADV;
		redstone = adv;
	}
	
	@Override
	public void setupUser(EntityPlayer player)
	{
		owner = player.getCommandSenderName();
	}
	
	@Override
	public void onPlaced(int facing)
	{
		setFacing(facing);
		setRotation(ForgeDirection.getOrientation(facing).getOpposite().ordinal());
	}
	
	public void setMode(int Mode)
	{
		InventoryMode = Mode;
	}
	
	public void updateMode()
	{
		if (this.InventoryMode < 0)
		{
			return;
		}
		
		switch (type)
		{
			case Items:
				this.inventory = new ItemStack[this.InventoryMode];
				this.filter = new InventoryFilter(this.InventoryMode);
				break;
			case Fluids:
				this.tanks = new AdvancedFluidTank[this.InventoryMode];
				for (int i = 0; i < this.InventoryMode; i++)
				{
					this.tanks[i] = new AdvancedFluidTank(this, "BasicHopperTank_" + i, 16000);
				}
				this.filter = new InventoryFilter(this.InventoryMode);
				break;
			case Energy:
				inventory = new ItemStack[this.InventoryMode];
				break;
			case Nothing:
				break;
			default:
				break;
		}
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return inventory[par1];
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (inventory[par1] != null)
		{
			if (inventory[par1].stackSize <= par2)
			{
				ItemStack itemstack = inventory[par1];
				inventory[par1] = null;
				return itemstack;
			}
			
			ItemStack itemstack = inventory[par1].splitStack(par2);
			
			if (inventory[par1].stackSize == 0)
			{
				inventory[par1] = null;
			}
			
			return itemstack;
		}
		
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (inventory[par1] != null)
		{
			ItemStack itemstack = inventory[par1];
			inventory[par1] = null;
			return itemstack;
		}
		
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		inventory[par1] = par2ItemStack;
		
		if ((par2ItemStack != null) && (par2ItemStack.stackSize > getInventoryStackLimit()))
		{
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public String getInvName()
	{
		return "TinyHopper";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
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
		return true;
	}
	
	@Override
	public int getXPos()
	{
		return xCoord;
	}
	
	@Override
	public int getYPos()
	{
		return yCoord;
	}
	
	@Override
	public int getZPos()
	{
		return zCoord;
	}
	
	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
	@Override
	public boolean hasFacing()
	{
		return true;
	}
	
	@Override
	public boolean hasRotation()
	{
		return true;
	}
	
	@Override
	public boolean hasInventory()
	{
		return type == HopperType.Items;
	}
	
	@Override
	public IInventory getInventory()
	{
		return new CopiedIInventory(this);
	}
	
	@Override
	public ItemStack addItemsToHopper(ItemStack par1)
	{
		ItemStack stack = par1.copy();
		int added = 0;
		for (int i = 0; i < getSizeInventory(); i++)
		{
			ItemStack cu = getStackInSlot(i);
			if ((cu != null) && (cu.stackSize < cu.getMaxStackSize()))
			{
				if ((cu.isItemEqual(stack)) && (ItemStack.areItemStackTagsEqual(stack, cu)))
				{
					int newStackSize = cu.stackSize + stack.stackSize - cu.getMaxStackSize();
					if (newStackSize < 0)
					{
						newStackSize = 0;
					}
					added += stack.stackSize - newStackSize;
					
					cu.stackSize = (64 - newStackSize);
					
					setInventorySlotContents(i, cu);
					
					if (newStackSize == 0)
					{
						break;
					}
				}
			}
		}
		
		stack.stackSize = added;
		return stack;
	}
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if (hasContainer())
		{
			par1.openGui(TinyModularThings.instance, EnumIDs.ADVTiles.getId(), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if (!worldObj.isRemote)
		{
			ItemStack item = par1.getCurrentEquippedItem();
			if (item != null)
			{
				if (item.getItem() instanceof IToolWrench && ((IToolWrench) item.getItem()).canWrench(par1, xCoord, yCoord, zCoord))
				{
					if (sneak)
					{
						int nextFacing = this.setNextFacing();
						this.setFacing(nextFacing);
						if (nextFacing == this.getRotation())
						{
							this.setFacing(this.setNextFacing());
						}
					}
					else
					{
						int nextRotation = this.setNextRotation();
						this.setRotation(nextRotation);
						if (nextRotation == this.getFacing())
						{
							this.setRotation(this.setNextRotation());
						}
					}
					((IToolWrench) item.getItem()).wrenchUsed(par1, xCoord, yCoord, zCoord);
					this.updateBlock();
					return true;
				}
				else if (item.getItem() instanceof IHopperUpgradeItem)
				{
					IHopperUpgradeItem upgrade = (IHopperUpgradeItem) item.getItem();
					if (upgrade != null)
					{
						HopperUpgrade hopper = upgrade.getUpgrade(item);
						if (this.addUpgrade(hopper))
						{
							installedUpgrades.addUpgrade(item, hopper);
						}
						par1.inventory.setInventorySlotContents(par1.inventory.currentItem, item);
						par1.inventory.onInventoryChanged();
						return true;
					}
				}
				
			}
			boolean preventOpening = false;
			for (HopperUpgrade cu : this.installedUpgrades.getAllUpgrades())
			{
				if (cu.onClick(sneak, par1, par2, this, side))
				{
					preventOpening = true;
				}
			}
			return preventOpening;
			
		}
		
		return false;
	}
	
	@Override
	public ItemStack removeItemsFromHopper(int slot, int stackSize)
	{
		return decrStackSize(slot, stackSize);
	}
	
	@Override
	public boolean hasFluidTank()
	{
		return type == HopperType.Fluids;
	}
	
	@Override
	public int getFluidTankSize()
	{
		return tanks.length;
	}
	
	@Override
	public CopiedFluidTank getTank(int tankID)
	{
		return new CopiedFluidTank(tanks[tankID]);
	}
	
	@Override
	public CopiedFluidTank[] getFluidTank()
	{
		return CopiedFluidTank.createTanks(tanks);
	}
	
	@Override
	public FluidStack addFluid(FluidStack par1)
	{
		FluidTank tank = FluidUtils.getPossibleTankFromFluid(tanks, par1);
		if (tank == null)
		{
			return null;
		}
		FluidStack added = par1.copy();
		
		added.amount = tank.fill(par1, true);
		return added;
	}
	
	@Override
	public FluidStack addFluid(FluidStack par1, int forcedSlot)
	{
		if (forcedSlot > tanks.length)
		{
			return null;
		}
		int added = tanks[forcedSlot].fill(par1, false);
		if (added <= 0)
		{
			return null;
		}
		FluidStack copied = par1.copy();
		copied.amount = tanks[forcedSlot].fill(par1, true);
		return copied;
	}
	
	@Override
	public FluidStack removeFluid(int amount, int slot, boolean remove)
	{
		if (slot > tanks.length)
		{
			return null;
		}
		return tanks[slot].drain(amount, remove);
	}
	
	@Override
	public boolean hasEnergyProvider()
	{
		return type == HopperType.Energy;
	}
	
	@Override
	public EnergyProvider getEnergyStorage()
	{
		return energy;
	}
	
	@Override
	public ArrayList<HopperUpgrade> getUpgrades()
	{
		return installedUpgrades.getAllUpgrades();
	}
	
	@Override
	public boolean hasUpgradeLimitation()
	{
		return false;
	}
	
	@Override
	public ArrayList<HopperUpgrade> getValidUpgrades()
	{
		return new ArrayList<HopperUpgrade>();
	}
	
	@Override
	public boolean applyEffect(HopperEffect par1, boolean change)
	{
		if (par1.getUpgradeType() == EffectType.Else)
		{
			if ((par1 == HopperEffect.Speed) && (speed != change))
			{
				speed = change;
				return true;
			}
		}
		else
		{
			if ((par1 == HopperEffect.AllSlots) && (allSlots != change))
			{
				allSlots = change;
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean hasEffectApplied(HopperEffect par1)
	{
		if (par1 == HopperEffect.Speed)
		{
			return speed;
		}
		if (par1 == HopperEffect.AllSlots)
		{
			return allSlots;
		}
		return false;
	}
	
	@Override
	public ArrayList<EntityPlayer> getUsingPlayers()
	{
		return users;
	}
	
	@Override
	public EntityPlayer getFakePlayer()
	{
		return this.fakePlayer;
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public int getRotation()
	{
		return rotation;
	}
	
	@Override
	public int getFacing()
	{
		return super.getFacing();
	}
	
	@Override
	public boolean isSixSidedRotation()
	{
		return true;
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		if (type != HopperType.Energy || side.ordinal() == getRotation())
		{
			return null;
		}
		return energy.getSaveBCPowerProvider();
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
	}
	
	@Override
	public void onTick()
	{
		if (!worldObj.isRemote)
		{
			energy.update();
		}
		
		if (worldObj.isRemote)
		{
			return;
		}
		
		if (worldObj.getWorldTime() % 10 == 0)
		{
			this.updateBlock();
		}
		
		if (worldObj.getWorldTime() % 80L == 0L)
		{
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20.0D, worldObj.provider.dimensionId, getDescriptionPacket());
		}
		
		if (fakePlayer == null)
		{
			fakePlayer = new FakePlayer(worldObj, xCoord, yCoord, zCoord);
		}
		
		if (speed || worldObj.getWorldTime() % 20 == 0)
		{
			for (HopperUpgrade upgrade : installedUpgrades.getAllUpgrades())
			{
				upgrade.onTick(this);
			}
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
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
		
		InventoryMode = nbt.getInteger("InventorySize");
		type = HopperType.values()[nbt.getInteger("Mode")];
		updateMode();
		redstone = nbt.getBoolean("Redstone");
		transferlimits[0] = nbt.getInteger("ItemTrans");
		transferlimits[1] = nbt.getInteger("FluidTrans");
		transferlimits[2] = nbt.getInteger("EnergyTrans");
		speed = nbt.getBoolean("SpeedUpgrade");
		allSlots = nbt.getBoolean("AllSlots");
		facing = nbt.getInteger("facing");
		rotation = nbt.getInteger("rotation");
		owner = nbt.getString("Owner");
		energy.readFromNBT(nbt);
		
		NBTTagList tanksdata = nbt.getTagList("Tanks");
		for (int i = 0; i < tanksdata.tagCount(); i++)
		{
			NBTTagCompound data = (NBTTagCompound) tanksdata.tagAt(i);
			tanks[data.getInteger("Slot")] = new AdvancedFluidTank(this, data.getString("Name"), data.getInteger("Size"));
			tanks[data.getInteger("Slot")].readFromNBT(data);
		}
		NBTTagList nbttaglist = nbt.getTagList("Items");
		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			
			if ((b0 >= 0) && (b0 < inventory.length))
			{
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		
		NBTTagList players = nbt.getTagList("Players");
		for (int i = 0; i < players.tagCount(); i++)
		{
			NBTTagCompound player = (NBTTagCompound) players.tagAt(i);
			users.add(worldObj.getPlayerEntityByName(player.getString("Name")));
		}
		
		NBTTagCompound filterNBT = nbt.getCompoundTag("Filter");
		filter.readFromNBT(filterNBT);
		
		NBTTagCompound upgrades = nbt.getCompoundTag("Upgrades");
		installedUpgrades.readFromNBT(upgrades);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("InventorySize", InventoryMode);
		nbt.setInteger("Mode", type.ordinal());
		nbt.setBoolean("Redstone", redstone);
		nbt.setInteger("ItemTrans", transferlimits[0]);
		nbt.setInteger("FluidTrans", transferlimits[1]);
		nbt.setInteger("EnergyTrans", transferlimits[2]);
		nbt.setBoolean("SpeedUpgrade", speed);
		nbt.setBoolean("AllSlots", allSlots);
		nbt.setInteger("facing", facing);
		nbt.setInteger("rotation", rotation);
		nbt.setString("Owner", owner);
		energy.writeToNBT(nbt);
		
		NBTTagCompound filter = new NBTTagCompound();
		this.filter.writeToNBT(filter);
		nbt.setCompoundTag("Filter", filter);
		
		NBTTagCompound upgrades = new NBTTagCompound();
		this.installedUpgrades.readFromNBT(upgrades);
		nbt.setCompoundTag("Upgrades", upgrades);
		
		NBTTagList tanksdata = new NBTTagList();
		for (int i = 0; i < tanks.length; i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("Slot", i);
			data.setString("Name", tanks[i].getName());
			data.setInteger("Size", tanks[i].getCapacity());
			tanks[i].writeToNBT(data);
			tanksdata.appendTag(data);
		}
		nbt.setTag("Tanks", tanksdata);
		
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
		
		NBTTagList players = new NBTTagList();
		for (int i = 0; i < users.size(); i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			data.setString("Name", users.get(i).username);
			players.appendTag(data);
		}
		nbt.setTag("Players", players);
	}
	
	@Override
	public Slot[] getSlots()
	{
		Slot[] array = new Slot[InventoryMode];
		for (int i = 0; i < InventoryMode; i++)
		{
			array[i] = new Slot(this, i, SlotX[InventoryMode][i], 39);
		}
		return array;
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new TinyHopperInventory(par1, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new TinyHopperGui(par1, this);
	}
	
	@Override
	public void playerJoins(EntityPlayer par1)
	{
		users.add(par1);
	}
	
	@Override
	public void playerLeaves(EntityPlayer par1)
	{
		users.remove(par1);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (tanks.length == 0)
		{
			return 0;
		}
		FluidTank validTank = FluidUtils.getPossibleTankFromFluidAndFilter(tanks, resource, this.filter.getItems());
		if (validTank == null)
		{
			return 0;
		}
		
		return validTank.fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(from, resource.amount, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if (tanks.length == 0)
		{
			return null;
		}
		
		FluidTank validTank = FluidUtils.getFirstFilledTank(tanks);
		if (validTank == null)
		{
			return null;
		}
		return validTank.drain(maxDrain, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (from.ordinal() == getRotation())
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		if (tanks.length == 0)
		{
			return null;
		}
		FluidTankInfo[] info = new FluidTankInfo[tanks.length];
		for (int i = 0; i < tanks.length; i++)
		{
			info[i] = tanks[i].getInfo();
		}
		return info;
	}
	
	@Override
	public HopperType getHopperType()
	{
		return type;
	}
	
	@Override
	public TankSlot[] getTanks()
	{
		TankSlot[] slots = new TankSlot[InventoryMode];
		for (int i = 0; i < slots.length; i++)
		{
			slots[i] = new TankSlot(tanks[i], SlotX[InventoryMode][i], 15);
		}
		return slots;
	}
	
	@Override
	public ForgeDirection getConnectionSide(IBlockAccess par0, int x, int y, int z)
	{
		return ForgeDirection.UNKNOWN;
	}
	
	@Override
	public boolean canConnect(ForgeDirection direction)
	{
		ForgeDirection front = ForgeDirection.getOrientation(rotation);
		ForgeDirection back = ForgeDirection.getOrientation(facing);
		
		if ((direction == back) || (direction == front))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderingTexture()
	{
		return type.getTexture(redstone);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	@Override
	public int getTransferlimit(HopperType par1)
	{
		if (par1 != par1.Nothing)
		{
			return transferlimits[par1.ordinal()];
		}
		return 0;
	}
	
	@Override
	public int getClearTransferlimit(HopperType par1)
	{
		switch (par1)
		{
			case Items:
				return 1;
			case Fluids:
				return 10;
			case Energy:
				return 10;
			case Nothing:
				return 0;
			default:
				return 0;
		}
	}
	
	@Override
	public void addTransferlimit(HopperType par1, int amount)
	{
		if (par1 == par1.Nothing)
		{
			return;
		}
		transferlimits[par1.ordinal()] += amount;
	}
	
	@Override
	public void removeTransferlimit(HopperType par1, int amount)
	{
		if (par1 == par1.Nothing)
		{
			return;
		}
		transferlimits[par1.ordinal()] -= amount;
		if (transferlimits[par1.ordinal()] < 0)
		{
			transferlimits[par1.ordinal()] = 0;
		}
	}
	
	@Override
	public String getOwner()
	{
		return owner;
	}
	
	@Override
	public boolean isOwnerInventory()
	{
		return this instanceof IOwner;
	}
	
	@Override
	public boolean addUpgrade(HopperUpgrade par1)
	{
		if (HopperRegistry.canApplyUpgrade(par1, installedUpgrades.getAllUpgrades()) && (par1.getUpgradeType() == this.type || par1.getUpgradeType() == HopperType.Nothing))
		{
			par1.onRegisterUpgrade(this);
			return true;
		}
		return false;
	}
	
	@Override
	public void removeUpgrade(HopperUpgrade par1)
	{
		par1.onRemovingUpgrade(this);
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		int[] array = new int[InventoryMode];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = i;
		}
		return array;
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if (j == getRotation())
		{
			return false;
		}
		if (filter.getStackInSlot(i) == null || (filter.getStackInSlot(i) != null && filter.getStackInSlot(i).isItemEqual(itemstack)))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return true;
	}
	
	@Override
	public boolean canEmitPowerFrom(ForgeDirection side)
	{
		if (type == type.Energy)
		{
			return side.ordinal() == this.getRotation();
		}
		return false;
	}
	
	@Override
	public int isPowering(int side)
	{
		if (redstone)
		{
			if (side == getFacing())
			{
				boolean flag = false;
				
				switch (type)
				{
					case Energy:
						flag = this.isEnergyFull();
						break;
					case Fluids:
						flag = FluidUtils.areTankFull(this);
						break;
					case Items:
						flag = InventoryUtil.isInventoryFull(this);
						break;
					default:
						break;
				}
				if (flag)
				{
					return 15;
				}
			}
		}
		return 0;
	}
	
	public boolean isEnergyFull()
	{
		return this.getEnergyStorage().isFull();
	}
	
	@Override
	public IOwner getOwners()
	{
		return null;
	}
	
	@Override
	public IInventory getFilterInventory()
	{
		return filter;
	}
	
	@Override
	public ItemStack getFilter(int id)
	{
		return filter.getStackInSlot(id);
	}
	
	@Override
	public IInventory getFilter()
	{
		return filter;
	}
	
}