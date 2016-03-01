package speiger.src.api.common.utils.misc.saveHandlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackHandler implements IDataSaver<FluidStack>
{

	@Override
	public void writeToNBT(List<FluidStack> par1, NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<par1.size();i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			par1.get(i).writeToNBT(data);
			list.appendTag(data);
		}
		nbt.setTag("Data", list);
	}

	@Override
	public List<FluidStack> readFromNBT(NBTTagCompound par1)
	{
		List<FluidStack> list = new ArrayList<FluidStack>();
		NBTTagList dataList = par1.getTagList("Data", 10);
		for(int i = 0;i<dataList.tagCount();i++)
		{
			list.add(FluidStack.loadFluidStackFromNBT(dataList.getCompoundTagAt(i)));
		}
		return list;
	}
	
}
