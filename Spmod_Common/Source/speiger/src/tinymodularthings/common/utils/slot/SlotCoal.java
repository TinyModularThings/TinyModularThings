package speiger.src.tinymodularthings.common.utils.slot;

import mods.railcraft.common.items.firestone.ItemFirestoneRefined;
import mods.railcraft.common.plugins.forge.ItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotCoal extends Slot
{
	
	public SlotCoal(IInventory par1iInventory, int par2, int par3, int par4)
	{
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1)
	{
		if (par1 == null)
		{
			return false;
		}
		int i = par1.itemID;
		
		if (i == Item.coal.itemID || i == Block.coalBlock.blockID)
		{
			return true;
		}
		
		try
		{
			if (par1.getItem() instanceof ItemFirestoneRefined)
			{
				return true;
			}
			else if (ItemRegistry.getItem("railcraft.cube.coke", 1) != null && i == ItemRegistry.getItem("railcraft.cube.coke", 1).itemID)
			{
				return true;
			}
			else if (ItemRegistry.getItem("railcraft.fuel.coke", 1) != null && i == ItemRegistry.getItem("railcraft.fuel.coke", 1).itemID)
			{
				return true;
			}
		}
		catch (Exception e)
		{
			
		}
		
		return false;
		
	}
	
}
