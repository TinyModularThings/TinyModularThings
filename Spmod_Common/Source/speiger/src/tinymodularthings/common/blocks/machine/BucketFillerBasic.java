package speiger.src.tinymodularthings.common.blocks.machine;

import java.io.DataInput;
import java.util.HashMap;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerRegisterEvent;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.data.utils.ItemData;
import speiger.src.api.common.inventory.slot.TankSlot;
import speiger.src.api.common.world.tiles.energy.EnergyProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BucketFillerAction;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BucketFillerBasic extends AdvInventory implements ISidedInventory,
		IEnergyProvider, IFluidHandler, IPacketReciver, IPowerReceptor,
		IActionReceptor
{
	
	public AdvancedFluidTank tank = new AdvancedFluidTank(this, "bucketFiller", 16000);
	public EnergyProvider provider = new EnergyProvider(this, 10000).setPowerLoss(2);
	
	public static HashMap<FluidItemData, FluidContainerData> fillList = new HashMap<FluidItemData, FluidContainerData>();
	public static HashMap<IStackInfo, FluidContainerData> drainList = new HashMap<IStackInfo, FluidContainerData>();
	
	public boolean drain = false;
	public int progress = 0;
	public int max = 200;
	public FluidContainerData cuRecipe = null;
	
	public BucketFillerBasic()
	{
		super(2);
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public float getBlockHardness()
	{
		return 3.5F;
	}
	
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 6F;
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		if(worldObj.isRemote)
		{
			return;
		}
		provider.update();
		if(provider.getStoredEnergy() < 10)
		{
			return;
		}
		if(cuRecipe != null && !hasRecipe())
		{
			cuRecipe = null;
		}
		if(cuRecipe == null && hasRecipe())
		{
			cuRecipe = getRecipe();
		}
		if(provider.getStoredEnergy() >= 10)
		{
			if(cuRecipe != null && canRun())
			{
				provider.useEnergy(10, false);
				progress++;
				if(progress >= max)
				{
					progress = 0;
					ItemStack result = null;
					if(drain) 
					{
						result = cuRecipe.emptyContainer.copy();
						tank.fill(cuRecipe.fluid, true);
					}
					else 
					{
						result = cuRecipe.filledContainer.copy();
						tank.drain(cuRecipe.fluid.amount, true);
					}
					
					inv[0].stackSize--;
					if(inv[0].stackSize <= 0)
					{
						inv[0] = null;
					}
					if(inv[1] == null)
					{
						inv[1] = result;
					}
					else
					{
						inv[1].stackSize += result.stackSize;
					}
				}
			}
			else if(cuRecipe == null && inv[0] != null)
			{
				if(inv[0].getItem() instanceof IFluidContainerItem)
				{
					IFluidContainerItem fluid = (IFluidContainerItem)inv[0].getItem();
					if(drain)
					{
						if(canFillFluid(fluid.getFluid(inv[0])))
						{
							provider.useEnergy(10, false);
							FluidStack fill = fluid.drain(inv[0], 100, false);
							if((fill == null || fill.amount <= 0) && inv[1] == null)
							{
								inv[1] = inv[0].copy();
								inv[0] = null;
								return;
							}
							int amount = this.tank.fill(fill, true);
							fluid.drain(inv[0], amount, true);
							provider.useEnergy(10, false);
						}
						else if(fluid.getFluid(inv[0]) == null && inv[1] == null)
						{
							inv[1] = inv[0].copy();
							inv[0] = null;
						}
					}
					else
					{
						FluidStack drained = tank.drain(100, false);
						if(drained != null && drained.amount > 0)
						{
							provider.useEnergy(10, false);
							int filled = fluid.fill(inv[0], drained, true);
							if(filled <= 0 && inv[1] == null)
							{
								inv[1] = inv[0].copy();
								inv[0] = null;
								return;
							}
							tank.drain(filled, true);
							provider.useEnergy(10, false);
						}
						else if(inv[1] == null)
						{
							inv[1] = inv[0].copy();
							inv[0] = null;
						}
					}
				}
				else if(inv[0].itemID == TinyItems.netherCrystal.itemID && drain)
				{
					if(inv[0].getItemDamage() == 3)
					{
						if(tank.getFluid() == null || (tank.getFluid().isFluidEqual(new FluidStack(FluidRegistry.LAVA, 1)) && tank.fill(new FluidStack(FluidRegistry.LAVA, 1000), false) == 1000))
						{
							progress++;
							provider.useEnergy(10, false);
							if(progress >= this.max)
							{
								progress = 0;
								tank.fill(new FluidStack(FluidRegistry.LAVA, 1000), true);
								inv[0] = inv[0].getItem().getContainerItemStack(inv[0]);
								if(inv[0].getItemDamage() != 3 && inv[1] == null)
								{
									inv[1] = inv[0].copy();
									inv[0] = null;
								}
							}
						}
					}
					else
					{
						inv[1] = inv[0].copy();
						inv[0] = null;
						progress = 0;
					}

				}
				else
				{
					this.progress = 0;
				}
			}
			else if(inv[0] != null && inv[1] == null)
			{
				inv[1] = inv[0].copy();
				inv[0] = null;
				progress = 0;
			}
			else
			{
				this.progress = 0;
			}
		}
	}
	
	private boolean canFillFluid(FluidStack fluid)
	{
		if(fluid == null)
		{
			return false;
		}
		
		if(this.tank.getFluid() != null && !this.tank.getFluid().isFluidEqual(fluid))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean canRun()
	{
		if(inv[1] == null)
		{
			return true;
		}
		ItemStack result = null;
		if(drain)
		{
			result = cuRecipe.emptyContainer;
			if(tank.getFluidAmount() + cuRecipe.fluid.amount > tank.getCapacity())
			{
				return false;
			}
		}
		else
		{
			result = cuRecipe.filledContainer;
			if(cuRecipe.fluid.amount > tank.getFluidAmount())
			{
				return false;
			}
		}
		
		if(!inv[1].isItemEqual(result))
		{
			return false;
		}
		return inv[1].stackSize + 1 < inv[1].getMaxStackSize();
	}
	
	public boolean hasRecipe()
	{
		return getRecipe() != null;
	}
	
	public FluidContainerData getRecipe()
	{
		if(inv[0] == null)
		{
			return null;
		}
		
		if(drain)
		{
			return drainList.get(new ItemData(inv[0]));
		}
		else
		{
			FluidContainerData data = fillList.get(new FluidItemData(new ItemData(inv[0]), tank.getFluid()));
			if(data != null && tank.getFluidAmount() >= data.fluid.amount)
			{
				return data;
			}
		}
		return null;
	}
	
	@Override
	public String getInvName()
	{
		return "Basic Bucket Filler";
	}
	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.addSpmodSlot(this, 0, 50, 41).addUsage("Input Slot");
		par1.addSpmodSlot(this, 1, 104, 41).addUsage("Output Slot");
		par1.addTankSlot(new TankSlot(tank, 150, 15));
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiLoad(GuiInventoryCore par1, int guiX, int guiY)
	{
		par1.getButtonsList().clear();
		par1.getButtonsList().add(new GuiButton(0, guiX + 15, guiY + 37, 30, 20, drain ? "Drain" : "Fill"));
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		String change = !drain ? "Drain" : "Fill";
		if(par2.id == 0)
		{
			par2.displayString = change;
			this.sendPacketToServer(SpmodPacketHelper.getHelper().createNBTPacket(this, TinyModularThings.instance).InjectNumber(!drain ? 0 : 1).finishPacket());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		par1.setTexture(getEngine().getTexture("Objects"));
		par1.defineSlot("ProgBarH");
		par1.drawSlotPros(75, 41, 22, 16);
		if(progress > 0)
		{
			int maximum = (int)(((double)progress / (double)max) * 22);
			par1.defineSlot("ProgBarHOverlay");
			par1.drawSlotPros(75, 41, maximum, 16);
		}
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(slotID == 0 && par1 != null)
		{
			if(drain)
			{
				boolean flag = par1.getItem() instanceof IFluidContainerItem;
				return flag || FluidContainerRegistry.isFilledContainer(par1);
			}
			else
			{
				boolean flag = par1.getItem() instanceof IFluidContainerItem;
				return flag || FluidContainerRegistry.isEmptyContainer(par1);
			}
		}
		return false;
	}
	
	

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return tank.fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(from, resource.amount, doDrain);
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
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
		return new FluidTankInfo[] {tank.getInfo() };
	}
	
	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}
	
	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			if(!worldObj.isRemote)
			{
				this.drain = par1.readInt() == 1 ? false : true;
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
				
			}
		}
		catch(Exception e)
		{
		}
		
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return provider.getSaveBCPowerProvider();
	}
	
	@Override
	public void doWork(PowerHandler workProvider)
	{
		
	}
	
	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		if(action != null && !worldObj.isRemote)
		{
			try
			{
				if(action instanceof BucketFillerAction)
				{
					BucketFillerAction bc = (BucketFillerAction)action;
					drain = !bc.fill;
				}
			}
			catch(Exception e)
			{
				
			}
		}
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return TextureEngine.getTextures().getTexture(TinyBlocks.machine, 1, side == 0 ? 1 : side == 1 ? 0 : 2);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt);
		this.provider.readFromNBT(nbt);
		drain = nbt.getBoolean("drain");
		progress = nbt.getInteger("progress");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.tank.writeToNBT(nbt);
		this.provider.writeToNBT(nbt);
		nbt.setBoolean("drain", drain);
		nbt.setInteger("progress", progress);
	}
	
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		if(key == 0)
		{
			if(tank.getFluid() == null)
			{
				tank.setFluid(new FluidStack(val, 0));
			}
			else
			{
				tank.getFluid().fluidID = val;
			}
		}
		if(key == 1)
		{
			if(tank.getFluid() == null)
			{
				tank.setFluid(new FluidStack(0, val));
			}
			else
			{
				tank.getFluid().amount = val;
			}
		}
		if(key == 2)
		{
			progress = val;
		}
	}
	
	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, tank.getFluid() != null ? tank.getFluid().fluidID : 0);
		par2.sendProgressBarUpdate(par1, 1, tank.getFluid() != null ? tank.getFluid().amount : 0);
		par2.sendProgressBarUpdate(par1, 2, progress);
	}
	
	@Override
	public String identifier()
	{
		return null;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		if(var1 < 2)
		{
			return new int[] {0 };
		}
		return new int[] {1 };
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(!(i < 2))
		{
			return false;
		}
		return FluidContainerRegistry.isContainer(itemstack) || FluidContainerRegistry.isBucket(itemstack);
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return true;
	}
	
	@Override
	public boolean requireForgeRegistration()
	{
		return true;
	}
	
	@ForgeSubscribe
	public void onLoad(FluidContainerRegisterEvent evt)
	{
		for(FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData())
		{
			if(data.filledContainer != null && data.emptyContainer != null && data.filledContainer.itemID != TinyItems.netherCrystal.itemID)
			{
				fillList.put(new FluidItemData(new ItemData(data.emptyContainer), data.fluid), data);
				drainList.put(new ItemData(data.filledContainer), data);
			}
		}
	}
	
	public static class FluidItemData
	{
		ItemData data;
		FluidStack fluid;
		
		public FluidItemData(ItemData par1, FluidStack par2)
		{
			data = par1;
			fluid = par2;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == null || !(obj instanceof FluidItemData))
			{
				return false;
			}
			FluidItemData data = (FluidItemData)obj;
			if(data.data == null || data.fluid == null || this.data == null || fluid == null)
			{
				return false;
			}
			if(fluid.equals(data.fluid) && this.data.equals(data.data))
			{
				return true;
			}
			return false;
		}
		
		@Override
		public int hashCode()
		{
			return data.hashCode() + (fluid != null ? fluid.hashCode() : 0);
		}
	}

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyBlocks.machine, 1, 1);
	}
}
