package speiger.src.api.common.data.packets;

import java.io.DataInput;

public interface IPacketReciver
{
	void recivePacket(DataInput par1);
	
	/**
	 * The Identifier is only Required for Custom Packets. You also need to register the PacketHandler.
	 * Every differed Packet Do not require it. Please Check out where the differend get Handled so you do not crash the game.
	 */
	public String identifier();
}
