package speiger.src.spmodapi.common.blocks.utils;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.items.IEssens;
import speiger.src.api.items.IExpBottle;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.SlotAdjust;

public class MobMachineInventory extends AdvContainer
{
	MobMachine tile;
	
	public MobMachineInventory(InventoryPlayer par1, MobMachine par2)
	{
		super(par1);
		tile = par2;
		addSpmodSlotToContainer(new Slot(par2, 0, 115, 13));//Output
		addSpmodSlotToContainer(new Slot(par2, 1, 133, 13));
		addSpmodSlotToContainer(new Slot(par2, 2, 151, 13));
		addSpmodSlotToContainer(new Slot(par2, 3, 115, 31));
		addSpmodSlotToContainer(new Slot(par2, 4, 133, 31));
		addSpmodSlotToContainer(new Slot(par2, 5, 151, 31));
		addSpmodSlotToContainer(new Slot(par2, 6, 115, 49));
		addSpmodSlotToContainer(new Slot(par2, 7, 133, 49));
		addSpmodSlotToContainer(new Slot(par2, 8, 151, 49));//Output End
		addSpmodSlotToContainer(new Slot(par2, 9, 6, 53));//Food
		addSpmodSlotToContainer(new SlotAdjust(par2, 10, 54, 53, IEssens.class));//Fuel
		addSpmodSlotToContainer(new SlotAdjust(par2, 11, 30, 53, IExpBottle.class));//Exp
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack var3 = null;
		Slot var4 = (Slot) this.inventorySlots.get(par2);
		
		if (var4 != null && var4.getHasStack())
		{
			ItemStack var5 = var4.getStack();
			var3 = var5.copy();
			
			if (par2 < 9)
			{
				if (!this.mergeItemStack(var5, 12, 39, true))
				{
					return null;
				}
				
				var4.onSlotChange(var5, var3);
			}
			else if (par2 != 9 && par2 != 11 && par2 != 10)
			{
				if (tile.foodList.get(Integer.valueOf(tile.type)).get(Arrays.asList(var5.itemID, var5.getItemDamage())) != null)
				{
					if (!this.mergeItemStack(var5, 9, 10, false))
					{
						return null;
					}
				}
				else if (var5 != null && var5.getItem() instanceof IEssens && !((IEssens) var5.getItem()).isEssensEmpty(var5))
				{
					if (!this.mergeItemStack(var5, 10, 11, false))
					{
						return null;
					}
				}
				else if (var5 != null && var5.getItem() instanceof IExpBottle && ((IExpBottle) var5.getItem()).hasExp(var5))
				{
					if (!this.mergeItemStack(var5, 11, 12, false))
					{
						return null;
					}
				}
				else if (par2 >= 12 && par2 < 40)
				{
					if (!this.mergeItemStack(var5, 39, 48, false))
					{
						return null;
					}
				}
				else if (par2 >= 39 && par2 < 48 && !this.mergeItemStack(var5, 12, 39, false))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(var5, 12, 39, false))
			{
				return null;
			}
			
			if (var5.stackSize == 0)
			{
				var4.putStack((ItemStack) null);
			}
			else
			{
				var4.onSlotChanged();
			}
			
			if (var5.stackSize == var3.stackSize)
			{
				return null;
			}
			
			var4.onPickupFromSlot(par1EntityPlayer, var5);
		}
		
		return var3;
	}
	
}
