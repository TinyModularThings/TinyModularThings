package speiger.src.tinymodularthings.client.gui.entity;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.spmodapi.common.util.slot.PlayerSlot;
import speiger.src.spmodapi.common.util.slot.SpmodSlot;
import speiger.src.tinymodularthings.common.interfaces.IEntityGuiProvider;

public class GuiTinyChestCart extends GuiInventoryCore
{
	IEntityGuiProvider gui;
	
	public GuiTinyChestCart(AdvContainer par1, IEntityGuiProvider par2)
	{
		super(par1);
		gui = par2;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = gui.getInvName();
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		setTexture(engine.getTexture("BasicFrame"));
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		setTexture(engine.getTexture("Objects"));
		this.defineSlot("Slot");
		ArrayList<SpmodSlot> slotToDraw = ((AdvContainer)this.inventorySlots).getAllSlots();
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
		ArrayList<PlayerSlot> slot = ((AdvContainer)this.inventorySlots).getPlayerSlots();
		for (int i = 0; i < slot.size(); i++)
		{
			this.drawSlot(slot.get(i));
		}
	}
	
}
