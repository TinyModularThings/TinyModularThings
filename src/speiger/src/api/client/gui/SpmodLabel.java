package speiger.src.api.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SpmodLabel implements IGuiLabel
{
	
	public int minX;
	public int maxX;
	
	public int minY;
	public int maxY;
	
	public List<String> text = new ArrayList<String>();
	
	protected BaseGui screen;
	protected boolean requireButton;
	
	
	public SpmodLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght, String...text)
	{
		this(owner, minX, minY, xLenght, yLenght);
		this.addText(text);
	}
	
	public SpmodLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght, boolean button)
	{
		this(owner, minX, minY, xLenght, yLenght);
		requireButton = button;
	}
	
	public SpmodLabel(BaseGui owner, int minX, int minY, int xLenght, int yLenght)
	{
		this(owner);
		this.minX = minX;
		this.minY = minY;
		this.maxX = minX + xLenght;
		this.maxY = minY + yLenght;
		requireButton = true;
	}
	
	public SpmodLabel(BaseGui screen)
	{
		this.screen = screen;
	}
	
	public SpmodLabel addText(String...par1)
	{
		text.addAll(Arrays.asList(par1));
		return this;
	}
	
	public SpmodLabel addText(List<String> par1)
	{
		text.addAll(par1);
		return this;
	}
	
	//Render Function
	public void onRender(Minecraft mc, int x, int y)
	{
		if(isInBound(x, y))
		{
			renderText(mc, x, y);
		}
	}
	
	public boolean isInBound(int x, int y)
	{
		return x >= minX && x <= maxX && y >= minY && y <= maxY;
	}
	
	public void renderText(Minecraft mc, int x, int y)
	{
		if(getText().isEmpty())
		{
			return;
		}
		if(GuiScreen.isShiftKeyDown() && requireButton)
		{
			return;
		}
		if(!GuiScreen.isCtrlKeyDown() && requireButton)
		{
			return;
		}
		screen.drawHoverText(getText(), x, y);
	}
	
	public List<String> getText()
	{
		return text;
	}
	
}
