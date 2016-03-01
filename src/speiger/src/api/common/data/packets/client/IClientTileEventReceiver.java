package speiger.src.api.common.data.packets.client;

import net.minecraft.entity.player.EntityPlayer;

public interface IClientTileEventReceiver
{
	public void onClientEvent(int eventID, EntityPlayer player, int argument);
}
