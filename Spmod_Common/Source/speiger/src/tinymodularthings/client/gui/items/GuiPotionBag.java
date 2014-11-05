package speiger.src.tinymodularthings.client.gui.items;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.gui.IItemGui;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class GuiPotionBag extends GuiInventoryCore
{
	InventoryPlayer inv;
	ItemStack stack;
	
	public GuiPotionBag(InventoryPlayer par1, Container par2)
	{
		super(par2);
		inv = par1;
		stack = par1.player.getCurrentEquippedItem();
		this.ySize = 221;
	}

	
	private static final ResourceLocation texture = new ResourceLocation(TinyModularThingsLib.ModID.toLowerCase() + ":textures/gui/items/PotionBagGui.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(texture);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		IItemGui tile = (IItemGui)stack.getItem();
		ArrayList<Slot> slotToDraw = ((AdvContainer) tile.getContainer(inv, stack)).getAllSlots();
		defineSlot(176, 4);
		for (int i = 0; i < slotToDraw.size(); i++)
		{
			this.drawSlot(slotToDraw.get(i));
		}
	}
	
}
