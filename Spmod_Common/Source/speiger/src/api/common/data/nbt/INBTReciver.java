package speiger.src.api.common.data.nbt;

import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.registry.helpers.SpmodMod;

public interface INBTReciver
{
	public void loadFromNBT(NBTTagCompound par1);
	
	public void saveToNBT(NBTTagCompound par1);
	
	public void finishLoading();
	
	public SpmodMod getOwner();
	
	public String getID();
}
