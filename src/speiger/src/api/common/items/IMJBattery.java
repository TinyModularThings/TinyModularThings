package speiger.src.api.common.items;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IMJBattery
{
	//return what got added
	public int charge(ItemStack battery, int amount);

	//return what got removed
	public int discharge(ItemStack battery, int amount);
	
	public int getTransferlimit(ItemStack battery);
	
	public boolean hasEnergy(ItemStack battery);
	
	public boolean needsEnergy(ItemStack battery);
	
	public int getMaxEnergy(ItemStack battery);
	
	public int getStoredEnergy(ItemStack battery);
	
	public BatteryType getType(ItemStack battery);
	
	public void onEnergyNetTick(ItemStack battery, int slot, IInventory inv);
	
	public enum BatteryType
	{
		Machine,
		Generator,
		Battery;
	}
}
