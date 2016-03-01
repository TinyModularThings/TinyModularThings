package speiger.src.api.common.utils.misc.saveHandlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.common.utils.math.IntCounter;

public class CounterSaver implements IDataSaver<IntCounter>
{

	@Override
	public void writeToNBT(List<IntCounter> par1, NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<par1.size();i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			IntCounter counter = par1.get(i);
			data.setInteger("Start", counter.getStart());
			data.setInteger("State", counter.getCount());
			list.appendTag(data);
		}
		nbt.setTag("Data", list);
	}

	@Override
	public List<IntCounter> readFromNBT(NBTTagCompound par1)
	{
		List<IntCounter> result = new ArrayList<IntCounter>();
		NBTTagList list = par1.getTagList("Data", 10);
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = list.getCompoundTagAt(i);
			result.add(new IntCounter(data.getInteger("Start"), data.getInteger("State")));
		}
		return result;
	}
	
}
