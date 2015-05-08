package speiger.src.tinymodularthings.common.network;

import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.tinymodularthings.common.network.packets.client.AluPipePacket;
import speiger.src.tinymodularthings.common.network.packets.client.BatteryPacket;
import speiger.src.tinymodularthings.common.network.packets.client.SlotClickPacket;

public class PacketRegistry
{
	public static void init()
	{
		SpmodPacketHelper.handler.registerPacket(AluPipePacket.class);
		SpmodPacketHelper.handler.registerPacket(SlotClickPacket.class);
		SpmodPacketHelper.handler.registerPacket(BatteryPacket.class);
	}
}
