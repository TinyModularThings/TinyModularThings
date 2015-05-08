package speiger.src.api.common.data.packets;

import net.minecraft.network.packet.Packet250CustomPayload;

public interface ISpmodPacketHandler
{	
	public void registerPacket(Class<? extends ISpmodPacket> par1);
	
	public Packet250CustomPayload createFinishPacket(ISpmodPacket par1);
}
