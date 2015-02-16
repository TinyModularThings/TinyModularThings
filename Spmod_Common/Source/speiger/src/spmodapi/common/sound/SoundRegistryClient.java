package speiger.src.spmodapi.common.sound;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.world.World;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;
import paulscode.sound.SoundSystem;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;

import com.google.common.base.Strings;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author Speiger
 * 
 */
public class SoundRegistryClient extends SoundRegistry implements ITickHandler
{
	
	@SideOnly(Side.CLIENT)
	private SoundSystem system;
	@SideOnly(Side.CLIENT)
	private GameSettings settings;
	
	private HashMap<String, SoundPoolEntry> sounds = new HashMap<String, SoundPoolEntry>();
	
	@SideOnly(Side.CLIENT)
	private SoundManager manager;
			
	private HashMap<List<Integer>, String> soundKeepers = new HashMap<List<Integer>, String>();
	
	private float lastLoudness = 1.0F;
	
	/**
	 * My Sound Controll
	 */
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void init(SoundLoadEvent evt)
	{
		manager = evt.manager;
		system = evt.manager.sndSystem;
		settings = Minecraft.getMinecraft().gameSettings;
		lastLoudness = settings.soundVolume;
		SpmodAPI.log.print("Register " + sounds.size() + " Sounds");
	}
	
	@Override
	public void registerSound(String name, String path)
	{
		SoundPoolEntry sound = new SoundPoolEntry(path, SoundRegistry.class.getClassLoader().getResource(path));
		if (sound != null && sound.getSoundName().equals(path) && sound.getSoundUrl() != null)
		{
			sounds.put(name, sound);
			return;
		}
		System.exit(0);
	}
	
	/**
	 * My Play Function. I made it new because the Vanilla system is limited to
	 * 256 songs which are not detectable
	 * 
	 */
	@Override
	public void playSound(World world, int x, int y, int z, String name, float volume, float pitch)
	{
		if (!SpmodConfig.booleanInfos.get("PlaySounds"))
		{
			return;
		}
		
		checkSounds();
		
		SoundPoolEntry sound = getSoundPoolEntry(name);
		if (system != null && sound != null && sound.getSoundName() != null && sound.getSoundUrl() != null && volume > 0.0F)
		{
			String id = createID(name, world, x, y, z);
			float f5 = 16.0F;
			
			if (volume > 1.0F)
			{
				f5 *= volume;
			}
			soundKeepers.put(new BlockPosition(world, x, y, z).getAsList(), id);
			system.newSource(volume > 1.0F, id, sound.getSoundUrl(), sound.getSoundName(), false, x, y, z, 2, f5);
			
			if (volume > 1.0F)
			{
				volume = 1.0F;
			}
			
			system.setPitch(id, pitch);
			system.setVolume(id, volume * settings.soundVolume);
			system.play(id);
			
		}
		else if(system != null && sound == null)
		{
			SoundPoolEntry entry = manager.soundPoolSounds.getRandomSoundFromSoundPool(name);
			if(entry != null)
			{
				sounds.put(name, entry);
				playSound(world, x, y, z, name, volume, pitch);
			}
			return;
		}
		
	}
	
	@Override
	public String getSoundID(World world, int x, int y, int z)
	{
		return soundKeepers.get(new BlockPosition(world, x, y, z).getAsList());
	}
	
	@Override
	public void restartSound(World world, int x, int y, int z, float newLoudness, float newPitch)
	{
		String name = getSoundID(world, x, y, z);
		if(!Strings.isNullOrEmpty(name))
		{
			system.stop(name);
			system.play(name);
		}
	}
	
	@Override
	public void stopAndRemoveSound(World world, int x, int y, int z, String name)
	{
		checkSounds();
		String id = createID(name, world, x, y, z);
		system.stop(id);
		system.removeSource(id);
		
	}
	
	@Override
	public void pauseSound(String name)
	{
		checkSounds();
		system.pause(name);
	}
	
	@Override
	public void stopSound(String name)
	{
		checkSounds();
		system.stop(name);
	}
	
	@Override
	public void playSound(String name)
	{
		checkSounds();
		system.play(name);
	}
	
	@Override
	public void setLoop(String name, boolean par2)
	{
		checkSounds();
		system.setLooping(name, par2);
	}
	
	@Override
	public void stopAndRemoveSound(String name)
	{
		checkSounds();
		if(system.playing(name))
		{
			system.stop(name);
		}
		system.removeSource(name);
	}
	
	@Override
	public void changeSoundVolumeAndPitch(String name, float loudness, float pitch)
	{
		checkSounds();
		system.setPitch(name, pitch);
		system.setVolume(name, loudness);
	}
	
	@Override
	public boolean isPlayingSound(String id)
	{
		checkSounds();
		return system.playing(id);
	}
	
	@Override
	public String createSound(String soundName, World world, int x, int y, int z, boolean priority)
	{
		if (!SpmodConfig.booleanInfos.get("PlaySounds"))
		{
			return "SoundDissabled";
		}
		checkSounds();
		SoundPoolEntry sound = getSoundPoolEntry(soundName);
		if (system != null && sound != null && sound.getSoundName() != null && sound.getSoundUrl() != null)
		{
			String id = createID(soundName, world, x, y, z);
			soundKeepers.put(new BlockPosition(world, x, y, z).getAsList(), id);
			system.newSource(priority, id, sound.getSoundUrl(), sound.getSoundName(), false, x, y, z, 2, 16);
			return id;
		}
		else if(system != null && sound == null)
		{
			SoundPoolEntry entry = manager.soundPoolSounds.getRandomSoundFromSoundPool(soundName);
			if(entry != null)
			{
				sounds.put(soundName, entry);
				return createSound(soundName, world, x, y, z, priority);
			}
		}
		return "No Sound";
		
	}
	
	private void checkSounds()
	{
		if (system == null)
		{
			system = manager.sndSystem;
		}
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

		@Override
		public String toString()
		{
			return "Sound Waiter: "+name+":"+path;
		}
		
		
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData)
	{
		if(lastLoudness != settings.soundVolume)
		{
			if(system != null)
			{
				for(String par1 : soundKeepers.values())
				{
					float oldSound = system.getVolume(par1);
					oldSound /= lastLoudness;
					oldSound *= settings.soundVolume;
					system.setVolume(par1, oldSound);
				}
			}
			lastLoudness = settings.soundVolume;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData)
	{
		
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel()
	{
		return "Spmod SoundHandler";
	}
	
}
