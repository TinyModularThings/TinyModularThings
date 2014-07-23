package speiger.src.spmodapi.client.gui.items.trades;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.items.IItemGui;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class GuiTrade extends GuiInventoryCore
{
	
	public GuiTrade(InventoryPlayer par1, IItemGui item)
	{
		super(item.getContainer(par1, par1.player.getCurrentEquippedItem()));
		
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		
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
