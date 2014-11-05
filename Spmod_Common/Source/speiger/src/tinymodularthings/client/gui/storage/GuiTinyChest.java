package speiger.src.tinymodularthings.client.gui.storage;

import java.util.ArrayList;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class GuiTinyChest extends GuiInventoryCore
{
	ISharedInventory tile;
	InventoryPlayer inv;
	
	public GuiTinyChest(InventoryPlayer par1, ISharedInventory par2)
	{
		super(par2.getInventory(par1));
		tile = par2;
		inv = par1;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Tiny Chest";
		if (tile.isEntity())
		{
			s = "Tiny Chest Cart";
		}
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
		
	}
	
	public static final ResourceLocation furnaceGuiTextures = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/storage/StorageGui.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		ArrayList<Slot> slotToDraw = ((AdvContainer) tile.getInventory(inv)).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
	}
}
