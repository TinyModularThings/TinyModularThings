package speiger.src.spmodapi.common.sound;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import paulscode.sound.SoundSystem;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Speiger
 * 
 */
public class SoundRegistry
{
	
	@SideOnly(Side.CLIENT)
	private SoundSystem system;
	@SideOnly(Side.CLIENT)
	private GameSettings settings;
	
	private HashMap<String, SoundPoolEntry> sounds = new HashMap<String, SoundPoolEntry>();
	
	@SideOnly(Side.CLIENT)
	private SoundManager manager;
	
	private ArrayList<SoundWaiter> waitingList = new ArrayList<SoundWaiter>();
	
	/**
	 * My Sound Controll
	 */
	
	private static SoundRegistry instance = new SoundRegistry();
	
	public static SoundRegistry getInstance()
	{
		return instance;
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void init(SoundLoadEvent evt)
	{
		manager = evt.manager;
		system = evt.manager.sndSystem;
		settings = Minecraft.getMinecraft().gameSettings;
		tryToRegist();
		SpmodAPI.log.print("Register " + sounds.size() + " Sounds");
	}
	
	public void registerSound(String name, String path)
	{
		if (manager != null)
		{
			manager.soundPoolSounds.addSound(path);
			while (!sounds.containsKey(name))
			{
				String newString = path.substring(0, path.lastIndexOf("."));
				SoundPoolEntry sound = manager.soundPoolSounds.getRandomSoundFromSoundPool(newString);
				if (sound != null && sound.getSoundName().equals(path) && !sounds.containsValue(sound))
				{
					waitingList.remove(new SoundWaiter(name, path));
					sounds.put(name, sound);
				}
			}
			
			return;
		}
		else
		{
			waitingList.add(new SoundWaiter(name, path));
		}
		
	}
	
	public void tryToRegist()
	{
		for (int i = 0; i < waitingList.size(); i++)
		{
			SoundWaiter current = waitingList.get(i);
			if (current != null)
			{
				registerSound(current.getName(), current.getPath());
			}
		}
	}
	
	/**
	 * My Play Function. I made it new because the Vanilla system is limited to
	 * 256 songs which are not detectable
	 * 
	 */
	public void playSound(World world, int x, int y, int z, String name, float volume, float pitch)
	{
		
		if (!SpmodConfig.playSounds)
		{
			return;
		}
		
		if (system == null)
		{
			system = manager.sndSystem;
			playSound(world, x, y, z, name, volume, pitch);
			
			return;
		}
		
		SoundPoolEntry sound = getSoundPoolEntry(name);
		if (system != null && sound != null && sound.getSoundName() != null && sound.getSoundUrl() != null && volume > 0.0F)
		{
			String id = createID(name, world, x, y, z);
			float f5 = 16.0F;
			
			if (volume > 1.0F)
			{
				f5 *= volume;
			}
			
			system.newSource(volume > 1.0F, id, sound.getSoundUrl(), sound.getSoundName(), false, x, y, z, 2, f5);
			
			if (volume > 1.0F)
			{
				volume = 1.0F;
			}
			
			system.setPitch(id, pitch);
			system.setVolume(id, volume * settings.soundVolume);
			
			system.play(id);
			
		}
		
	}
	
	public void restartSound(String name, World world, int x, int y, int z, float newLoudness, float newPitch)
	{
		stopSound(world, x, y, z, name);
		playSound(world, x, y, z, name, newLoudness, newPitch);
	}
	
	public void stopSound(World world, int x, int y, int z, String name)
	{
		if (system == null)
		{
			system = manager.sndSystem;
			stopSound(world, x, y, z, name);
			return;
		}
		String id = createID(name, world, x, y, z);
		
		system.stop(id);
		system.removeSource(id);
		
	}
	
	private SoundPoolEntry getSoundPoolEntry(String name)
	{
		return sounds.get(name);
	}
	
	private String createID(String name, World world, int x, int y, int z)
	{
		return "SpmodSound_" + name + "_" + world.provider.getDimensionName() + "_" + x + "_" + y + "_" + z;
	}
	
	public class SoundWaiter
	{
		private String name;
		private String path;
		
		public SoundWaiter(String Name, String Path)
		{
			name = Name;
			path = Path;
		}
		
		public String getName()
		{
			return name;
		}
		
		public String getPath()
		{
			return path;
		}
	}
	
}
