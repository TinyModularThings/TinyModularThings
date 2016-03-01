package speiger.src.api.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.common.inventory.IInfoSlot;
import speiger.src.api.common.inventory.IUpdateInfoSlot;

public abstract class BaseGui extends GuiContainer
{
	public static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
	
	public List<IGuiLabel> label = new ArrayList<IGuiLabel>();
	
	public BaseGui(Container par1)
	{
		super(par1);
	}
	
	public void drawHoverText(List text, int x, int y)
	{
		this.drawHoveringText(text, x, y, fontRendererObj);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.label.clear();
		List slots = this.inventorySlots.inventorySlots;
		for(int i = 0;i < slots.size();i++)
		{
			Slot slot = (Slot)slots.get(i);
			if(slot instanceof IInfoSlot)
			{
				if(slot instanceof IUpdateInfoSlot)
				{
					int x = slot.xDisplayPosition + k;
					int y = slot.yDisplayPosition + l;
					label.add(new SpmodSlotLabel(this, x, y, 16, 16, (IInfoSlot)slot));
				}
				else
				{
					List<String> text = ((IInfoSlot)slot).getSlotInfo();
					if(text != null && text.size() > 0)
					{
						int x = slot.xDisplayPosition + k;
						int y = slot.yDisplayPosition + l;
						label.add(new SpmodLabel(this, x, y, 16, 16).addText(text));
					}
				}
			}
		}
	}
	
	public void addGuiLabel(IGuiLabel par1)
	{
		label.add(par1);
	}
	
	@Override
	public void drawScreen(int x, int y, float p_73863_3_)
	{
		super.drawScreen(x, y, p_73863_3_);
		for(int i = 0;i<this.label.size();i++)
		{
			IGuiLabel label = (IGuiLabel)this.label.get(i);
			if(label instanceof IGuiLabel)
			{
				label.onRender(mc, x, y);
			}
		}
	}
	
	public void displayGauge(int j, int k, int xOver, int yOver, int line, int col, int squaled, ResourceLocation texture, FluidStack liquid)
	{
		if(liquid == null)
		{
			return;
		}
		int start = 0;
		
		IIcon liquidIcon = null;
		Fluid fluid = liquid.getFluid();
		if(fluid != null && fluid.getStillIcon() != null)
		{
			liquidIcon = fluid.getStillIcon();
		}
		mc.renderEngine.bindTexture(BLOCK_TEXTURE);
		
		if(liquidIcon != null)
		{
			while(true)
			{
				int x;
				
				if(squaled > 16)
				{
					x = 16;
					squaled -= 16;
				}
				else
				{
					x = squaled;
					squaled = 0;
				}
				
				drawTexturedModelRectFromIcon(j + col, k + line + 58 - x - start, liquidIcon, 16, 16 - (16 - x));
				start = start + 16;
				
				if(x == 0 || squaled == 0)
				{
					break;
				}
			}
		}
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(j + col, k + line, xOver, yOver, 16, 60);
	}
	
}
