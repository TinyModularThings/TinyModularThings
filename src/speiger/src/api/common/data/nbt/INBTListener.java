package speiger.src.api.common.data.nbt;

import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.core.SpmodMod;

public interface INBTListener
{
	public void readFromNBT(NBTTagCompound nbt);
	
	public void writeToNBT(NBTTagCompound nbt);
	
	public SpmodMod getOwner();
	
	public String getID();
	
	public void setLoadingState(LoadingState par1);
	
	public static enum LoadingState
	{
		Loading,
		Loaded,
		FailedLoading,
		Saving,
		RequestedSaving,
		Saved,
		FailedSaving,
		Done;
	}
}
