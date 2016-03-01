package speiger.src.api.common.data.packets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.data.packets.server.ISyncTile;

public interface ISpmodPacketHandler
{
	public void updateNetworkField(TileEntity tile, String field);
	
	public void updateNetworkTile(ISyncTile par1);
	
	public void sendEventUpdate(TileEntity par1, int eventID, int argument, boolean limitRange);
	
	public void sendClientEventUpdate(TileEntity par1, int eventID, int argument);
	
	public void registerPacket(Class<? extends ISpmodPacket> packet);
	
	public void sendToPlayer(ISpmodPacket packet, EntityPlayer player);
	
	public void sendToServer(ISpmodPacket packet);
}
