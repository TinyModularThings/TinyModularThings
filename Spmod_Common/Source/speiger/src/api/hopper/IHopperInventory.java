package speiger.src.api.hopper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.inventory.TankSlot;
import speiger.src.tinymodularthings.common.utils.HopperType;

public abstract interface IHopperInventory
{
	public abstract Slot[] getSlots();
	
	public abstract void playerJoins(EntityPlayer player);
	
	public abstract void playerLeaves(EntityPlayer player);
	
	public abstract HopperType getHopperType();
	
	public abstract TankSlot[] getTanks();
	
	public abstract IInventory getFilter();
	
	public abstract ItemStack getFilter(int id);
	
}
