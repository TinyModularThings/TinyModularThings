package speiger.src.api.common.items;

import net.minecraft.item.ItemStack;

public interface IEssens
{
	public int getEssensStorage(ItemStack par1);
	
	public int getStoredEssens(ItemStack par1);
	
	public int getEssensPerCharge(ItemStack par1);
	
	public int chargeEssens(ItemStack par1, int amount);
	
	public int dischargeEssens(ItemStack par1, int amount);
	
	public boolean hasEssens(ItemStack par1);
	
	public boolean needsEssens(ItemStack par1);
	
	public boolean isAdvancedEssens(ItemStack par1);
}
