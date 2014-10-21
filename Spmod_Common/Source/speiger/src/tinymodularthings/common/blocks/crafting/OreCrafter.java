package speiger.src.tinymodularthings.common.blocks.crafting;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.util.InventoryUtil;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.client.gui.crafting.GuiOreCrafter;
import speiger.src.tinymodularthings.common.config.TinyConfig;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class OreCrafter extends TileFacing implements IPacketReciver, IInventory, ISidedInventory
{
	ItemStack[] inv = new ItemStack[48];
	ArrayList<ItemStack> currentItems = new ArrayList<ItemStack>();
	boolean packetRequired = false;
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	public void oreUpdate()
	{
		if(inv[13] == null)
		{
			if(!currentItems.isEmpty())
			{
				currentItems.clear();
			}
		}
		else
		{
			if(currentItems.isEmpty())
			{
				updateOre();
			}
			else
			{
				if(OreDictionary.getOreID(inv[13]) != OreDictionary.getOreID(currentItems.get(0)))
				{
					currentItems.clear();
					updateOre();
				}
			}
		}
	}
	
	public boolean addItemsToInventory(ItemStack par1)
	{
		if(par1 != null)
		{
			if(inv[14] == null)
			{
				inv[14] = par1.copy();
				return true;
			}
			else if(inv[14] != null && inv[14].isItemEqual(par1) && inv[14].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[14].stackSize += par1.stackSize;
				return true;
			}
			else if(inv[15] == null)
			{
				inv[15] = par1.copy();
				return true;				
			}
			else if(inv[15] != null && inv[15].isItemEqual(par1) && inv[15].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[15].stackSize += par1.stackSize;
				return true;				
			}
			else if(inv[16] == null)
			{
				inv[16] = par1.copy();
				return true;
			}
			else if(inv[16] != null && inv[16].isItemEqual(par1) && inv[16].stackSize + par1.stackSize <= par1.getMaxStackSize())
			{
				inv[16].stackSize += par1.stackSize;
				return true;
			}
		}
		return false;
	}
	
	
	
	@Override
	public void onBreaking()
	{
		if(!worldObj.isRemote)
		{
			InventoryUtil.dropInventory(worldObj, xCoord, yCoord, zCoord, this);
		}
	}

	public void updateOre()
	{
		int id = OreDictionary.getOreID(inv[13]);
		if(id != -1 && TinyConfig.allowedOres.isAllowed(OreDictionary.getOreName(id)))
		{
			ArrayList<ItemStack> stacks = (ArrayList<ItemStack>)OreDictionary.getOres(id).clone();
			for(ItemStack stack : stacks)
			{
				if(stack.getItemDamage() == PathProxy.getRecipeBlankValue())
				{
					ArrayList<ItemStack> list = new ArrayList<ItemStack>();
					stack.getItem().getSubItems(stack.itemID, null, list);
					currentItems.addAll(list);
				}
				else
				{
					currentItems.add(stack);
				}
			}
			for(int i = 0;i<currentItems.size();i++)
			{
				ItemStack stack = currentItems.get(i);
				if(stack.isItemEqual(inv[13]))
				{
					currentItems.remove(i);
				}
			}
		}
	}
	
	public void handleOpenInventory()
	{
		if(worldObj.getWorldTime() % 30 == 0)
		{
			if(!currentItems.isEmpty())
			{
				if(currentItems.size() > 9)
				{
					currentItems.add(currentItems.remove(0));
				}
			}
		}
		for(int i = 0;i<10;i++)
		{
			if(i < currentItems.size())
			{
				inv[i] = currentItems.get(i);
			}
			else
			{
				inv[i] = null;
			}
		}
	}
	
	public void handleIncomingItems()
	{
		for(int i = 0;i<3;i++)
		{
			if(inv[10+i] != null)
			{
				ItemStack copy = inv[10+i].copy();
				copy = transformItem(copy);
				if(this.addItemsToInventory(copy))
				{
					inv[10+i] = null;
					this.onInventoryChanged();
				}
			}
		}
	}
	
	public ItemStack transformItem(ItemStack copy)
	{
		HashMap<Integer, Integer> ints = new HashMap<Integer, Integer>();
		for(int i = 17;i<this.inv.length;i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				ints.put(OreDictionary.getOreID(stack), i);
			}
		}
		if(ints.get(OreDictionary.getOreID(copy)) != null)
		{
			int id = OreDictionary.getOreID(copy);
			if(TinyConfig.allowedOres.isAllowed(OreDictionary.getOreName(id)))
			{
				int result = ints.get(id);
				ItemStack itemResult = inv[result].copy();
				itemResult.stackSize = copy.stackSize;
				return itemResult;
			}
		}
		
		return copy;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			oreUpdate();
			handleOpenInventory();
			handleIncomingItems();
			if(packetRequired)
			{
				packetRequired = false;
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
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
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<this.getSizeInventory();i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("Slot", i);
				stack.writeToNBT(data);
				list.appendTag(data);
			}
		}
		nbt.setTag("Item", list);		
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		inv = new ItemStack[this.getSizeInventory()];
		NBTTagList list = nbt.getTagList("Item");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			int slot = data.getInteger("Slot");
			if(slot >= 0 && slot < inv.length)
			{
				inv[slot] = ItemStack.loadItemStackFromNBT(data);
			}
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		try
		{
			int slot = par1.readInt();
			this.slotClick(slot);
		}
		catch(Exception e)
		{
		}
	}
	
	public void slotClick(int slot)
	{
		if(inv[13] != null)
		{
			ItemStack stack = inv[slot].copy();
			stack.stackSize = inv[13].stackSize;
			if(this.addItemsToInventory(stack))
			{
				inv[13] = null;
				this.onInventoryChanged();
			}
		}
	}
	
	@Override
	public String identifier()
	{
		return null;
	}


	@Override
	public int getSizeInventory()
	{
		return inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return inv[var1];
	}
	
	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		
		if (inv[var1] != null)
		{
			ItemStack var3;
			
			if (inv[var1].stackSize <= var2)
			{
				var3 = inv[var1];
				inv[var1] = null;
				return var3;
			}
			else
			{
				var3 = inv[var1].splitStack(var2);
				
				if (inv[var1].stackSize == 0)
				{
					inv[var1] = null;
				}
				
				return var3;
			}
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{

		if (inv[var1] != null)
		{
			ItemStack var2 = inv[var1];
			inv[var1] = null;
			return var2;
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
		inv[var1] = var2;
		
		if (var2 != null && var2.stackSize > getInventoryStackLimit())
		{
			var2.stackSize = getInventoryStackLimit();
		}
		
	}
	

	@Override
	public String getInvName()
	{
		return "Ore Crafter";
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
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new OreCrafterInventory(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiOreCrafter(par1, this);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[]{10, 11, 12, 14, 15, 16};
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(i >= 10 && i <=12)
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		if(i >= 14 && i <= 16)
		{
			return true;
		}
		return false;
	}
	
}
