package speiger.src.spmodapi.common.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.api.common.data.packets.ISpmodPacketHandler;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.items.trades.TradePacket;
import speiger.src.spmodapi.common.network.packets.base.BlankUpdatePacket;
import speiger.src.spmodapi.common.network.packets.base.TileNBTPacket;
import speiger.src.spmodapi.common.network.packets.client.AccesserPacket;
import speiger.src.spmodapi.common.network.packets.client.CommandPacket;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class SpmodPacketHandler implements IPacketHandler, ISpmodPacketHandler
{
	private static boolean packets = false;
	private WeakHashMap<Integer, Class<? extends ISpmodPacket>> idsToPacket = new WeakHashMap<Integer, Class<? extends ISpmodPacket>>();
	private WeakHashMap<Class<? extends ISpmodPacket>, Integer> packetToIds = new WeakHashMap<Class<? extends ISpmodPacket>, Integer>();
	private static EntityCounter counter = new EntityCounter(0);
	
	
	public SpmodPacketHandler()
	{
		registerPacket(BlankUpdatePacket.class);
		registerPacket(AccesserPacket.class);
		registerPacket(CommandPacket.class);
		registerPacket(TileNBTPacket.class);
		registerPacket(TradePacket.class);
	}
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player par1)
	{
		if (packet.channel.equalsIgnoreCase("Spmod"))
		{
			if (!packets)
			{
				packets = true;
				return;
			}
			packets = false;
			handlePacket(packet, (EntityPlayer)par1);
		}
	}
	
	public void handlePacket(Packet250CustomPayload par1, EntityPlayer par2)
	{
		try
		{
			DataInputStream stream = new DataInputStream(new ByteArrayInputStream(par1.data));
			byte packetID = stream.readByte();
			
			ISpmodPacket packet = getSpmodPacket(packetID);
			if(packet != null)
			{
				packet.readData(stream);
				packet.handlePacket(par2, FMLCommonHandler.instance().getEffectiveSide());
			}
			stream.close();
		}
		catch(Exception e)
		{
			SpmodAPI.log.print("Received a none Valid Packet... ");
			e.printStackTrace();
		}
	}
	
	private ISpmodPacket getSpmodPacket(int id)
	{
		try
		{
			Class<? extends ISpmodPacket> clz = idsToPacket.get(id);
			if(clz == null)
			{
				return null;
			}
			return clz.newInstance();
		}
		catch(Exception e)
		{
			SpmodAPI.log.print("Packet Requires a Constructure with no variables");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void registerPacket(Class<? extends ISpmodPacket> par1)
	{
		int id = counter.getAndUpdateID();
		idsToPacket.put(id, par1);
		packetToIds.put(par1, id);
	}

	@Override
	public Packet250CustomPayload createFinishPacket(ISpmodPacket par1)
	{
		if(par1 == null)
		{
			return null;
		}
		int id = packetToIds.get(par1.getClass());
		Packet250CustomPayload packet = null;
		try
		{
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			DataOutputStream stream = new DataOutputStream(buffer);
			stream.writeByte(id);
			par1.writeData(stream);
			stream.close();
			packet = new Packet250CustomPayload("Spmod", buffer.toByteArray());
		}
		catch(Exception e)
		{
			SpmodAPI.log.print("Having problems building the PacketData maybe a Packet Problem or the Data amount is to big");
			e.printStackTrace();
		}
		return packet;
	}
}
