package speiger.src.tinymodularthings.common.entity.minecarts.tinychest;

import mods.railcraft.api.carts.CartTools;
import mods.railcraft.api.carts.IItemTransfer;
import mods.railcraft.api.carts.ILinkageManager;
import mods.railcraft.api.core.items.IStackFilter;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.storage.GuiTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChestInventory;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.entity.minecarts.TCarts;
import speiger.src.tinymodularthings.common.enums.EnumIDs;

public abstract class EntityTinyChestCart extends TCarts implements
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
	
	public static class OneSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public OneSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public OneSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 1;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
		
	}
	
	public static class TwoSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public TwoSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public TwoSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 2;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
	}
	
	public static class ThreeSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public ThreeSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public ThreeSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 3;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
	}
	
	public static class FourSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public FourSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public FourSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 4;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
	}
	
	public static class FiveSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public FiveSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public FiveSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 5;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
	}
	
	public static class SixSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public SixSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public SixSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 6;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
		
	}
	
	public static class SevenSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public SevenSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public SevenSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 7;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
	}
	
	public static class EightSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public EightSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public EightSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 8;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			return true;
		}
	}
	
	public static class NineSlotTinyChestCart extends EntityTinyChestCart
			implements ISharedInventory
	{
		
		public NineSlotTinyChestCart(World world)
		{
			super(world);
		}
		
		public NineSlotTinyChestCart(World par1World, double par2, double par4, double par6)
		{
			super(par1World, par2, par4, par6);
		}
		
		@Override
		public boolean hasGui()
		{
			return true;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
		}
		
		@Override
		public GuiContainer getGui(InventoryPlayer par0)
		{
			return new GuiTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new TinyChestInventory(par0, this);
		}
		
		@Override
		public int getSizeInventory()
		{
			return 9;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 0);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean interactFirst(EntityPlayer par1EntityPlayer)
		{
			if (MinecraftForge.EVENT_BUS.post(new MinecartInteractEvent(this, par1EntityPlayer)))
			{
				return true;
			}
			if (!worldObj.isRemote)
			{
				par1EntityPlayer.openGui(TinyModularThings.instance, EnumIDs.Entities.getId(), worldObj, entityId, -1, 0);
			}
			
			return true;
		}
		
	}
	
}
