package speiger.src.api.inventory;

import net.minecraft.item.ItemStack;

public interface IFilteredInventory
{
	public ItemStack getFilteredItem(int slotID);

	public void setFilteredItem(int slotID, ItemStack par1);
}
