package speiger.src.api.common.data.packets.server;

import net.minecraft.entity.player.EntityPlayer;

public interface ITileEventReceiver
{
	public void onEvent(int eventID, EntityPlayer player, int argument);
}
