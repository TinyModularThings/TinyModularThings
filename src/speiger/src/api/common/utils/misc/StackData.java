package speiger.src.api.common.utils.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class StackData
{
	ItemStack containt;
	
	public StackData(ItemStack item)
	{
		containt = item;
	}
	
	public ItemStack getContaint()
	{
		return containt;
	}
	
	@Override
	public int hashCode()
	{
		return containt.getItem().hashCode() + containt.getItemDamage();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof StackData)
		{
			StackData data = (StackData)obj;
			return data.containt.isItemEqual(containt);
		}
		return false;
	}
}
