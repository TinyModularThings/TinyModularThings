package speiger.src.spmodapi.common.handler;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.packets.SpmodPacketHelper;
import speiger.src.api.packets.SpmodPacketHelper.PacketType;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class SpmodPacket implements IMessage
{
	private byte[] bytes;

	public SpmodPacket()
	{
	}

	public SpmodPacket(byte[] byteArray)
	{
		bytes = byteArray;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		SpmodMod mod = SpmodModRegistry.getModFromName(ByteBufUtils.readUTF8String(buf));
		PacketType type = PacketType.values()[buf.readByte()];
		if (type == PacketType.TileEntity)
		{
			World world = DimensionManager.getWorld(buf.readInt());
			int x = buf.readInt();
			int y = buf.readInt();
			int z = buf.readInt();

			TileEntity tile = world.getTileEntity(x, y, z);
			if (tile != null && tile instanceof IPacketReciver)
			{
				((IPacketReciver) tile).recivePacket(buf);
			}
		} else if (type == PacketType.Custom)
		{
			IPacketReciver recive = SpmodPacketHelper.getHelper().getReciverFromName(ByteBufUtils.readUTF8String(buf));
			if (recive != null)
			{
				recive.recivePacket(buf);
			}
		}

	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeBytes(bytes);
	}

	public static class SpmodPacketHandler implements IMessageHandler<SpmodPacket, IMessage>
	{

		@Override
		public IMessage onMessage(SpmodPacket packet, MessageContext ctx)
		{
			return null;
		}
	}
}
