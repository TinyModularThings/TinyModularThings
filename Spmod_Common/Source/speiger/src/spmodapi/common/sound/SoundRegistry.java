package speiger.src.spmodapi.common.sound;

import net.minecraft.world.World;

public class SoundRegistry
{
	public void registerSound(String name, String path)
	{
		
	}

	public void playSound(World world, int x, int y, int z, String name, float volume, float pitch)
	{
		
	}

	public String getSoundID(World world, int x, int y, int z)
	{
		return "SoundDissabled";
	}

	public String createSound(String soundName, World world, int x, int y, int z, boolean priority)
	{
		return "SoundDissabled";
	}

	public boolean isPlayingSound(String id)
	{
		return false;
	}

	public void changeSoundVolumeAndPitch(String name, float loudness, float pitch)
	{
		
	}

	public void stopAndRemoveSound(String name)
	{
		
	}

	public void setLoop(String name, boolean par2)
	{
		
	}

	public void playSound(String name)
	{
		
	}

	public void stopSound(String name)
	{
		
	}

	public void pauseSound(String name)
	{
		
	}

	public void stopAndRemoveSound(World world, int x, int y, int z, String name)
	{
		
	}

	public void restartSound(World world, int x, int y, int z, float newLoudness, float newPitch)
	{
		
	}
}
