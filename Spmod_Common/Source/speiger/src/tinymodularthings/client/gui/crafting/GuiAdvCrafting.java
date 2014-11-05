package speiger.src.tinymodularthings.client.gui.crafting;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.client.gui.utils.GuiMobMachine;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.client.gui.storage.GuiTinyChest;

public class GuiAdvCrafting extends GuiInventoryCore
{
	public GuiAdvCrafting(IBlockGui par1, int par2, InventoryPlayer par3, BlockPosition par4)
	{
		super(par1.getInventory(par2, par3, par4));
	} 
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Advanced Workbench";
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GuiTinyChest.furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		ArrayList<Slot> slotToDraw = ((AdvContainer) this.inventorySlots).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
		mc.getTextureManager().bindTexture(GuiMobMachine.texture);
		this.drawTexturedModalRect(k + 73, l + 35, 176, 64, 30, 16);
	}
	
}
