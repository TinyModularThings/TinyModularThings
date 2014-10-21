package speiger.src.tinymodularthings.client.gui.crafting;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.commands.GuiCommands;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.client.gui.storage.GuiTinyChest;

public class GuiOreCrafter extends GuiInventoryCore
{
	
	public GuiOreCrafter(InventoryPlayer par1, AdvTile tile)
	{
		super(tile.getInventory(par1));
		this.ySize = 225;
		this.xSize = 210;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Oredictionary Crafter";
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 25, ySize - 96 + 2, 4210752);
	}
	
	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GuiCommands.furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		mc.getTextureManager().bindTexture(GuiTinyChest.furnaceGuiTextures);
		ArrayList<Slot> slotToDraw = ((AdvContainer) this.inventorySlots).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
	}
	
}
