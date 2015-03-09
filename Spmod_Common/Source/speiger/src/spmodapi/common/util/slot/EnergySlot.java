package speiger.src.spmodapi.common.util.slot;

import speiger.src.api.common.world.tiles.energy.IEnergySubject;

public class EnergySlot
{
	IEnergySubject sub;
	SlotSize renderSize;
	int xCoord;
	int yCoord;
	
	public EnergySlot(IEnergySubject par1, int x, int y)
	{
		this(par1, SlotSize.Small, x, y);
	}
	
	public EnergySlot(IEnergySubject par1, SlotSize par2, int x, int y)
	{
		sub = par1;
		renderSize = par2;
		xCoord = x;
		yCoord = y;
	}
	
	public int getX()
	{
		return xCoord;
	}
	
	public int getY()
	{
		return yCoord;
	}
	
	public SlotSize getSize()
	{
		return renderSize;
	}
	
	public static enum SlotSize
	{
		Small,
		Medium,
		Big;
	}
}
