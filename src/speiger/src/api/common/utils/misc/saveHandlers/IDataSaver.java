package speiger.src.api.common.utils.misc.saveHandlers;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public interface IDataSaver<T>
{
	public void writeToNBT(List<T> par1, NBTTagCompound nbt);
	
	public List<T> readFromNBT(NBTTagCompound par1);
}
