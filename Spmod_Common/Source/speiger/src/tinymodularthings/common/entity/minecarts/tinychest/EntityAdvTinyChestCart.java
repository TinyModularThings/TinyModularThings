package speiger.src.tinymodularthings.common.entity.minecarts.tinychest;

import mods.railcraft.api.carts.IRoutableCart;
import mods.railcraft.api.core.items.IStackFilter;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.utils.TicketUtils;

public class EntityAdvTinyChestCart extends EntityTinyChestCart
		implements IRoutableCart
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
	public void setupSize(int par1)
	{
		super.setupSize(par1 + 2);
	}
	
	public int getInventorySize()
	{
		return getSizeInventory() - 2;
	}
	
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
	
	@Override
	public BlockStack getRenderedBlock()
	{
		return new BlockStack(TinyBlocks.storageBlock, 2);
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}

	@Override
	public String getInvName()
	{
		return "Advanced Tiny Chest Cart";
	}

	@Override
	public AdvContainer getInventory(InventoryPlayer par0)
	{
		AdvContainer cont = new AdvContainer(par0);
		for(int i = 0;i<getInventorySize();i++)
		{
			cont.addSpmodSlotToContainer(new SpmodSlot(this, i, SlotX[getInventorySize()][i], 30));
		}
		cont.addSpmodSlotToContainer(new SpmodSlot(this, getInventorySize(), 134, 57));
		cont.addSlotToContainer(new SpmodSlot(this, getInventorySize() + 1, 152, 57).blockSlot());

		cont.setInventory(par0);
		return cont;
	}
	
	
	
}