package speiger.src.api.common.world.items.energy;

import net.minecraft.item.ItemStack;

public interface IBCBattery
{
	public int getStoredMJ(ItemStack par1);
	
	public int getMaxMJStorage(ItemStack par1);
	
	public BatteryType getType(ItemStack par1);
	
	public boolean requestEnergy(ItemStack par1);
	
	public int getRequestedAmount(ItemStack par1);
	
	public boolean wantToSendEnergy(ItemStack par1);
	
	public int energyToSend(ItemStack par1);
	
	public boolean isEmpty(ItemStack par1);
	
	public boolean isFull(ItemStack par1);
	
	public int charge(ItemStack par1, int amount, boolean simulate);
	
	public int discharge(ItemStack par1, int amount, boolean simulate);
	
	public int getTransferlimit(ItemStack par1);
	
	public static enum BatteryType
	{
		Machine(false, true),
		Generator(true, false),
		Storage(true, true);
		
		boolean source;
		boolean acceptor;
		
		private BatteryType(boolean par1, boolean par2)
		{
			source = par1;
			acceptor = par2;
		}
	}
}
