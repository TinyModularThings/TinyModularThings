package speiger.src.spmodapi.common.lib.bc;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.inventory.ISpecialInventory;

public class TransactorSpecial extends Transactor
{
	
	protected ISpecialInventory inventory;
	
	public TransactorSpecial(ISpecialInventory inventory)
	{
		this.inventory = inventory;
	}
	
	@Override
	public int inject(ItemStack stack, ForgeDirection orientation, boolean doAdd)
	{
		return inventory.addItem(stack, doAdd, orientation);
	}
	
	@Override
	public ItemStack remove(IStackFilter filter, int size, ForgeDirection orientation, boolean doRemove)
	{
		ItemStack[] extracted = inventory.extractItem(false, orientation, size);
		if (extracted != null && extracted.length > 0 && filter.matches(extracted[0]))
		{
			if (doRemove)
			{
				inventory.extractItem(true, orientation, size);
			}
			return extracted[0];
		}
		return null;
	}
}
