package speiger.src.api.items;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class ItemList extends ArrayList<ItemStack>
{
	ArrayList<Integer> marked = new ArrayList<Integer>();

	int last = -1;
	
	@Override
	public ItemStack get(int arg0)
	{
		last = arg0;
		return super.get(arg0);
	}
	
	public void markForRemove()
	{
		if(last != -1 && !marked.contains(marked))
		{
			marked.add(last);
		}
	}
	
	public void remove()
	{
		for(int i = marked.size()-1;i>-1;i--)
		{
			Integer ints = marked.get(i);
			this.remove(ints.intValue());
		}
	}
	
	
	
}
