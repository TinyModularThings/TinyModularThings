package speiger.src.api.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ICraftingInit
{
	public void afterCrafting(ItemStack par1, NBTTagCompound nbt);
	
	public void afterUpgradeCrafting(ItemStack item, NBTTagCompound nbt);
	
	public boolean canUpgradeOnCrafting(ItemStack item, NBTTagCompound nbt);
}
