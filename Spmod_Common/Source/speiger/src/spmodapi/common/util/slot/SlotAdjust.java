package speiger.src.spmodapi.common.util.slot;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotAdjust extends Slot
{
	Class[] clz;
	
	public SlotAdjust(IInventory par1iInventory, int par2, int par3, int par4, Class... par5)
	{
		super(par1iInventory, par2, par3, par4);
		clz = par5;
	}
	
	@Override
	public boolean isItemValid(ItemStack par1)
	{
		if (par1 != null && par1.getItem() != null)
		{
			Item item = par1.getItem();
			Class cls = item.getClass();
			if(cls != null)
			{
				for(int i = 5;i>0;i--)
				{
					for (Class inter : cls.getInterfaces())
					{
						for (Class cu : clz)
						{
							if (cu.getSimpleName().equalsIgnoreCase(inter.getSimpleName()))
							{
								return true;
							}
						}
					}
					cls = cls.getSuperclass();
				}
			}
		}
		return false;
	}
	
}
