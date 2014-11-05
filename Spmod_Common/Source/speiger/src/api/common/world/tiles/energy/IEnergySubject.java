package speiger.src.api.common.world.tiles.energy;

public interface IEnergySubject
{
	/**
	 * @Return is the Storage full
	 */
	public boolean isFull();
	
	/**
	 * @Return the max Storage
	 */
	public int getMaxStorage();
	
	/**
	 * @Requested_Amount: the requested amount of energy.
	 * @Simulate: if you want only to test how much energy you gain.
	 * @Return the used Amount
	 */
	public int useEnergy(int amount, boolean simulate);
	
	/**
	 * @Return the stored Energy
	 */
	public int getStoredEnergy();
	
	/**
	 * @Amount_To_Add: the amount you want to add.
	 * @Simulate: If you want only to test adding energy.
	 * @Return: The added amount.
	 */
	public int addEnergy(int amount, boolean simulate);
	
	/**
	 * @Return the amount that would be requested
	 */
	public int getRequestedEnergy();
	
	/**
	 * @Return if you request energy.
	 */
	public boolean requestEnergy();
	
	/**
	 * @Amount: if you want to access the energy directly.
	 */
	public void setEnergy(int amount);
}
