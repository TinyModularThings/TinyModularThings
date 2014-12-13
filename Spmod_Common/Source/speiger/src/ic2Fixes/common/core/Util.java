package speiger.src.ic2Fixes.common.core;

public class Util
{
	public static double lerp(double start, double end, double fraction)
	{
		assert ((fraction >= 0.0D) && (fraction <= 1.0D));
		
		return start + (end - start) * fraction;
	}
}
