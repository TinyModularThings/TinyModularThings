package speiger.src.api.nbt;

import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.util.SpmodMod;

public interface INBTReciver
{
	public void loadFromNBT(NBTTagCompound par1);
	
	public void saveToNBT(NBTTagCompound par1);
	
	public void finishLoading();
	
	public SpmodMod getOwner();
	
	public String getID();
}
