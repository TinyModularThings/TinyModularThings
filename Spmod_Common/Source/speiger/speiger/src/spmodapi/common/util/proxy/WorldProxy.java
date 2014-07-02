package speiger.src.spmodapi.common.util.proxy;

import net.minecraft.world.World;

public class WorldProxy
{
	public static int getGround(World world)
	{
		return world.provider.getAverageGroundLevel();
	}
	
	public static int getBaseHight(World world)
	{
		int i = getGround(world) + 1;
		return i / 64;
	}
	
	public static int getGenCounts(int factor, World world)
	{
		int i = factor * getBaseHight(world);
		int rounded = (int) Math.round(world.rand.nextGaussian() * Math.sqrt(i) + i);
		return rounded;
	}
}
