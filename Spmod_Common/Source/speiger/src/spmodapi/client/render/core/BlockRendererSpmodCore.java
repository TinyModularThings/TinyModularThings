package speiger.src.spmodapi.client.render.core;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import speiger.src.api.client.render.IBlockRenderer;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.core.RenderHelper;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TextureEngine.BlockData;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererSpmodCore implements ISimpleBlockRenderingHandler, IItemRenderer
{
	ArrayList<BlockData> data = new ArrayList<BlockData>();
	
	public static BlockRendererSpmodCore instance = new BlockRendererSpmodCore();
	static TextureEngine engine = TextureEngine.getTextures();
	static BlockRendererHelper helper = new BlockRendererHelper();
	
	public void addBlockToRender(Block block)
	{
		data.add(new BlockData(block));
	}
	
	public void register()
	{
		for(BlockData block : data)
		{
			MinecraftForgeClient.registerItemRenderer(block.getResult().itemID, instance);
		}
		data.clear();
	}
	
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
					BlockStack stack = new BlockStack(block, meta);
					if(render.requiresMultibleRenderPasses(meta))
					{
						int passes = render.getRenderPasses(meta);
						for(int i = 0;i<passes;i++)
						{
							render.onRender(world, x, y, z, renderer, stack, i);
						}
					}
					else
					{
						render.onRender(world, x, y, z, renderer, stack, 0);
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
		return type == type.FIRST_PERSON_MAP ? false : true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return new BlockStack(item).getCastedBlock(IBlockRenderer.class).renderItemBlock(item.getItemDamage());
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		BlockStack stack = new BlockStack(item);
		IBlockRenderer render = stack.getCastedBlock(IBlockRenderer.class);
		if(render != null)
		{
			if(render.renderItemBlockBasic(item.getItemDamage()))
			{
				renderBlock(render, stack, (RenderBlocks)data[0]);
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
						render.onItemRendering(helper, type, stack, i, key[0], key[1], key[2], item, data[0]);
					}
					else
					{
						render.onItemRendering(helper, type, stack, i, key[0], key[1], key[2], item, data[0], data[1]);
					}
				}
			}
		}
	}
	
	public void renderBlock(IBlockRenderer par1, BlockStack par2, RenderBlocks par3)
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
		
		Tessellator tessellator = Tessellator.instance;
		Block block = par2.getBlock();
		block.setBlockBounds(size[0], size[1], size[2], size[3], size[4], size[5]);
		block.setBlockBoundsForItemRender();
		par3.setRenderBoundsFromBlock(block);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		par3.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par3.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, -1.0F);
		par3.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		par3.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		par3.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		tessellator.startDrawingQuads();
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
		par3.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, textureArray[0]);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static class BlockRendererHelper
	{
		public static void renderBlockStandart(IBlockRenderer par1, BlockStack par2, RenderBlocks par3)
		{
			BlockRendererSpmodCore.instance.renderBlock(par1, par2, par3);
		}
	}
	
}
