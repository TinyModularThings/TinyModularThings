package speiger.src.api.common.utils.world;

import net.minecraft.world.World;

public interface IWorldTickNotify
{
	//Will be handled only 1 time. Also Internal Stuff
	public void onTickStart(World world);
}
