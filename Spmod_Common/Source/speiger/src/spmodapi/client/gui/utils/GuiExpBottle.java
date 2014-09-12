package speiger.src.spmodapi.client.gui.utils;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.blocks.utils.ExpStorage;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class GuiExpBottle extends GuiInventoryCore
{
	ExpStorage exp;
	
	public GuiExpBottle(InventoryPlayer player, ExpStorage par0)
	{
		super(par0.getInventory(player));
		exp = par0;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String string = LanguageRegister.getLanguageName(this, "exp.storage", getSpmodCore());
		fontRenderer.drawString(string, xSize / 2 - fontRenderer.getStringWidth(string) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
		String name = LanguageRegister.getLanguageName(this, "exp.stored", getSpmodCore());
		fontRenderer.drawString(name + ": " + exp.exp, 45, ySize - 110 + 2, 4210752);
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/gui/utils/StorageGui.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		defineSlot(176, 4);
		this.drawSlots();
	}
	
}
