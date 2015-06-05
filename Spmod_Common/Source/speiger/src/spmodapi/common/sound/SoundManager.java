package speiger.src.spmodapi.common.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.network.packets.common.LoginSoundPacket;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class SoundManager
{
	HashMap<String, List<String>> playerToSounds = new HashMap<String, List<String>>();
	
	//Server Only
	
	public void onPlayerLogin(EntityPlayer par1)
	{
		Packet packet = SpmodAPI.handler.createFinishPacket(new LoginSoundPacket().setRequest());
		PacketDispatcher.sendPacketToPlayer(packet, (Player)par1);
	}
	
	private void validdate(EntityPlayer par1)
	{
		if(playerToSounds.containsKey(par1.username))
		{
			return;
		}
		playerToSounds.put(par1.username, new ArrayList<String>());
	}
	
	public void onPacket(SpmodPacket par1)
	{
		ISpmodPacket basePacket = par1.getPacket();
		if(basePacket != null)
		{
			if(basePacket instanceof LoginSoundPacket)
			{
				LoginSoundPacket login = (LoginSoundPacket)basePacket;
				String owner = par1.getPlayer().username;
				playerToSounds.remove(owner);
				if(!login.isPrivate())
				{
					check(owner);
					for(String data : login.getData())
					{
						playerToSounds.get(owner).add(data);
					}
				}
			}
		}
	}
	
	private void check(String key)
	{
		if(!playerToSounds.containsKey(key))
		{
			playerToSounds.put(key, new ArrayList<String>());
		}
	}
	
	//Client Only
	
	public void init()
	{
		
	}
}
