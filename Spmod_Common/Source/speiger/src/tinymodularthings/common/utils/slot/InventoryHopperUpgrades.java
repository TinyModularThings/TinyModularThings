package speiger.src.tinymodularthings.common.utils.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.hopper.HopperRegistry;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IHopperUpgradeItem;
import speiger.src.api.util.InventoryUtil;

public class InventoryHopperUpgrades implements IInventory
{
	ItemStack[] items = new ItemStack[40];
	HopperUpgrade[] upgrades = new HopperUpgrade[40];
	IHopper hopper;
	
	public InventoryHopperUpgrades(IHopper par1)
	{
		hopper = par1;
	}
	
	@Override
	public int getSizeInventory()
	{
		return items.length;
	}
	
	public ItemStack getStackInSlot(int par1)
	{
		return this.items[par1];
	}
	
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.items[par1] != null)
		{
			ItemStack itemstack;
			
			if (this.items[par1].stackSize <= par2)
			{
				itemstack = this.items[par1];
				this.items[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.items[par1].splitStack(par2);
				
				if (this.items[par1].stackSize == 0)
				{
					this.items[par1] = null;
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
		if (this.items[par1] != null)
		{
			ItemStack itemstack = this.items[par1];
			this.items[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.items[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public String getInvName()
	{
		return "HopperUpgrade";
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
	public void onInventoryChanged()
	{
		this.closeChest();
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
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null && upgrades[i] != null)
			{
				HopperUpgrade up = upgrades[i];
				hopper.removeUpgrade(up);
				upgrades[i] = null;
			}
			else if (items[i] != null && upgrades[i] == null)
			{
				HopperUpgrade up = ((IHopperUpgradeItem) items[i].getItem()).getUpgrade(items[i]);
				if (hopper.addUpgrade(up))
				{
					upgrades[i] = up;
				}
			}
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.items.length; ++i)
		{
			if (this.items[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.items[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
		NBTTagList up = new NBTTagList();
		for (int i = 0; i < upgrades.length; i++)
		{
			if (upgrades[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				nbttagcompound1.setString("ID", upgrades[i].getNBTName());
				upgrades[i].onNBTWrite(nbttagcompound1);
				up.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Upgrades", up);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList nbttaglist = nbt.getTagList("Items");
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			
			if (b0 >= 0 && b0 < this.items.length)
			{
				this.items[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		NBTTagList up = nbt.getTagList("Upgrades");
		for (int i = 0; i < up.tagCount(); ++i)
		{
			NBTTagCompound data = (NBTTagCompound) up.tagAt(i);
			byte by = data.getByte("Slot");
			if (by >= 0 && by < this.upgrades.length)
			{
				HopperUpgrade Updata = HopperRegistry.getHopperUpgradeFromNBT(data.getString("ID"));
				Updata.onNBTRead(data);
				upgrades[by] = Updata;
			}
		}
	}
	
	public ArrayList<HopperUpgrade> getAllUpgrades()
	{
		ArrayList<HopperUpgrade> upgrade = new ArrayList<HopperUpgrade>();
		for (ItemStack stack : items)
		{
			if (stack != null)
			{
				upgrade.add(((IHopperUpgradeItem) stack.getItem()).getUpgrade(stack));
			}
		}
		return upgrade;
	}
	
	public void addUpgrade(ItemStack item, HopperUpgrade par1)
	{
		ItemStack stack = InventoryUtil.splitStack(item, 1);
		if (stack != null && par1 != null)
		{
			int slot = getFreeSlot();
			if (slot != -1)
			{
				items[slot] = stack;
				upgrades[slot] = par1;
				return;
			}
		}
		item.stackSize++;
	}
	
	public int getFreeSlot()
	{
		for (int i = 0; i < items.length; i++)
		{
			if (items[i] == null)
			{
				if (upgrades[i] != null)
				{
					hopper.removeUpgrade(upgrades[i]);
					upgrades[i] = null;
				}
				if (upgrades[i] != null)
				{
					continue;
				}
				return i;
			}
		}
		return -1;
	}
}
