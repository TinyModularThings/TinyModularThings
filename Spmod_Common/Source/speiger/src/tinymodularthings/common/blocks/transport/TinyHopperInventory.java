package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.hopper.IHopperInventory;
import speiger.src.api.inventory.TankSlot;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class TinyHopperInventory extends AdvContainer
{
	IHopperInventory tile;
	public TinyHopperInventory(InventoryPlayer par1, IHopperInventory par2)
	{
		tile = par2;
		par2.playerJoins(par1.player);
		switch (par2.getHopperType())
		{
			case Items:
				Slot[] slots = par2.getSlots();
				for (Slot cu : slots)
				{
					addSpmodSlotToContainer(cu);
				}
				
				break;
			case Energy:
				Slot[] slots1 = par2.getSlots();
				for (Slot cu : slots1)
				{
					addSpmodSlotToContainer(cu);
				}
				break;
			case Fluids:
				TankSlot[] slot = par2.getTanks();
				for (int i = 0; i < slot.length; i++)
				{
					if (slot[i] != null)
					{
						addTankSlot(slot[i]);
					}
				}
				break;
			case Nothing:
				break;
		}
		
		int var3;
		for (var3 = 0; var3 < 3; var3++)
		{
			for (int var4 = 0; var4 < 9; var4++)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; var3++)
		{
			addSlotToContainer(new Slot(par1, var3, 8 + var3 * 18, 142));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		tile.playerLeaves(par1EntityPlayer);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		int size = tile.getSlots().length;
		
		ItemStack itemstack = null;
		Slot slot = (Slot) inventorySlots.get(par2);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if (par2 < size)
			{
				if (!mergeItemStack(itemstack1, size, 36 + size, true))
				{
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, 0, size, false))
			{
				return null;
			}
			
			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}
		
		return itemstack;
	}
}