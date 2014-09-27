package speiger.src.spmodapi.client.render.utils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderUtilsBlock implements ISimpleBlockRenderingHandler
{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	public static Icon pork;
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta != 3)
		{
			renderer.renderStandardBlock(block, x, y, z);
		}
		else
		{
			loadEntityCatcher(world, x, y, z, block, renderer);
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
		return renderID;
	}
	
	public void loadEntityCatcher(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks blocks)
	{
		
		blocks.setOverrideBlockTexture(blocks.getIconSafe(pork));
		blocks.renderCrossedSquares(block, x, y, z);
		blocks.clearOverrideBlockTexture();
		blocks.renderStandardBlock(block, x, y, z);
	}
	
}
