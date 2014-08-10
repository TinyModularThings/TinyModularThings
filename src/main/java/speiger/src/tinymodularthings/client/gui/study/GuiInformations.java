package speiger.src.tinymodularthings.client.gui.study;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.slot.EmptyContainer;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class GuiInformations extends GuiInventoryCore
{
	public GuiInformations()
	{
		super(new EmptyContainer());
		ySize = 228;
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/study/InformationGui.png");
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = LanguageRegister.getLanguageName(this, "study.book", getCore());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
		
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}
