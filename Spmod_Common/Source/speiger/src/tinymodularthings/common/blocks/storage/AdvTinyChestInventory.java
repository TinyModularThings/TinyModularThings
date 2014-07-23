package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AdvTinyChestInventory extends AdvContainer
{
	
	ISharedInventory tile;
	
	int[][] SlotX = new int[][] { {}, { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
	
	public AdvTinyChestInventory(InventoryPlayer par1, ISharedInventory par2)
	{
		tile = par2;
		int size = par2.getIInventory().getSizeInventory();
		
		if (par2.isEntity())
		{
			size -= 2;
		}
		
		for (int i = 0; i < size; i++)
		{
			addSpmodSlotToContainer(new Slot(par2.getIInventory(), i, SlotX[size][i], 30));
		}
		
		if (par2.isEntity())
		{
			addSpmodSlotToContainer(new Slot(par2.getIInventory(), size, 134, 57));
			addSlotToContainer(new Slot(par2.getIInventory(), size + 1, 152, 57));
		}
		
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
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
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		if (!tile.isEntity())
		{
			int size = tile.getIInventory().getSizeInventory();
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
		else
		{
			int size = tile.getIInventory().getSizeInventory();
			size -= 2;
			int secSize = tile.getIInventory().getSizeInventory();
			ItemStack itemstack = null;
			Slot slot = (Slot) inventorySlots.get(par2);
			
			if (slot != null && slot.getHasStack())
			{
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				
				if (par2 == size + 1)
				{
					return null;
				}
				else if (par2 < size + 1)
				{
					if (!mergeItemStack(itemstack1, secSize, 36 + secSize, true))
					{
						return null;
					}
				}
				else if (!mergeItemStack(itemstack1, 0, secSize - 1, false))
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
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if (tile.isEntity())
		{
			if (par1 == tile.getIInventory().getSizeInventory() - 1)
			{
				return null;
			}
			return super.slotClick(par1, par2, par3, par4EntityPlayer);
		}
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		if (!tile.isEntity() && tile != null && tile instanceof AdvTile)
		{
			AdvTile adv = (AdvTile) tile;
			for (int i = 0; i < crafters.size(); ++i)
			{
				ICrafting icrafting = (ICrafting) crafters.get(i);
				adv.onSendingGuiInfo(this, icrafting);
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (!tile.isEntity() && tile != null && tile instanceof AdvTile)
		{
			AdvTile adv = (AdvTile) tile;
			adv.receiveClientEvent(par1, par2);
		}
	}
	
}
