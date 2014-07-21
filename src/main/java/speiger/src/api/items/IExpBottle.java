package speiger.src.api.items;

import net.minecraft.item.ItemStack;

public interface IExpBottle
{
	/**
	 * returns the MaxExp that the bottle can store
	 */
	public int getMaxExp(ItemStack par1);
	
	/**
	 * return stored Exp
	 */
	public int getStoredExp(ItemStack par1);
	
	/**
	 * charge function. Returns the added amount.
	 */
	public int charge(ItemStack par1, int amount);
	
	/**
	 * discharge function. Returns the removed amount
	 */
	public int discharge(ItemStack par1, int amount);
	
	/**
	 * transferlimit function. return the transferlimit of the expBottle
	 */
	public int getTransferlimit(ItemStack par1);
	
	/**
	 * return if it has exp.
	 */
	public boolean hasExp(ItemStack par1);
	
	/**
	 * return if it needs exp.
	 */
	public boolean needExp(ItemStack par1);
}
