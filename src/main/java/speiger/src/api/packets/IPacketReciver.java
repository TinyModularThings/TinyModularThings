package speiger.src.api.packets;

import io.netty.buffer.ByteBuf;

public interface IPacketReciver
{
	void recivePacket(ByteBuf par1);
	
	//For Registration. Only needed for None TileEntities! TileEntities can say NULL!
	public String identifier();
}
