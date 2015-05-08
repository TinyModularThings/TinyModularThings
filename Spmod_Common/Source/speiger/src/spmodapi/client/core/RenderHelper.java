package speiger.src.spmodapi.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHelper
{
	static ResourceLocation foodIcon = new ResourceLocation(SpmodAPILib.ModID.toString() + ":textures/gui/extras/foodIcons.png");
	static RenderItem itemRenderer = new RenderItem();
	static RenderBlocks blockRender = new RenderBlocks();
	static int GlobalRenderer = RenderingRegistry.getNextAvailableRenderId();
	
	static float[][] sizes = new float[][] { {0.0F, -1.0F, 0.0F }, {0.0F, 1.0F, 0.0F }, {0.0F, 0.0F, -1.0F }, {0.0F, 0.0F, 1.0F }, {-1.0F, 0.0F, 0.0F }, {1.0F, 0.0F, 0.0F } };
	
	public static ResourceLocation getFoodTexture()
	{
		return foodIcon;
	}
	
	public static int getGlobalRenderID()
	{
		return GlobalRenderer;
	}
	
	public static float[][] getFullBlockSize()
	{
		return sizes;
	}
	
	public static RenderItem getItemRenderer()
	{
		return itemRenderer;
	}
	
	public static RenderBlocks getBlockRenderer(IBlockAccess par1)
	{
		blockRender.blockAccess = par1;
		return blockRender;
	}
	
	public static void drawSaturationOverlay(float saturationGained, float saturationLevel, Minecraft mc, int left, int top, float alpha)
	{
		int startBar = saturationGained != 0.0F ? (int)saturationLevel / 2 : 0;
		int endBar = (int)Math.ceil(Math.min(20.0F, saturationLevel + saturationGained) / 2.0F);
		int barsNeeded = endBar - startBar;
		mc.getTextureManager().bindTexture(foodIcon);
		
		enableAlpha(alpha);
		for(int i = startBar;i < startBar + barsNeeded;i++)
		{
			int x = left - i * 8 - 9;
			int y = top;
			float effectiveSaturationOfBar = (saturationLevel + saturationGained) / 2.0F - i;
			
			if(effectiveSaturationOfBar >= 1.0F)
				mc.ingameGUI.drawTexturedModalRect(x, y, 27, 0, 9, 9);
			else if(effectiveSaturationOfBar > 0.5D)
				mc.ingameGUI.drawTexturedModalRect(x, y, 18, 0, 9, 9);
			else if(effectiveSaturationOfBar > 0.25D)
				mc.ingameGUI.drawTexturedModalRect(x, y, 9, 0, 9, 9);
			else if(effectiveSaturationOfBar > 0.0F)
				mc.ingameGUI.drawTexturedModalRect(x, y, 0, 0, 9, 9);
		}
		disableAlpha(alpha);
		
		mc.getTextureManager().bindTexture(Gui.icons);
	}
	
	public static void enableAlpha(float alpha)
	{
		if(alpha == 1.0F)
		{
			return;
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}
	
	public static void disableAlpha(float alpha)
	{
		if(alpha == 1.0F)
		{
			return;
		}
		GL11.glDisable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
