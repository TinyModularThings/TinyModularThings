package speiger.src.spmodapi.common.util.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.utils.InventoryUtil;
import speiger.src.spmodapi.common.util.TextureEngine.StackInfo;

public class SlotItemAdjustSlot extends Slot
{
	StackInfo[] data;
	public SlotItemAdjustSlot(IInventory par1iInventory, int par2, int par3, int par4, StackInfo...data)
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
		
		for(StackInfo cu : data)
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
