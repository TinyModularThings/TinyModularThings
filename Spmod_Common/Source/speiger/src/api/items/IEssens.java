package speiger.src.api.items;

import net.minecraft.item.ItemStack;

public interface IEssens
{
	/**
	 * returns the EssensValue per Charge
	 */
	public int getEssensValue(ItemStack par1);
	
	/**
	 * return the essens filled amount
	 */
	public int fillEssens(ItemStack par1, int amount);
	
	/**
	 * return the drained amount
	 */
	public int drainEssens(ItemStack par1, int amount);

	/**
	 * return the total Amount of Charges
	 */
	public int getMaxCharge(ItemStack par1);
	
	/**
	 * return the Charges left
	 */
	public int getCurrentCharges(ItemStack par1);
	
	/**
	 * return true if the essens bottle is Empty. Else false
	 */
	public boolean isEssensEmpty(ItemStack par1);
	
	/**
	 * return if the Essens Bottle isFull.
	 * For better detection it is usefull!
	 */
	public boolean isEssensBottleFull(ItemStack par1);
}
