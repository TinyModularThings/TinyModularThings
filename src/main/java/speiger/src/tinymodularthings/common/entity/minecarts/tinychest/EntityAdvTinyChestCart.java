package speiger.src.tinymodularthings.common.entity.minecarts.tinychest;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartInteractEvent;
import speiger.src.api.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.storage.GuiAdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChestInventory;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.utils.TicketUtils;

public abstract class EntityAdvTinyChestCart extends EntityTinyChestCart
		implements IRoutableCart, ISharedInventory
{
	
	public EntityAdvTinyChestCart(World par1World)
	{
		super(par1World);
	}
	
	public EntityAdvTinyChestCart(World par1World, double par2, double par4, double par6)
	{
		super(par1World, par2, par4, par6);
	}
	
	@Override
	public void killMinecart(DamageSource par1DamageSource)
	{
		if (!worldObj.isRemote)
		{
			setInventorySlotContents(getInventorySize() + 1, (ItemStack) null);
		}
		super.killMinecart(par1DamageSource);
	}
	
	@Override
	public ItemStack getCartItem()
	{
		return new ItemStack(TinyItems.advTinyStorageCart, 1, getInventorySize() - 1);
	}
	
	@Override
	public int getSizeInventory()
	{
		return 2 + getInventorySize();
	}
	
	public abstract int getInventorySize();
	
	@Override
	public ItemStack removeOneItem(IInventory inv, IStackFilter filter)
	{
		for (int i = 0; i < inv.getSizeInventory() - 2; i++)
		{
			ItemStack slot = inv.getStackInSlot(i);
			if ((slot != null) && (filter.matches(slot)))
			{
				return inv.decrStackSize(i, 1);
			}
		}
		return null;
	}
	
	@Override
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
			for (int ii = 0; ii < dest.getSizeInventory() - 2; ii++)
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
				for (int ii = 0; ii < dest.getSizeInventory() - 2; ii++)
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
	
	@Override
	public String getDestination()
	{
		ItemStack stack = getStackInSlot(getSizeInventory() - 1);
		return TicketUtils.getDestination(stack);
	}
	
	@Override
	public boolean setDestination(ItemStack ticket)
	{
		if (ticket != null && TicketUtils.isTicket(ticket))
		{
			String dest = TicketUtils.getDestination(ticket);
			if (!dest.equals(getDestination()))
			{
				setInventorySlotContents(getSizeInventory() - 1, TicketUtils.copyTicket(ticket));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!worldObj.isRemote && worldObj.getWorldTime() % 10 == 0)
		{
			if (getStackInSlot(getInventorySize()) != null)
			{
				ItemStack stack = getStackInSlot(getInventorySize());
				if (TicketUtils.isTicket(stack))
				{
					setDestination(stack);
				}
			}
		}
	}
	
	public static class OneSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public OneSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public OneSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 1;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class TwoSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public TwoSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public TwoSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 2;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class ThreeSlotAdvTinyChestCart extends
			EntityAdvTinyChestCart
	{
		public ThreeSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public ThreeSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 3;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class FourSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public FourSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public FourSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 4;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class FiveSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public FiveSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public FiveSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 5;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class SixSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public SixSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public SixSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 6;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class SevenSlotAdvTinyChestCart extends
			EntityAdvTinyChestCart
	{
		public SevenSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public SevenSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 7;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class EightSlotAdvTinyChestCart extends
			EntityAdvTinyChestCart
	{
		public EightSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public EightSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 8;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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
	
	public static class NineSlotAdvTinyChestCart extends EntityAdvTinyChestCart
	{
		public NineSlotAdvTinyChestCart(World par1World)
		{
			super(par1World);
		}
		
		public NineSlotAdvTinyChestCart(World par1World, double par2, double par4, double par6)
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
			return new GuiAdvTinyChest(par0, this);
		}
		
		@Override
		public Container getInventory(InventoryPlayer par0)
		{
			return new AdvTinyChestInventory(par0, this);
		}
		
		@Override
		public int getInventorySize()
		{
			return 9;
		}
		
		@Override
		public BlockStack getRenderedBlock()
		{
			return new BlockStack(TinyBlocks.storageBlock, 2);
		}
		
		@Override
		public IInventory getIInventory()
		{
			return this;
		}
		
		@Override
		public boolean isEntity()
		{
			return true;
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