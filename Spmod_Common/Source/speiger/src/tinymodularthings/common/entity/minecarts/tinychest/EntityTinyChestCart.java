package speiger.src.tinymodularthings.common.entity.minecarts.tinychest;

import mods.railcraft.api.carts.CartTools;
import mods.railcraft.api.carts.IItemTransfer;
import mods.railcraft.api.carts.ILinkageManager;
import mods.railcraft.api.core.items.IStackFilter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.entity.GuiTinyChestCart;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.entity.minecarts.TCarts;
import speiger.src.tinymodularthings.common.enums.EnumIDs;

public class EntityTinyChestCart extends TCarts implements
		IItemTransfer
{
	public EntityTinyChestCart(World world)
	{
		super(world);
	}

	public EntityTinyChestCart(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(23, new Integer(0));
	}

	public void setupSize(int par1)
	{
		this.dataWatcher.updateObject(23, new Integer(par1));
	}
	
	@Override
	public ItemStack getCartItem()
	{
		return new ItemStack(TinyItems.tinyStorageCart, 1, getSizeInventory() - 1);
	}
	
	@Override
	public ItemStack offerItem(Object source, ItemStack offer)
	{
		if ((getSizeInventory() > 0))
		{
			offer = moveItemStack(offer, this);
			if (offer == null)
			{
				return null;
			}
		}
		ILinkageManager lm = CartTools.getLinkageManager(worldObj);
		
		EntityMinecart linkedCart = lm.getLinkedCartA(this);
		if ((linkedCart != source) && ((linkedCart instanceof IItemTransfer)))
		{
			offer = ((IItemTransfer) linkedCart).offerItem(this, offer);
		}
		if (offer == null)
		{
			return null;
		}
		linkedCart = lm.getLinkedCartB(this);
		if ((linkedCart != source) && ((linkedCart instanceof IItemTransfer)))
		{
			offer = ((IItemTransfer) linkedCart).offerItem(this, offer);
		}
		return offer;
	}
	
	@Override
	public ItemStack requestItem(Object source)
	{
		return requestItem(this, new StackFilterHelper(new ItemStack[0]));
	}
	
	@Override
	public ItemStack requestItem(Object source, ItemStack request)
	{
		return requestItem(this, new StackFilterHelper(new ItemStack[] { request }));
	}
	
	@Override
	public ItemStack requestItem(Object source, IStackFilter request)
	{
		ItemStack result = null;
		if ((getSizeInventory() > 0))
		{
			result = removeOneItem(this, request);
			if (result != null)
			{
				return result;
			}
		}
		ILinkageManager lm = CartTools.getLinkageManager(worldObj);
		
		EntityMinecart linkedCart = lm.getLinkedCartA(this);
		if ((linkedCart != source) && ((linkedCart instanceof IItemTransfer)))
		{
			result = ((IItemTransfer) linkedCart).requestItem(this, request);
		}
		if (result != null)
		{
			return result;
		}
		linkedCart = lm.getLinkedCartB(this);
		if ((linkedCart != source) && ((linkedCart instanceof IItemTransfer)))
		{
			result = ((IItemTransfer) linkedCart).requestItem(this, request);
		}
		return result;
	}
	
	public ItemStack removeOneItem(IInventory inv, IStackFilter filter)
	{
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			if ((slot != null) && (filter.matches(slot)))
			{
				return inv.decrStackSize(i, 1);
			}
		}
		return null;
	}
	
	public ItemStack moveItemStack(ItemStack stack, IInventory dest)
	{
		if (stack == null)
		{
			return null;
		}
		stack = stack.copy();
		if (dest == null)
		{
			return stack;
		}
		boolean movedItem = false;
		do
		{
			movedItem = false;
			ItemStack destStack = null;
			for (int ii = 0; ii < dest.getSizeInventory(); ii++)
			{
				destStack = dest.getStackInSlot(ii);
				if ((destStack != null) && (destStack.isItemEqual(stack)))
				{
					int maxStack = Math.min(destStack.getMaxStackSize(), dest.getInventoryStackLimit());
					int room = maxStack - destStack.stackSize;
					if (room > 0)
					{
						int move = Math.min(room, stack.stackSize);
						destStack.stackSize += move;
						stack.stackSize -= move;
						if (stack.stackSize <= 0)
						{
							return null;
						}
						movedItem = true;
					}
				}
			}
			if (!movedItem)
			{
				for (int ii = 0; ii < dest.getSizeInventory(); ii++)
				{
					destStack = dest.getStackInSlot(ii);
					if (destStack == null)
					{
						if (stack.stackSize > dest.getInventoryStackLimit())
						{
							dest.setInventorySlotContents(ii, stack.splitStack(dest.getInventoryStackLimit()));
						}
						else
						{
							dest.setInventorySlotContents(ii, stack);
							return null;
						}
						movedItem = true;
					}
				}
			}
		}
		while (movedItem);
		return stack;
	}
	
	public double getDrag()
	{
		return 0.991999979019165D;
	}
	
	public static class StackFilterHelper implements IStackFilter
	{
		ItemStack[] stack;
		
		public StackFilterHelper(ItemStack[] itemStacks)
		{
			stack = itemStacks;
		}
		
		@Override
		public boolean matches(ItemStack stack)
		{
			if ((this.stack.length == 0) || (!hasFilter()))
			{
				return true;
			}
			
			return this.isItemEqual(stack, this.stack);
		}
		
		public boolean hasFilter()
		{
			for (ItemStack filter : stack)
			{
				if (filter != null)
				{
					return true;
				}
			}
			return false;
		}
		
		public boolean isItemEqual(ItemStack a, ItemStack b)
		{
			return isItemEqual(a, b, true, true);
		}
		
		public boolean isItemEqualIgnoreNBT(ItemStack a, ItemStack b)
		{
			return isItemEqual(a, b, true, false);
		}
		
		public boolean isItemEqual(ItemStack a, ItemStack b, boolean matchDamage, boolean matchNBT)
		{
			if ((a == null) || (b == null))
			{
				return false;
			}
			if (a.itemID != b.itemID)
			{
				return false;
			}
			if ((matchNBT) && (!ItemStack.areItemStackTagsEqual(a, b)))
			{
				return false;
			}
			if ((matchDamage) && (a.getHasSubtypes()))
			{
				if ((isWildcard(a)) || (isWildcard(b)))
				{
					return true;
				}
				if (a.getItemDamage() != b.getItemDamage())
				{
					return false;
				}
			}
			return true;
		}
		
		public boolean isItemEqual(ItemStack stack, ItemStack[] matches)
		{
			for (ItemStack match : matches)
			{
				if (isItemEqual(stack, match))
				{
					return true;
				}
			}
			return false;
		}
		
		public boolean isWildcard(ItemStack stack)
		{
			return isWildcard(stack.getItemDamage());
		}
		
		public boolean isWildcard(int damage)
		{
			return (damage == -1) || (damage == 32767);
		}
	}
	
	public String getInvName()
	{
		return "Tiny Chest Cart";
	}

	@Override
	public int getSizeInventory()
	{
		return this.dataWatcher.getWatchableObjectInt(23);
	}
	
    public boolean interactFirst(EntityPlayer par1)
    {
        if(MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1))) 
        {
            return true;
        }
        if (!this.worldObj.isRemote)
        {
        	par1.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, this.entityId, -1, 0);
        }

        return true;
    }

	@Override
	public boolean hasGui()
	{
		return true;
	}

	@Override
	public GuiContainer getGui(InventoryPlayer par0)
	{
		return new GuiTinyChestCart(getInventory(par0), this);
	}

	static int[][] SlotX = new int[][] { {}, { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };

	
	@Override
	public AdvContainer getInventory(InventoryPlayer par0)
	{
		AdvContainer cont = new AdvContainer(par0);
		for(int i = 0;i<this.getSizeInventory();i++)
		{
			cont.addSpmodSlotToContainer(new SpmodSlot(this, i,  SlotX[getSizeInventory()][i], 30));
		}
		cont.setInventory(par0);
		return cont;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound par1nbtTagCompound)
	{
		super.writeEntityToNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger("Sized", dataWatcher.getWatchableObjectInt(23));
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound par1nbtTagCompound)
	{
		int data = par1nbtTagCompound.getInteger("Sized");
		dataWatcher.updateObject(23, data);
		super.readEntityFromNBT(par1nbtTagCompound);
	}

	@Override
	public BlockStack getRenderedBlock()
	{
		return new BlockStack(TinyBlocks.storageBlock, 0);
	}

	@Override
	public String getEntityName()
	{
		return getInvName();
	}
	
	
	
	
	
	
}
