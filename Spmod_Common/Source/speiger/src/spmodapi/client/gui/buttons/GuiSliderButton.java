package speiger.src.spmodapi.client.gui.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;

public class GuiSliderButton extends GuiButton
{
	public float sliderValue = 1.0F;
	
	public boolean dragging;
	
	public String originalName;
	
	GuiInventoryCore core;
	
	public GuiSliderButton(int par1, int par2, int par3, String par4, float par5)
	{
		super(par1, par2, par3, 150, 20, par4);
		this.sliderValue = par5;
		originalName = par4;
	}
	
	public GuiSliderButton setGui(GuiInventoryCore par1)
	{
		core = par1;
		return this;
	}
	
	protected int getHoverState(boolean par1)
	{
		return 0;
	}
	
	protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3)
	{
		if(this.drawButton)
		{
			if(this.dragging)
			{
				this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
				
				if(this.sliderValue < 0.0F)
				{
					this.sliderValue = 0.0F;
				}
				
				if(this.sliderValue > 1.0F)
				{
					this.sliderValue = 1.0F;
				}
				int newNumber = (int)(sliderValue * 100);
				this.displayString = originalName + ": "+newNumber;
			}
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
			this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
		}
	}
	
	public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3)
	{
		if(super.mousePressed(par1Minecraft, par2, par3))
		{
			this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
			
			if(this.sliderValue < 0.0F)
			{
				this.sliderValue = 0.0F;
			}
			
			if(this.sliderValue > 1.0F)
			{
				this.sliderValue = 1.0F;
			}
			
			
			
			this.dragging = true;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void mouseReleased(int par1, int par2)
	{
		this.dragging = false;
		core.actionPerformed(this);
		
	}
	
}
