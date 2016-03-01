package speiger.src.api.client.gui;

import java.util.List;

import speiger.src.api.common.inventory.IInfoSlot;

public class SpmodSlotLabel extends SpmodLabel
{
	public IInfoSlot slot;
	
	public SpmodSlotLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght, IInfoSlot info)
	{
		super(owner, minX, minY, xLenght, yLenght);
		slot = info;
	}
	
	public SpmodSlotLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght, boolean button, IInfoSlot info)
	{
		super(owner, minX, minY, xLenght, yLenght, button);
		slot = info;
	}
	
	@Override
	public List<String> getText()
	{
		return slot.getSlotInfo();
	}
}
