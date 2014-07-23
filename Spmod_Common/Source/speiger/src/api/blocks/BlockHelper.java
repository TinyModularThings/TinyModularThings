package speiger.src.api.blocks;

public class BlockHelper
{
	public static String getPlacingMode(int i)
	{
		switch (i)
		{
			case 0:
				return "mode.placing.looking";
			case 1:
				return "mode.placing.looking.opposite";
			case 2:
				return "mode.placing.block.side";
			case 3:
				return "mode.placing.block.side.opposite";
			default:
				return "error.nothing";
		}
	}
	
	public static int getMaxPlaceingModes()
	{
		return 4;
	}
}
