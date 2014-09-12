package speiger.src.api.energy;

import net.minecraft.item.ItemStack;

public interface IBCBattery
{
	/**
	 * @Return Stored MJ
	 */
	public int getStoredMJ(ItemStack par1);
	
	/**
	 * @Return Max Space
	 */
	public int getMaxStorage(ItemStack par1);
	
	/**
	 * @Return charged amount.
	 */
	public int charge(ItemStack par1, int par2, boolean ignoreTransferlimit, boolean simulate);
	
	/**
	 * @Return discharged amount.
	 */
	public int discharge(ItemStack par1, int par2, boolean ignoreTransferlimit, boolean simulate);
	
	/**
	 * @Return Transferlimit
	 */
	public int getTransferlimit(ItemStack par1);
	
	/**
	 * @Return BatteryType
	 */
	public BatteryType getType(ItemStack par1);
	
	/**
	 * @Return If the Battery Request energy.
	 */
	public boolean requestEnergy(ItemStack par1);
	
	/**
	 * @Return if its empty
	 */
	public boolean isEmpty(ItemStack par1);
	
	/**
	 * @Return if its full
	 */
	public boolean isFull(ItemStack par1);
	
	/**
	 * @Return Stored MJ in % (From 0 to 100)
	 */
	public int getStoredMJInPercent(ItemStack par1);
	
	public static enum BatteryType
	{
		Machine(true, false), Battery(true, true), Generator(false, true);
		
		boolean provide;
		boolean recive;
		
		private BatteryType(boolean par1, boolean par2)
		{
			provide = par1;
			recive = par2;
		}
		
		public boolean canRecivePower()
		{
			return recive;
		}
		
		public boolean canProvidePower()
		{
			return provide;
		}
	}
}
