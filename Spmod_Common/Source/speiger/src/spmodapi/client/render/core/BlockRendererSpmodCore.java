package speiger.src.spmodapi.client.render.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.client.render.IBlockRenderer;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.core.RenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererSpmodCore implements ISimpleBlockRenderingHandler
{

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
		return RenderHelper.GlobalRenderer;
	}
	
}
