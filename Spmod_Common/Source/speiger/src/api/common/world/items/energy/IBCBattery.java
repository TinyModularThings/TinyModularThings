package speiger.src.api.common.world.items.energy;

import net.minecraft.item.ItemStack;

public interface IBCBattery
{
	/**
	 * @return the Stored Energy
	 */
	public int getStoredMJ(ItemStack par1);
	
	/**
	 * @return the Max Capacity of the Battery
	 */
	public int getMaxMJStorage(ItemStack par1);
	
	/**
	 * @return the Type of the Battery
	 */
	public BatteryType getType(ItemStack par1);
	
	/**
	 * @return if the Battery Request Energy.
	 */
	public boolean requestEnergy(ItemStack par1);
	
	/**
	 * @return how much energy the 
	 */
	public int getRequestedAmount(ItemStack par1);
	
	/**
	 * @return if the battery has energy to send.
	 * Used in the ItemEnergyNet.
	 * That allows you to control that. 
	 * If that returns false the energyToSend == 0
	 */
	public boolean wantToSendEnergy(ItemStack par1);
	
	/**
	 * @return the energy that the battery wants to send.
	 */
	public int energyToSend(ItemStack par1);
	
	/** 
	 * @return if the battery is empty. Only Helper function
	 */
	public boolean isEmpty(ItemStack par1);
	
	/**
	 * @return if the battery is full. Only Helper function.
	 */
	public boolean isFull(ItemStack par1);
	
	/**
	 * Actual Charge function.
	 * @Amount you want to charge
	 * @simulate if you want to only test how much you can charge
	 * @return the amount that got used.
	 */
	public int charge(ItemStack par1, int amount, boolean simulate);
	
	/**
	 * Actual Discharge function,
	 * @amount that you want to draw.
	 * @simulate that you can test how much
	 * @return the amount you will get
	 */
	public int discharge(ItemStack par1, int amount, boolean simulate);
	
	/**
	 * This Function is called from Sender to Receiver. 
	 * If a portable machine wants to send its energy by its own / Draw Power by its own.
	 * If you respect the batteries information, then please use the following functions:
	 * @see wantToSendEnergy & @see energyToSend for drawing Power and @see requestEnergy &
	 * @see getRequestedAmount for sending Power.
	 * These will have at least in my mod a differend Transferlimits.
	 * @return the transferlimit. Effected by everything. What every you want.
	 */
	public int getTransferlimit(ItemStack par1);
	
	/**
	 * DO NOT USE THAT FUNCTION!!
	 * This function is only for the internal usage. It is for the modders itself. That uses also my code as base.
	 * @return the Max Transferlimit. That will go never higher. so the Static variable. 
	 */
	public int getMaxTransferlimt(ItemStack par1);
	
	/**
	 * This function is for the EnergyNet. So that you can see the difference between every battery.
	 * Simply use a basic name + the System.nano(); to generate the BatteryID.
	 * @return the Battery ID. 
	 */
	public String getBatteryID(ItemStack par1);
	
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
