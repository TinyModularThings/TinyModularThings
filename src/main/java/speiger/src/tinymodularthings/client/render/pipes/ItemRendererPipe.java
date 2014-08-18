package speiger.src.tinymodularthings.client.render.pipes;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.tinymodularthings.common.blocks.pipes.basic.ItemBlockPipe;

public class ItemRendererPipe implements IItemRenderer
{
	Block block;
	
	public ItemRendererPipe(Block block)
	{
		this.block = block;
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
			case ENTITY:
				renderItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			case EQUIPPED:
				renderItem((RenderBlocks) data[0], item, -0.4f, 0.50f, 0.35f);
				break;
			case INVENTORY:
				renderItem((RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
				break;
			default:
		}
	}
	
	private void renderItem(RenderBlocks render, ItemStack item, float x, float y, float z)
	{
		Tessellator tessellator = Tessellator.instance;
		IIcon icon = ((ItemBlockPipe) item.getItem()).getIconFromDamage(0);
		
		if (icon == null)
		{
			icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}
		
		block.setBlockBounds(0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
		block.setBlockBoundsForItemRender();
		render.setRenderBoundsFromBlock(block);
		
		GL11.glTranslatef(x, y, z);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1F, 0.0F);
		render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1F);
		render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		
	}
	
}
