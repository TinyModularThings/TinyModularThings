package speiger.src.tinymodularthings.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import speiger.src.tinymodularthings.common.items.core.TinyItem;

public class ItemTinyItem extends TinyItem
{
	
	String Name;
	
	public ItemTinyItem(int par1, String name)
	{
		super(par1);
		Name = name;
		setCreativeTab(CreativeTabs.tabFood);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return Name;
	}
	
}
