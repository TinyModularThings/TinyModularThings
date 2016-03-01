package speiger.src.api.common.items;

import net.minecraft.item.ItemStack;

public interface IExpBottle
{
	public int charge(ItemStack par1, int amount);
	
	public int discharge(ItemStack par1, int amount);
	
	public int getTransferlimit(ItemStack par1);
	
	public int getMaxCharge(ItemStack par1);
	
	public int getStoredExp(ItemStack par1);
	
	public boolean hasExp(ItemStack par1);
	
	public boolean needsExp(ItemStack par1);
}
