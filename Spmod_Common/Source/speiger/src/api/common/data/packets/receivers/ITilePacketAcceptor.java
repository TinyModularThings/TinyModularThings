package speiger.src.api.common.data.packets.receivers;

import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;

public interface ITilePacketAcceptor
{
	public void onSpmodPacket(SpmodPacket par1);
}
