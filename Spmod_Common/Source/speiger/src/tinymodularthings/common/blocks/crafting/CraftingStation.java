package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.crafting.CraftingStationGui;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CraftingStation extends AdvTile
{
	public HashMap<String, CraftingInventory> iinv = new HashMap<String, CraftingInventory>();
	
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine par1 = TextureEngine.getTextures();
		return side == 0 ? par1.getTexture(TinyBlocks.craftingBlock, 0) : side == 1 ? par1.getTexture(TinyBlocks.craftingBlock, 1) : par1.getTexture(TinyBlocks.craftingBlock, 2);
	}
	
	public CraftingInventory getInventoryFromPlayer(EntityPlayer par1)
	{
		if(!iinv.containsKey(par1.username))
		{
			CraftingInventory inv = new CraftingInventory(this);
			iinv.put(par1.username, inv);
		}
		return iinv.get(par1.username);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList list = nbt.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			CraftingInventory inv = new CraftingInventory(this);
			inv.readFromNBT(data);
			iinv.put(data.getString("ID"), inv);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		Iterator<Entry<String, CraftingInventory>> iter = iinv.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<String, CraftingInventory> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("ID", entry.getKey());
			entry.getValue().writeToNBT(data);
			list.appendTag(data);
		}
		nbt.setTag("Data", list);
	}
	
	
	
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}

	
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if(!worldObj.isRemote)
		{
			par1.openGui(TinyModularThings.instance, EnumIDs.ADVTiles.getId(), getWorldObj(), xCoord, yCoord, zCoord);
		}
		return true;
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> stack = super.onDrop(fortune);
		for(CraftingInventory inv : iinv.values())
		{
			for(int i = 0;i<inv.inv.length;i++)
			{
				ItemStack cu = inv.getStackInSlot(i);
				if(cu != null)
				{
					stack.add(cu);
				}
			}
		}
		return stack;
	}
	
	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new CraftingStationInventory(par1, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new CraftingStationGui(par1, this);
	}




	public static class CraftingInventory extends InventoryCrafting
	{
		ItemStack[] inv = new ItemStack[18];
		TileEntity owner;
		private static Container event;
		
		public CraftingInventory(TileEntity tile)
		{
			super(event, 3, 3);
			owner = tile;
		}
		
		public void setContainer(Container par1)
		{
			event = par1;
		}
		
		public void leaveContainer()
		{
			event = null;
		}
		
		@Override
		public int getSizeInventory()
		{
			return 9;
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
					if(event != null)
					{
						event.onCraftMatrixChanged(this);
					}
					return var3;
				}
				else
				{
					var3 = inv[var1].splitStack(var2);
					
					if (inv[var1].stackSize == 0)
					{
						inv[var1] = null;
					}
					if(event != null)
					{
						event.onCraftMatrixChanged(this);
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
			if(event != null)
			{
				event.onCraftMatrixChanged(this);
			}
		}

		@Override
		public String getInvName()
		{
			return "Crafting Inventory";
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
			owner.onInventoryChanged();
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
		
		public void readFromNBT(NBTTagCompound nbt)
		{
	        NBTTagList nbttaglist = nbt.getTagList("Items");
	        this.inv = new ItemStack[18];

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
		
		public void writeToNBT(NBTTagCompound nbt)
		{
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
		
	}
	
}
