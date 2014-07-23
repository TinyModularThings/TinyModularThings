package speiger.src.spmodapi.client.gui.items.trades;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import speiger.src.api.items.IItemGui;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.items.trades.TradeInventory;
import speiger.src.spmodapi.common.lib.SpmodAPILib;

public class GuiTrade extends GuiInventoryCore
{
	MerchantRecipe recipe;
	public GuiTrade(InventoryPlayer par1, IItemGui item)
	{
		super(item.getContainer(par1, par1.player.getCurrentEquippedItem()));
		recipe = ((TradeInventory)this.inventorySlots).getCurrentTrade();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRenderer.drawString("Random Trade", 50, 7, 4210752);
	}
	
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(SpmodAPILib.ModID.toLowerCase() + ":textures/gui/items/trades/trades.png");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(furnaceGuiTextures);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
		defineSlot(176, 0);
		this.drawSlots();
		defineSlot(176, 64);
		this.drawSlotPros(100, 50, 27, 16);
		this.drawSlotPros(100, 25, 27, 16);
		
		GL11.glPushMatrix();
        ItemStack itemstack = recipe.getItemToBuy();
        ItemStack itemstack1 = recipe.getSecondItemToBuy();
        ItemStack itemstack2 = recipe.getItemToSell();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);
        
		
        itemRenderer.zLevel = 100.0F;
        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack, k + 30, l + 24);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack, k + 30, l + 24);

        if (itemstack1 != null)
        {
            itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack1, k + 70, l + 24);
            itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack1, k + 70, l + 24);
        }

        itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack2, k + 135, l + 24);
        itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.getTextureManager(), itemstack2, k + 135, l + 24);
        itemRenderer.zLevel = 0.0F;
        
        if (this.isPointInRegion(30, 24, 16, 16, par2, par3))
        {
            this.drawItemStackTooltip(itemstack, par2, par3);
        }
        else if (itemstack1 != null && this.isPointInRegion(70, 24, 16, 16, par2, par3))
        {
            this.drawItemStackTooltip(itemstack1, par2, par3);
        }
        else if (this.isPointInRegion(135, 24, 16, 16, par2, par3))
        {
            this.drawItemStackTooltip(itemstack2, par2, par3);
        }
        
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	

	
	
}
