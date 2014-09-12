package speiger.src.tinymodularthings.common.upgrades.hoppers.guis;

import java.util.ArrayList;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.slot.AdvContainer;

public class FilterGui extends GuiInventoryCore
{
	public FilterGui(Container par1)
	{
		super(par1);
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("TinyModularThings".toLowerCase() + ":textures/gui/storage/StorageGui.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		ArrayList slotToDraw = ((AdvContainer) this.inventorySlots).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			drawSlot((Slot) slotToDraw.get(i));
		}
	}
	
}
