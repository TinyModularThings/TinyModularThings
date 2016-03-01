package speiger.src.api.common.utils.misc.saveHandlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemStackHandler implements IDataSaver<ItemStack>
{
	
	@Override
	public void writeToNBT(List<ItemStack> par1, NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0;i < par1.size();i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			par1.get(i).writeToNBT(data);
			list.appendTag(data);
		}
		nbt.setTag("Data", list);
	}
	
	@Override
	public List<ItemStack> readFromNBT(NBTTagCompound par1)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		NBTTagList data = par1.getTagList("Data", 10);
		for(int i = 0;i < data.tagCount();i++)
		{
			list.add(ItemStack.loadItemStackFromNBT(data.getCompoundTagAt(i)));
		}
		return list;
	}
	
}
