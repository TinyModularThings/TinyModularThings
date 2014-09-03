package speiger.src.tinymodularthings.common.blocks.machine;

import java.io.DataInput;
import java.util.ArrayList;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.energy.EnergyProvider;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.util.InventoryUtil;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.tile.AdvancedFluidTank;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.machine.BucketFillerGui;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.plugins.BC.actions.BucketFillerAction;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import buildcraft.api.inventory.ISpecialInventory;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BucketFillerBasic extends AdvTile implements ISpecialInventory, ISidedInventory, IEnergyProvider, IFluidHandler, IPacketReciver, IPowerReceptor, IActionReceptor
{
	public ItemStack[] inv = new ItemStack[2];
	public AdvancedFluidTank tank = new AdvancedFluidTank(this, "bucketFiller", 16000);
	public EnergyProvider provider = new EnergyProvider(this, 10000).setPowerLoss(2);
	
	public static ArrayList<FluidContainerData> recipes = new ArrayList<FluidContainerData>();
	
	public boolean drain = false;
	public int progress = 0;
	public int max = 200;
	public FluidContainerData cuRecipe = null;
	public static boolean start = false;
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new InventoryBucketFiller(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new BucketFillerGui(par1, this);
	}

	
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(hasContainer() && !worldObj.isRemote)
		{
			par1.openGui(TinyModularThings.instance, EnumIDs.ADVTiles.getId(), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}

	@Override
    public int getSizeInventory()
    {
        return this.inv.length;
    }

	@Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.inv[par1];
    }

    @Override
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
    
    

    @Override
	public void onTick()
	{
		super.onTick();
		if(!worldObj.isRemote)
		{
			provider.update();
			
			if(!start)
			{
				start = true;
				for(FluidContainerData cu : FluidContainerRegistry.getRegisteredFluidContainerData())
				{
					this.recipes.add(cu);
				}
			}
			
			if(cuRecipe != null && !hasRecipe())
			{
				cuRecipe = null;
			}
			if(cuRecipe == null)
			{
				cuRecipe = findRecipe();
			}
			if(worldObj.getWorldTime() % 10 == 0)
			{
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
			
			
			if(cuRecipe != null && provider.getStoredEnergy() > 10 && canWork())
			{
				provider.useEnergy(10, false);
				progress++;
				if(progress >= max)
				{
					progress = 0;
					if(drain)
					{
						ItemStack output = cuRecipe.emptyContainer.copy();
						tank.fill(cuRecipe.fluid, true);
						
						if(inv[1] == null)
						{
							inv[1] = output;
						}
						else if(inv[1] != null)
						{
							inv[1].stackSize++;
						}
						
						inv[0].stackSize--;
						if(inv[0].stackSize <= 0)
						{
							inv[0] = null;
						}
					}
					else
					{
						ItemStack output = cuRecipe.filledContainer.copy();
						tank.drain(cuRecipe.fluid.amount, true);
						
						if(inv[1] == null)
						{
							inv[1] = output;
						}
						else if(inv[1] != null)
						{
							inv[1].stackSize++;
						}
						
						inv[0].stackSize--;
						if(inv[0].stackSize <= 0)
						{
							inv[0] = null;
						}
					}
					this.onInventoryChanged();
					this.updateBlock();
				}
			}
			else
			{
				this.progress = 0;
			}
			
		}
	}
    
    public boolean canWork()
	{
    	if(drain)
    	{
    		ItemStack stack = this.cuRecipe.emptyContainer;
    		if(inv[1] == null)
    		{
    			return true;
    		}
    		else if(inv[1] != null && inv[1].isItemEqual(stack))
    		{
    			int max = inv[1].getMaxStackSize();
    			return inv[1].stackSize + 1 <= max;
    		}
    	}
    	else
    	{
    		ItemStack stack = this.cuRecipe.filledContainer;
    		if(inv[1] == null)
    		{
    			return true;
    		}
    		else if(inv[1] != null && inv[1].isItemEqual(stack))
    		{
    			int max = inv[1].getMaxStackSize();
    			return inv[1].stackSize + 1 <= max;
    		}
    	}
		return false;
	}

	public FluidContainerData findRecipe()
	{
    	if(inv[0] == null)
    	{
    		return null;
    	}
    	
    	if(drain)
    	{
    		
    		FluidContainerData list = getListFromInputItem();
    		if(list == null)
    		{
    			return null;
    		}
    		
    		if(tank.fill(list.fluid, false) != list.fluid.amount)
    		{
    			return null;
    		}
    		if(inv[1] != null && inv[1].stackSize + 1 > 64)
    		{
    			return null;
    		}
    		return list;
    		
    	}
    	else
    	{
    		
    		ArrayList<FluidContainerData> list = getListFromFluid();
    		FluidContainerData end = null;
    		for(FluidContainerData data : list)
    		{
    			if(data.emptyContainer.isItemEqual(inv[0]))
    			{
    				end = data;
    				break;
    			}
    		}
    		if(end != null)
    		{
    			if(tank.drain(end.fluid.amount, false).isFluidStackIdentical(end.fluid))
    			{
    				if(inv[1] == null || (inv[1] != null && inv[1].isItemEqual(end.filledContainer) && inv[1].stackSize + 1 <= 64))
    				{
    					return end;
    				}
    			}
    		}
    	}
		return null;
	}
    
    public ArrayList<FluidContainerData> getListFromFluid()
    {
    	ArrayList<FluidContainerData> data = new ArrayList<FluidContainerData>();
    	if(tank.getFluid() == null)
    	{
    		return data;
    	}
    	
    	for(FluidContainerData cu : recipes)
    	{
    		if(cu.fluid.isFluidEqual(tank.getFluid()))
    		{
    			data.add(cu);
    		}
    	}
    	return data;
    }
    
    public FluidContainerData getListFromInputItem()
    {
    	
    	if(inv[0] == null)
    	{
    		return null;
    	}
    	
    	for(FluidContainerData cu : recipes)
    	{
    		if(cu.filledContainer.isItemEqual(inv[0]))
    		{
    			return cu;
    		}
    	}
    	return null;
    }

	public boolean hasRecipe()
    {
    	if(cuRecipe == null)
    	{
    		return false;
    	}
    	if(inv[0] == null)
    	{
    		return false;
    	}
    	if(drain)
    	{
    		if(!cuRecipe.filledContainer.isItemEqual(inv[0]))
    		{
    			return false;
    		}
    		if(this.tank.fill(cuRecipe.fluid, false) != cuRecipe.fluid.amount)
    		{
    			return false;
    		}
    		if(inv[1] != null && !inv[1].isItemEqual(cuRecipe.emptyContainer))
    		{
    			return false;
    		}
    		if(inv[1] != null && inv[1].stackSize + 1 > 64)
    		{
    			return false;
    		}
    	}
    	else
    	{
    		if(!cuRecipe.emptyContainer.isItemEqual(inv[0]))
    		{
    			return false;
    		}
    		if(tank.getFluid() == null || !tank.getFluid().isFluidEqual(cuRecipe.fluid))
    		{
    			return false;
    		}
    		if(tank.getFluidAmount() < cuRecipe.fluid.amount)
    		{
    			return false;
    		}
    		if(inv[1] != null && !inv[1].isItemEqual(cuRecipe.filledContainer))
    		{
    			return false;
    		}
    		if(inv[1] != null && inv[1].stackSize + 1 > 64)
    		{
    			return false;
    		}
    	}
    	
    	return true;
    }

	@Override
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

    @Override
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
		return "BasicBucketFiller";
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
		return this.containesContainer(itemstack);
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
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return provider;
	}
	
	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from)
	{
		if(this.containesContainer(stack))
		{
			ItemStack cu = inv[0];
			if(cu != null)
			{
				if(cu.isItemEqual(stack))
				{
					if(cu.stackSize >= cu.getMaxStackSize())
					{
						return 0;
					}
					
					int left = stack.getMaxStackSize() - (stack.stackSize + cu.stackSize);
					if(left < 0)
					{
						if(doAdd)
						{
							cu.stackSize = cu.getMaxStackSize();
							inv[0] = cu;
						}
						return stack.stackSize - left;
					}
					else
					{
						if(doAdd)
						{
							cu.stackSize += stack.stackSize;
							inv[0] = cu;
						}
						return stack.stackSize;
					}
				}
			}
			else
			{
				if(doAdd)
				{
					inv[0] = stack;
				}
				return stack.stackSize;
			}
		}
		return 0;
	}
	
	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount)
	{
		if(inv[1] == null)
		{
			return new ItemStack[0];
		}
		ItemStack stack = this.inv[1].copy();
		ItemStack removed = InventoryUtil.splitStack(stack, maxItemCount);
		
		if(doRemove)
		{
			this.inv[1] = stack;
		}
		
		return new ItemStack[]{removed};
	}
	

	
	public boolean containesContainer(ItemStack par1)
	{
		for(FluidContainerData cu : recipes)
		{
			if(drain)
			{
				if(cu.filledContainer.isItemEqual(par1))
				{
					return true;
				}
			}
			else
			{
				if(cu.emptyContainer.isItemEqual(par1))
				{
					return true;
				}
			}
		}
		return false;
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
		catch (Exception e)
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
					BucketFillerAction bc = (BucketFillerAction) action;
					drain = !bc.fill;
				}
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	

	
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt);
		this.provider.readFromNBT(nbt);
		drain = nbt.getBoolean("drain");
		progress = nbt.getInteger("progress");
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.tank.writeToNBT(nbt);
		this.provider.writeToNBT(nbt);
		nbt.setBoolean("drain", drain);
        nbt.setInteger("progress", progress);
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
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		this.readFromNBT(pkt.data);
	}

	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		switch(key)
		{
			case 0:
				if(tank.getFluid() == null)
				{
					tank.setFluid(new FluidStack(key, val));
				}
				else
				{
					tank.getFluid().fluidID = val;
				}
				break;
			case 1:
				if(tank.getFluid() == null)
				{
					tank.setFluid(new FluidStack(0, val));
				}
				else
				{
					tank.getFluid().amount = val;
				}
				break;
		}
	}

	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, tank.getFluid() != null ? tank.getFluid().fluidID : 0);
		par2.sendProgressBarUpdate(par1, 1, tank.getFluid() != null ? tank.getFluid().amount : 0);
	}

	@Override
	public String identifier()
	{
		return null;
	}

	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(TinyBlocks.machine, 1, this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		if(var1 < 2)
		{
			return new int[]{0};
		}
		return new int[]{1};
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
	
	
	
}
