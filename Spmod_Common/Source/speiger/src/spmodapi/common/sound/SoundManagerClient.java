package speiger.src.spmodapi.common.sound;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.utils.config.EntityCounter;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.network.packets.common.LoginSoundPacket;
import cpw.mods.fml.common.network.PacketDispatcher;

public class SoundManagerClient extends SoundManager
{
	private File soundFolder;
	private boolean inited = false;
	private HashMap<Integer, String> soundIDs = new HashMap<Integer, String>();
	private EntityCounter counter = new EntityCounter(0);
	
	@Override
	public void init()
	{
		if(!inited)
		{
			inited = true;
			soundFolder = new File(Minecraft.getMinecraft().mcDataDir, "SpmodSoundLibary");
			if(!soundFolder.exists() || !soundFolder.isDirectory())
			{
				if(!soundFolder.mkdirs())
				{
					inited = false;
				}
			}
			if(!soundFolder.exists())
			{
				inited = false;
			}
		}
		if(inited)
		{
			File[] data = soundFolder.listFiles();
			if(data != null && data.length > 0)
			{
				for(int i = 0;i<data.length;i++)
				{
					try
					{
						File file = data[i];
						if(isValidFile(file))
						{
							URL url = file.toURI().toURL();
							String name = getTrimmedName(file.getName());
							SpmodAPI.soundEngine.registerSound(name, file.getName(), url);
							soundIDs.put(counter.getAndUpdateID(), name);
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private String getTrimmedName(String par1)
	{
		return par1.substring(0, par1.lastIndexOf("."));
	}
	
	private boolean isValidFile(File par1)
	{
		if(par1 == null)
		{
			return false;
		}
		if(par1.getName().endsWith(".ogg") || par1.getName().endsWith(".wav"))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void onPacket(SpmodPacket par1)
	{
		ISpmodPacket basePacket = par1.getPacket();
		if(basePacket != null)
		{
			if(basePacket instanceof LoginSoundPacket && ((LoginSoundPacket)basePacket).isRequest())
			{
				sendAnswer();
			}
		}
	}
	
	public void sendAnswer()
	{
		boolean isPrivate = !SpmodConfig.booleanInfos.get("ShareSounds");
		String[] data = isPrivate ? new String[0] : soundIDs.values().toArray(new String[0]);
		ISpmodPacket packet = new LoginSoundPacket(isPrivate, data);
		PacketDispatcher.sendPacketToServer(SpmodAPI.handler.createFinishPacket(packet));
	}
}
