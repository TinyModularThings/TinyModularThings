package speiger.src.spmodapi.common.items.core;

import net.minecraft.item.ItemStack;

public class BasicItem extends SpmodItem
{
	String name;
	
	public BasicItem(int id, String name)
	{
		super(id);
		this.name = name;
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		return name;
	}
	
}
