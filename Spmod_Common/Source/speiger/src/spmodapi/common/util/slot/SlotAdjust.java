package speiger.src.spmodapi.common.util.slot;

import java.lang.reflect.Type;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotAdjust extends Slot
{
	Class[] clz;
	public SlotAdjust(IInventory par1iInventory, int par2, int par3, int par4, Class...par5)
	{
		super(par1iInventory, par2, par3, par4);
		clz = par5;
	}
	@Override
	public boolean isItemValid(ItemStack par1)
	{
		if(par1 != null && par1.getItem() != null)
		{
			Item item = par1.getItem();
			
			for(Class cu : clz)
			{
				for(Class inter : item.getClass().getInterfaces())
				{
					if(cu.getSimpleName().equalsIgnoreCase(inter.getSimpleName()))
					{
						return true;
					}
				}
				
			}
			
		}
		return false;
	}
	
	
	
}
