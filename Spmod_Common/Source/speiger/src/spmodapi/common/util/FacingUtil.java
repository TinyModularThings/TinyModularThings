package speiger.src.spmodapi.common.util;

public class FacingUtil
{
	public static int getNextFacing(int lastFacing, boolean six)
	{
		if (six)
		{
			lastFacing++;
			if (lastFacing > 5)
			{
				return 0;
			}
			return lastFacing;
		}
		else
		{
			lastFacing++;
			if (lastFacing > 5)
			{
				return 2;
			}
			return lastFacing;
		}
		
	}
}
