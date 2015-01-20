package speiger.src.tinymodularthings.common.blocks.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodCraftingSlot;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CraftingStation extends AdvTile
{
	public HashMap<String, CraftingInventory> iinv = new HashMap<String, CraftingInventory>();
	
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		Icon[] par1 = TextureEngine.getIcon(TinyBlocks.craftingBlock, 2);
		return side < 2 ? par1[side] : par1[2];
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
	public float getBlockHardness()
	{
		return 2F;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		par2.add("Crafting table that store items");
		par2.add("Every player has his own Inventory");
	}

	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 4F;
	}

	@Override
	public boolean canUpdate()
	{
		return false;
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
	public boolean tilehandlesItemMoving()
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(AdvContainer par1, EntityPlayer par2, int par3)
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)par1.inventorySlots.get(par3);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par3 >= 0 && par3 < 9)
            {
                if (!par1.mergeItemStack(itemstack1, 18, 54, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par3 >= 18 && par3 < 45)
            {
                if (!par1.mergeItemStack(itemstack1, 45, 54, false))
                {
                    return null;
                }
            }
            else if (par3 >= 45 && par3 < 54)
            {
                if (!par1.mergeItemStack(itemstack1, 18, 45, false))
                {
                    return null;
                }
            }
            else if (!par1.mergeItemStack(itemstack1, 18, 54, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par2, itemstack1);
        }

        return itemstack;
    }

	@Override
	public void registerIcon(TextureEngine par1, Block par2)
	{
		par1.registerTexture(par2, 2, "CraftingStation_Bottom", "CraftingStation_Top", "CraftingStation_Side");
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
	public void addContainerSlots(AdvContainer par1)
	{
		CraftingInventory inv = this.getInventoryFromPlayer(par1.getOwner());
		inv.setContainer(par1);
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlotToContainer(new SpmodSlot(inv, y + x * 3, 10 + y * 18, 17 + x * 18));
			}
		}
		for(int x = 0;x<3;x++)
		{
			for(int y = 0;y<3;y++)
			{
				par1.addSpmodSlotToContainer(new SpmodCraftingSlot(par1.getOwner(), inv, inv, 9+x+y*3, 110 + x * 18, 18 + y * 18));
			}
		}
	}
	
	@Override
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		super.onPlayerCloseContainer(par1);
		CraftingInventory inv = this.getInventoryFromPlayer(par1);
		inv.leaveContainer();
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(slotID < 9)
		{
			return true;
		}
		return super.canMergeItem(par1, slotID);
	}
	
	@Override
	public void onMatrixChanged(AdvContainer par1, IInventory par2)
	{
		CraftingInventory inv = getInventoryFromPlayer(par1.getOwner());
		if(inv != null)
		{
			inv.onMatrixChange(par2, worldObj);
		}
	}
	
	public static class CraftingInventory extends InventoryCrafting
	{
		ItemStack[] inv = new ItemStack[18];
		TileEntity owner;
		Container event;
		
		public CraftingInventory(TileEntity tile)
		{
			super(null, 3, 3);
			owner = tile;
		}
		public void leaveContainer()
		{
			event = null;
		}
		
		public CraftingInventory setContainer(Container par1)
		{
			event = par1;
			return this;
		}
		
		@Override
		public int getSizeInventory()
		{
			return 18;
		}
		
		public void onMatrixChange(IInventory par1, World par2)
		{
			List<IRecipe> recipes = (List<IRecipe>)((ArrayList)CraftingManager.getInstance().getRecipeList()).clone();
			ArrayList<IRecipe> results = new ArrayList<IRecipe>();
			for(IRecipe cuRecipe : recipes)
			{
				if(cuRecipe.matches(this, par2))
				{
					results.add(cuRecipe);
				}
			}
			if(results.size() > 9)
			{
				Collections.shuffle(results);
			}
			
			for(int i = 0;i<9;i++)
			{
				if(i < results.size())
				{
					setInventorySlotContents(9+i, results.get(i).getCraftingResult(this));
				}
				else
				{
					setInventorySlotContents(9+i, null);
				}
			}
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
			if(var1 < 9)
			{
				if(event != null)
				{
					event.onCraftMatrixChanged(this);
				}
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
