package speiger.src.spmodapi.common.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class SpmodPacketHandler implements IPacketHandler
{
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		if (packet.channel.equalsIgnoreCase("Spmod"))
		{
			if (side == Side.CLIENT)
			{
				handleClient(packet);
			}
			else if (side == Side.SERVER)
			{
				handleServer(packet);
			}
		}
	}
	
	public void handleServer(Packet250CustomPayload packet)
	{
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try
		{
			World world = DimensionManager.getWorld(stream.readInt());
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			SpmodModRegistry.getModFromName(stream.readUTF());
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof IPacketReciver)
			{
				((IPacketReciver) tile).recivePacket(stream);
			}
		}
		catch (Exception e)
		{
			SpmodAPI.log.print("Got a Invalid Server Packet! Please look at your client!: ");
		}
	}
	
	public void handleClient(Packet250CustomPayload packet)
	{
		DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try
		{
			World world = DimensionManager.getWorld(stream.readInt());
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof IPacketReciver)
			{
				((IPacketReciver) tile).recivePacket(stream);
			}
		}
		catch (Exception e)
		{
			SpmodAPI.log.print("Got a Invalid Client Packet! Please look at your People. Maybe they tried to Hack!");
		}
	}
	
}
