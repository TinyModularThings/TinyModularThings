package speiger.src.api.client.gui;

import java.util.List;

public abstract class SpmodUpdatingLabel extends SpmodLabel
{

	public SpmodUpdatingLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght)
	{
		super(owner, minX, minY, xLenght, yLenght);
	}
	
	public SpmodUpdatingLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght, boolean button)
	{
		super(owner, minX, minY, xLenght, yLenght, button);
	}
	
	@Override
	public abstract List<String> getText();
	
}
