package speiger.src.api.inventory;

public class FakeSlot
{
	/** Left Top Corner XCoord **/
	int xCoord;
	/** Left Top Corner YCoord **/
	int yCoord;
	
	int xSize = 16;
	
	int ySize = 16;
	
	int FakeID;
	
	public FakeSlot(int id, int x, int y)
	{
		FakeID = id;
		xCoord = x;
		yCoord = y;
	}
	
	public int getID()
	{
		return FakeID;
	}
	
	public int getXCoord()
	{
		return xCoord;
	}
	
	public int getYCoord()
	{
		return yCoord;
	}
	
	public boolean isInXRange(int x)
	{
		if (x > xCoord && x < xCoord + xSize)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isInYRange(int y)
	{
		if (y > yCoord && y < yCoord + ySize)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isInRange(int x, int y)
	{
		return isInXRange(x) && isInYRange(y);
	}
}
