package speiger.src.api.packets;

import java.io.DataInput;

public interface IPacketReciver
{
	void recivePacket(DataInput par1);
	
	//For Registration. Only needed for None TileEntities! TileEntities can say NULL!
	public String identifier();
}
