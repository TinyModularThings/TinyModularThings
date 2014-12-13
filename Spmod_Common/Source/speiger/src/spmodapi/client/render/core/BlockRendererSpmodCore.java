package speiger.src.spmodapi.client.render.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.render.IBlockRenderer;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.core.RenderHelper;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.FMLLog;

public class BlockRendererSpmodCore implements ISimpleBlockRenderingHandler, IItemRenderer
{	
	public static BlockRendererSpmodCore instance = new BlockRendererSpmodCore();
	static TextureEngine engine = TextureEngine.getTextures();
	static BlockRendererHelper helper = new BlockRendererHelper();
	

	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if(block != null)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if(block instanceof IBlockRenderer)
			{
				IBlockRenderer render = (IBlockRenderer)block;
				if(render.requiresRenderer(meta))
				{
					RenderBlocksSpmod rendering = new RenderBlocksSpmod(renderer);
					BlockStack stack = new BlockStack(block, meta);
					if(render.requiresMultibleRenderPasses(meta))
					{
						int passes = render.getRenderPasses(meta);
						for(int i = 0;i<passes;i++)
						{
							render.onRender(world, x, y, z, rendering, stack, i);
						}
					}
					else
					{
						render.onRender(world, x, y, z, rendering, stack, 0);
					}
				}
				else if(render.dissableRendering(meta))
				{
					
				}
				else
				{
					renderer.renderStandardBlock(block, x, y, z);
				}
			}
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}

	@Override
	public int getRenderId()
	{
		return RenderHelper.getGlobalRenderID();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return type == type.FIRST_PERSON_MAP ? false : new BlockStack(item).getCastedBlock(IBlockRenderer.class).renderItemBlock(item.getItemDamage());
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		BlockStack stack = new BlockStack(item);
		IBlockRenderer render = stack.getCastedBlock(IBlockRenderer.class);
		if(render != null)
		{
			RenderBlocks renderer = (RenderBlocks)data[0];
			if(render.renderItemBlockBasic(item.getItemDamage()))
			{
				renderBlock(render, stack, renderer, type);
			}
			else
			{
				float[] key = render.getXYZForItemRenderer(type, stack.getMeta());
				if(key == null || key.length != 3)
				{
					key = new float[]{0.5F, 0.5F, 0.5F};
				}
				int renderPasses = render.getItemRenderPasses(stack.getMeta());
				if(renderPasses <= 0)
				{
					renderPasses = 1;
				}
				for(int i = 0;i<renderPasses;i++)
				{
					if(type == type.INVENTORY)
					{
						render.onItemRendering(helper, type, stack, i, key[0], key[1], key[2], item, renderer);
					}
					else
					{
						render.onItemRendering(helper, type, stack, i, key[0], key[1], key[2], item, renderer, data[1]);
					}
				}
			}
		}
	}
	
	public void renderBlock(IBlockRenderer par1, BlockStack par2, RenderBlocks par3, ItemRenderType par4)
	{
		float[] size = par1.getBoundingBoxes(par2.getMeta());
		if(size == null || size.length < 6)
		{
			size = new float[]{0,0,0,1,1,1};
		}
		Icon[] textureArray = new Icon[6];
		for(int i = 0;i<6;i++)
		{
			Icon blockIcon = par2.getTexture(i);
			if(blockIcon == null || !engine.isTextureRegistered(blockIcon, engine.getTextureMap()))
			{
				blockIcon = engine.getIconSafe();
			}
			textureArray[i] = blockIcon;
		}
		float[] data = par1.getXYZForItemRenderer(par4, par2.getMeta());
		if(data == null || data.length != 3)
		{
			data = new float[]{-0.5F, -0.5F, -0.5F};
		}
		
		Tessellator tessellator = Tessellator.instance;
		Block block = par2.getBlock();
		block.setBlockBounds(size[0], size[1], size[2], size[3], size[4], size[5]);
		block.setBlockBoundsForItemRender();
		par3.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(data[0], data[1], data[2]);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		par3.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par3.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textureArray[1]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		par3.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textureArray[2]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		par3.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textureArray[3]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		par3.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textureArray[4]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		par3.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textureArray[5]);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static class BlockRendererHelper
	{
		public static void renderBlockStandart(IBlockRenderer par1, BlockStack par2, RenderBlocks par3, ItemRenderType par4)
		{
			BlockRendererSpmodCore.instance.renderBlock(par1, par2, par3, par4);
		}
		
		public static void renderBlockStandart(Icon[] texture, float[] sides, Block block, float[] sizes, RenderBlocks render)
		{
			if(sizes == null || sizes.length != 3)
			{
				sizes = new float[]{-0.5F, -0.5F, -0.5F};
			}
			
			Tessellator tessellator = Tessellator.instance;
			block.setBlockBounds(sides[0], sides[1], sides[2], sides[3], sides[4], sides[5]);
			block.setBlockBoundsForItemRender();
			render.setRenderBoundsFromBlock(block);
			GL11.glTranslatef(sizes[0], sizes[1], sizes[2]);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, -1.0F, 0.0F);
			render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, texture[0]);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 1.0F, 0.0F);
			render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, texture[1]);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, -1.0F);
			render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, texture[2]);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0.0F, 0.0F, 1.0F);
			render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, texture[3]);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1.0F, 0.0F, 0.0F);
			render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, texture[4]);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1.0F, 0.0F, 0.0F);
			render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, texture[5]);
			tessellator.draw();
			GL11.glTranslatef(-sizes[0], -sizes[1], -sizes[2]);
			block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
}
