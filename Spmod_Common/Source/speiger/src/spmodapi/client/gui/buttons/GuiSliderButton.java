package speiger.src.spmodapi.client.gui.buttons;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;

public class GuiSliderButton extends SpmodGuiButton
{
	public float sliderValue = 1.0F;
	
	private float weelEffect = 0.0F;
	
	public boolean dragging;
	
	public String originalName;
	
	public GuiSliderButton(int par1, int par2, int par3, String par4, float par5, GuiInventoryCore par6)
	{
		super(par1, par2, par3, 150, 20, par4);
		this.sliderValue = par5;
		originalName = par4;
		core = par6;
		core.onButtonUpdate(this);
	}
	
	public GuiSliderButton setWeelEffect(float par1)
	{
		weelEffect = par1;
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
				if(core != null)
				{
					core.onButtonUpdate(this);
				}
				else
				{
					int newNumber = (int)(sliderValue * 100);
					this.displayString = originalName + ": "+newNumber+"%";
				}
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
			
			if(core != null)
			{
				core.onButtonUpdate(this);
			}
			else
			{
				int newNumber = (int)(sliderValue * 100);
				this.displayString = originalName + ": "+newNumber+"%";
			}
			
			this.dragging = true;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public void handleMouseInput(int x, int y)
	{
		int weel = Mouse.getEventDWheel();
		if(weel != 0 && weelEffect > 0.0F)
		{
			weel = (weel / 120);
			sliderValue += (weel * weelEffect);
			if(this.sliderValue < 0.0F)
			{
				this.sliderValue = 0.0F;
			}
			
			if(this.sliderValue > 1.0F)
			{
				this.sliderValue = 1.0F;
			}
			core.onButtonUpdate(this);
			core.releaseButton(this);
		}
	}

	public void mouseReleased(int par1, int par2)
	{
		this.dragging = false;
		if(core != null)
		{
			core.releaseButton(this);
		}
	}
	
}
