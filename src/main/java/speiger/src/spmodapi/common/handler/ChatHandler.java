package speiger.src.spmodapi.common.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.ServerChatEvent;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;

public class ChatHandler
{
	private static ChatHandler chat = new ChatHandler();
	
	public static ChatHandler getInstance()
	{
		return chat;
	}
	
	@SubscribeEvent
	public void onChatMessage(ServerChatEvent evt)
	{
		if(evt.username.equalsIgnoreCase("Server"))
		{
			return;
		}
		
		if(evt.message.equalsIgnoreCase("SpmodMod"))
		{
			evt.setCanceled(true);
			evt.player.openGui(SpmodAPI.instance, EnumGuiIDs.Commands.getID(), evt.player.worldObj, 0, 0, 0);
		}
	}
}
