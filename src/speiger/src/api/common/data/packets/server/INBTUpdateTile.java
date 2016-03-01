package speiger.src.api.common.data.packets.server;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Speiger
 * 
 */
public interface INBTUpdateTile extends ISyncTile
{
	public void onReadFromPacket(NBTTagCompound nbt);
	
	public void onWriteToPacket(NBTTagCompound nbt);
}
