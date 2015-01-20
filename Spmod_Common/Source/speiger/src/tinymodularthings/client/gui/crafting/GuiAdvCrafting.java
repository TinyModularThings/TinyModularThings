package speiger.src.tinymodularthings.client.gui.crafting;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;

public class GuiAdvCrafting extends GuiInventoryCore
{
	public GuiAdvCrafting(IBlockGui par1, int par2, InventoryPlayer par3, BlockPosition par4)
	{
		super(par1.getInventory(par2, par3, par4));
		this.setAutoDrawing();
	} 
	
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String s = "Advanced Workbench";
		fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 4210752);
		fontRenderer.drawString(I18n.getString("container.inventory"), 8, ySize - 96 + 2, 4210752);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		setTexture(engine.getTexture("Objects"));
		defineSlot("ProgBarH");
		this.drawTexturedModalRect(k - 52, l, getX(), getY(), 23, 16);
	}


	@Override
	public ResourceLocation getGuiTexture()
	{
		return engine.getTexture("BasicFrame");
	}
	
	
	
}
