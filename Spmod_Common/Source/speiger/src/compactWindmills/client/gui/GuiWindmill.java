package speiger.src.compactWindmills.client.gui;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.compactWindmills.CompactWindmills;
import speiger.src.compactWindmills.common.blocks.ContainerWindmill;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.core.CWPreference;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;

public class GuiWindmill extends GuiInventoryCore
{
	WindMill mill;
	
	public GuiWindmill(InventoryPlayer par1, WindMill par2)
	{
		super(new ContainerWindmill(par1, par2));
		mill = par2;
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Windmill", this.xSize / 2 - this.fontRenderer.getStringWidth("Windmill") / 2, 6, 4210752);
		this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
		this.fontRenderer.drawString("Rotor:", 44, 30, 0x404040);
		String eu = String.valueOf(mill.cuOutput);
		if(eu.length() > 4)
		{
			int size = eu.substring(eu.indexOf(".")).length();
			eu = eu.substring(0, eu.indexOf(".")+ (size < 3 ? size : 3));
		}
		this.fontRenderer.drawSplitString("Output " + eu + "EU", 105, 15, 70, 0x404040);
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(CWPreference.ModID.toLowerCase() + ":textures/gui/GuiWindmill.png");
	
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
}
