package speiger.src.spmodapi.common.handler;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.ServerChatEvent;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.util.proxy.LangProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ChatHandler
{
	private static ChatHandler chat = new ChatHandler();
	
	public static ChatHandler getInstance()
	{
		return chat;
	}
	
	@ForgeSubscribe
	public void onChatMessage(ServerChatEvent evt)
	{
		if (evt.username.equalsIgnoreCase("Server"))
		{
			return;
		}
		
		if (evt.message.equalsIgnoreCase("SpmodMod"))
		{
			evt.setCanceled(true);
			evt.player.openGui(SpmodAPI.instance, EnumGuiIDs.Commands.getID(), evt.player.worldObj, 0, 0, 0);
		}
	}
}
