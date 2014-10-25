package speiger.src.tinymodularthings.common.blocks.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.utils.fluids.TinyFluidTank;
import forestry.core.config.ForestryItem;

public class OilGenerator extends AdvTile implements ISidedInventory, INBTReciver, IFluidHandler
{
	public FluidTank lavaTank = new TinyFluidTank("lava", 10000, this);
	public FluidTank oilTank = new TinyFluidTank("oil", 10000, this);
	public static int TotalTime = 50;
	public float StoredOil = 0.0F;
	
	public static HashMap<BlockPosition, ArrayList<OilEntry>> productions = new HashMap<BlockPosition, ArrayList<OilEntry>>();

	@Override
	public void onBreaking()
	{
		productions.remove(this.getPosition());
	}

	@Override
	public int getSizeInventory()
	{
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if(itemstack != null)
		{
			if(productions.get(this.getPosition()) == null)
			{
				productions.put(getPosition(), new ArrayList<OilEntry>());
			}
			productions.get(getPosition()).add(new OilEntry(itemstack.stackSize));
		}
	}

	@Override
	public String getInvName()
	{
		return "Oil Generator";
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1000;
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
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{0};
	}


	
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(FluidRegistry.isFluidRegistered("oil"))
		{
			int itemID = itemstack.itemID;
			if(itemID == Item.porkRaw.itemID || itemID == Item.beefRaw.itemID || itemID == Item.chickenRaw.itemID)
			{
				return true;
			}
			try
			{
				if(itemID == ForestryItem.beeDroneGE.itemID || itemID == ForestryItem.beeLarvaeGE.itemID || itemID == ForestryItem.beePrincessGE.itemID || itemID == ForestryItem.beeQueenGE.itemID)
				{
					return true;
				}
			}
			catch(Exception e)
			{
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			ArrayList<OilEntry> oil = productions.get(getPosition());
			if(oil != null && !oil.isEmpty())
			{
				for(int i = 0;i<oil.size();i++)
				{
					OilEntry entry = oil.get(i);
					if(!entry.hasWork())
					{
						oil.remove(i--);
						float add = (float)entry.amount / 100;
						StoredOil += add;
					}
				}
			}
			else if(oil == null)
			{
				productions.put(getPosition(), new ArrayList<OilEntry>());
			}
			
			for(;StoredOil > 1.0F;)
			{
				oilTank.fill(FluidRegistry.getFluidStack("oil", 1), true);
				StoredOil-=1F;
			}
		}
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	
	
	public class OilEntry
	{
		int amount;
		int tick = 0;
		
		public OilEntry(NBTTagCompound nbt)
		{
			readFromNBT(nbt);
		}
		
		public OilEntry(int amount)
		{
			this.amount = amount;
		}
		
		public boolean hasWork()
		{
			tick++;
			if(tick > OilGenerator.TotalTime)
			{
				return true;
			}
			return false;
		}
		
		public void readFromNBT(NBTTagCompound nbt)
		{
			amount = nbt.getInteger("Amount");
			tick = nbt.getInteger("Progress");
		}
		
		public void writeToNBT(NBTTagCompound nbt)
		{
			nbt.setInteger("Amount", amount);
			nbt.setInteger("Progress", tick);
		}
	}



	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		NBTTagList list = par1.getTagList("Progress");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			BlockPosition pos = new BlockPosition(nbt);
			ArrayList<OilEntry> oil = new ArrayList<OilEntry>();
			NBTTagList data = nbt.getTagList("Value");
			for(int z = 0;z<data.tagCount();z++)
			{
				NBTTagCompound key = (NBTTagCompound)data.tagAt(z);
				oil.add(new OilEntry(key));
			}
			productions.put(pos, oil);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList nbt = new NBTTagList();
		Iterator<Entry<BlockPosition, ArrayList<OilEntry>>> iter = productions.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<BlockPosition, ArrayList<OilEntry>> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			entry.getKey().writeToNBT(data);
			NBTTagList list = new NBTTagList();
			ArrayList<OilEntry> value = entry.getValue();
			for(OilEntry oil : value)
			{
				NBTTagCompound key = new NBTTagCompound();
				oil.writeToNBT(key);
				list.appendTag(key);
			}
			data.setTag("Value", list);
			nbt.appendTag(data);
		}
		
		par1.setTag("Progress", nbt);
	}

	@Override
	public void finishLoading()
	{
	}

	@Override
	public SpmodMod getOwner()
	{
		return TinyModularThings.instance;
	}

	@Override
	public String getID()
	{
		return "oilgenerator";
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(FluidRegistry.LAVA.getID() == resource.fluidID)
		{
			return lavaTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return this.drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return oilTank.drain(maxDrain, doDrain);
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
		return new FluidTankInfo[]{oilTank.getInfo()};
	}




	
	
}
