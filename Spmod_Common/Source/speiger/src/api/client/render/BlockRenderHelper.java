package speiger.src.api.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;

public class BlockRenderHelper
{
	static TextureEngine engine = TextureEngine.getTextures();
	
	public static void renderInInv(BlockStack par1, RenderBlocks par2)
	{
		Block block = par1.getBlock();
		block.setBlockBoundsForItemRender();
		renderInv(par1, par2);
	}
	
	public static void renderFluidBlock(Icon par1, RenderBlocks par2, int x, int y, int z, Block par3)
	{
		Block block = par3;
		Tessellator tessler = Tessellator.instance;

	    par2.setRenderBoundsFromBlock(block);

	    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, -1.0F, 0.0F);
	    par2.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 1.0F, 0.0F);
	    par2.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 0.0F, -1.0F);
	    par2.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 0.0F, 1.0F);
	    par2.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(-1.0F, 0.0F, 0.0F);
	    par2.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(1.0F, 0.0F, 0.0F);
	    par2.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(par1));
	    tessler.draw();

	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public static void renderInv(BlockStack par1, RenderBlocks par2)
	{
		Block block = par1.getBlock();
		Tessellator tessler = Tessellator.instance;

	    par2.setRenderBoundsFromBlock(block);

	    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, -1.0F, 0.0F);
	    par2.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(0, par1.getMeta())));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 1.0F, 0.0F);
	    par2.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(1, par1.getMeta())));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 0.0F, -1.0F);
	    par2.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(2, par1.getMeta())));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(0.0F, 0.0F, 1.0F);
	    par2.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(3, par1.getMeta())));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(-1.0F, 0.0F, 0.0F);
	    par2.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(4, par1.getMeta())));
	    tessler.draw();

	    tessler.startDrawingQuads();
	    tessler.setNormal(1.0F, 0.0F, 0.0F);
	    par2.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(5, par1.getMeta())));
	    tessler.draw();

	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public static void renderSide(BlockStack par1, RenderBlocks par2, int side)
	{
		Block block = par1.getBlock();
		Tessellator tessler = Tessellator.instance;
	
	    par2.setRenderBoundsFromBlock(block);
	
	    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
	    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	
	    switch(side)
	    {
	    	case 0:
			    tessler.startDrawingQuads();
			    tessler.setNormal(0.0F, -1.0F, 0.0F);
			    par2.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(0, par1.getMeta())));
			    tessler.draw();
			    break;
	    	case 1:
			    tessler.startDrawingQuads();
			    tessler.setNormal(0.0F, 1.0F, 0.0F);
			    par2.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(1, par1.getMeta())));
			    tessler.draw();
	    		break;
	    	case 2:
			    tessler.startDrawingQuads();
			    tessler.setNormal(0.0F, 0.0F, -1.0F);
			    par2.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(2, par1.getMeta())));
			    tessler.draw();
	    		break;
	    	case 3:
			    tessler.startDrawingQuads();
			    tessler.setNormal(0.0F, 0.0F, 1.0F);
			    par2.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(3, par1.getMeta())));
			    tessler.draw();
	    		break;
	    	case 4:
			    tessler.startDrawingQuads();
			    tessler.setNormal(-1.0F, 0.0F, 0.0F);
			    par2.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(4, par1.getMeta())));
			    tessler.draw();
	    		break;
	    	case 5:
			    tessler.startDrawingQuads();
			    tessler.setNormal(1.0F, 0.0F, 0.0F);
			    par2.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, engine.getIconSafe(block.getIcon(5, par1.getMeta())));
			    tessler.draw();
	    		break;
	    }
	
	    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	public static void renderSide(IAdvTile tile, int x, int y, int z, RenderBlocks par1, int side)
	{
		TileEntity par2 = (TileEntity)tile;
		switch(side)
		{
			case 0:
				par1.renderFaceYNeg(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
			case 1:
				par1.renderFaceYPos(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
			case 2:
				par1.renderFaceZNeg(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
			case 3:
				par1.renderFaceZPos(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
			case 4:
				par1.renderFaceXNeg(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
			case 5:
				par1.renderFaceXPos(par2.getBlockType(), x, y, z, tile.getEngine().getIconSafe(tile.getIconFromSideAndMetadata(side, tile.getRenderPass())));
				break;
		}
	}
}
