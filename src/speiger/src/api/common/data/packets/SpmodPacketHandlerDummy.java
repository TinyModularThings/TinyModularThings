package speiger.src.api.common.data.packets;

import speiger.src.api.common.data.packets.server.INetworkTile;
import speiger.src.api.common.data.packets.server.ISyncTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class SpmodPacketHandlerDummy implements ISpmodPacketHandler
{
	
	@Override
	public void updateNetworkField(TileEntity tile, String field)
	{
		
	}
	
	@Override
	public void updateNetworkTile(ISyncTile par1)
	{
		
	}
	
	@Override
	public void sendEventUpdate(TileEntity par1, int eventID, int argument, boolean limitRange)
	{
		
	}
	
	@Override
	public void sendClientEventUpdate(TileEntity par1, int eventID, int argument)
	{
		
	}

	@Override
	public void registerPacket(Class<? extends ISpmodPacket> packet)
	{
		
	}

	@Override
	public void sendToPlayer(ISpmodPacket packet, EntityPlayer player)
	{
		
	}

	@Override
	public void sendToServer(ISpmodPacket packet)
	{
		
	}


	
}
