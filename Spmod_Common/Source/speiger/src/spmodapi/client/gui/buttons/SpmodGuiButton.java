package speiger.src.spmodapi.client.gui.buttons;

import org.lwjgl.input.Mouse;

import net.minecraft.client.gui.GuiButton;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;

public class SpmodGuiButton extends GuiButton
{
	public GuiInventoryCore core;
	public int lenght;
	public int height;
	
	public SpmodGuiButton(int par1, int par2, int par3, int par4, int par5, String par6)
	{
		super(par1, par2, par3, par4, par5, par6);
		lenght = par4;
		height = par5;
	}
	
	public SpmodGuiButton(int par1, int par2, int par3, String par4)
	{
		this(par1, par2, par3, 200, 20, par4);
	}
	
	public void handleMouseInput(int x, int y)
	{
	}

	public boolean isMouseOverMe(int x, int y)
	{
		return this.enabled && xPosition <= x && this.yPosition <= y && yPosition + height >= y && xPosition + lenght >= x;
	}
}
