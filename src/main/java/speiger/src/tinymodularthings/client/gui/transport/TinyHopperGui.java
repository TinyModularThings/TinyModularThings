package speiger.src.tinymodularthings.client.gui.transport;

import java.util.ArrayList;

import javax.swing.Icon;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IHopperInventory;
import speiger.src.api.inventory.TankSlot;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.utils.HopperType;

public class TinyHopperGui extends GuiInventoryCore
{
	AdvTile tile;
	InventoryPlayer inv;
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("TinyModularThings".toLowerCase() + ":textures/gui/storage/StorageGui.png");
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
	
	public TinyHopperGui(InventoryPlayer par1, AdvTile par2)
	{
		super(par2.getInventory(par1));
		tile = par2;
		inv = par1;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Tiny Hopper";
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 3, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
		IHopperInventory hopperInv = (IHopperInventory) tile;
		IHopper hopper = (IHopper) tile;
		if (hopperInv.getHopperType() == HopperType.Energy)
		{
			String text = "" + hopper.getEnergyStorage().getEnergy() + "MJ / " + hopper.getEnergyStorage().getMaxStorage() + " MJ";
			fontRenderer.drawString("Stored Energy:", 45, 30, 4210752);
			fontRenderer.drawString(text, 50, 40, 4210752);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
  {
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    mc.getTextureManager().bindTexture(furnaceGuiTextures);
    int k = (width - xSize) / 2;
    int l = (height - ySize) / 2;
    drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
    IHopperInventory hopper = (IHopperInventory)tile;
    switch (hopper.getHopperType())
    {
    case Fluids:
      ArrayList tanks = ((AdvContainer)tile.getInventory(inv)).getTanks();
      defineSlot(176, 24);
      for (int i = 0; i < tanks.size(); i++)
      {
        TankSlot slots = (TankSlot)tanks.get(i);
        drawSlotPros(slots.getXCoord(), slots.getYCoord(), 18, 59);
        if (slots.getTank() != null)
        {
          displayGauge(k, l, slots.getYCoord(), slots.getXCoord(), slots.getTank().getFluid() != null ? slots.getTank().getFluid().amount / 275 : 0, slots.getTank().getFluid());
        }
      }
      break;
    case Items:
      ArrayList slotToDraw = ((AdvContainer)tile.getInventory(inv)).getAllSlots();
      defineSlot(176, 4);
      for (int i = 0; i < slotToDraw.size(); i++)
      {
        drawSlot((Slot)slotToDraw.get(i));
      }
      break;
    case Energy:
    	break;
	case Nothing:
		break;
	default:
		break;
    }
  }
	
	private void displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid)
	{
		if (liquid == null)
		{
			return;
		}
		int start = 0;
		
		Icon liquidIcon = null;
		Fluid fluid = liquid.getFluid();
		if ((fluid != null) && (fluid.getStillIcon() != null))
		{
			liquidIcon = fluid.getStillIcon();
		}
		
		mc.renderEngine.bindTexture(BLOCK_TEXTURE);
		
		if (liquidIcon != null)
		{
			while (true)
			{
				int x;
				if (squaled > 16)
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
				start += 16;
				
				if ((x == 0) || (squaled == 0))
				{
					break;
				}
			}
		}
		mc.renderEngine.bindTexture(furnaceGuiTextures);
		drawTexturedModalRect(j + col, k + line, 197, 25, 16, 60);
	}
}