package speiger.src.api.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DisplayStack
{
	Item id;
	int meta;
	
	public DisplayStack(ItemStack par1)
	{
		this(par1.getItem(), par1.getItemDamage());
	}
	
	public DisplayStack(Item par1, int par2)
	{
		id = par1;
		meta = par2;
	}
	
}
