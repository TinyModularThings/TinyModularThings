package speiger.src.spmodapi.common.util.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ItemSlot extends SpmodSlot
{
	ItemStack item;
	public ItemSlot(IInventory tile, int id, int x, int y, ItemStack item)
	{
		super(tile, id, x, y);
		this.blockSlot();
		this.item = item;
	}
	
	@Override
	public ItemStack getStack()
	{
		return item;
	}
	
}
