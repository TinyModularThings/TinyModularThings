package speiger.src.spmodapi.common.util.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.api.common.utils.InventoryUtil;

public class SlotItemAdjustSlot extends Slot
{
	IStackInfo[] data;
	public SlotItemAdjustSlot(IInventory par1iInventory, int par2, int par3, int par4, IStackInfo...data)
	{
		super(par1iInventory, par2, par3, par4);
		this.data = data;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1)
	{
		if(par1 == null || data == null || data.length == 0)
		{
			return false;
		}
		
		for(IStackInfo cu : data)
		{
			if(cu.getResult() != null)
			{
				if(InventoryUtil.isItemEqual(cu.getResult(), par1))
				{
					return true;
				}
			}
		}
		return false;
	}
	
}
