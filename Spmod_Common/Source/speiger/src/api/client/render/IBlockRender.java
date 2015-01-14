package speiger.src.api.client.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.common.world.blocks.BlockStack;

public interface IBlockRender
{
	public boolean requiresRender();
	
	public void onRenderInv(BlockStack par1, RenderBlocks render);
	
	public void onRenderWorld(IBlockAccess world, int x, int y, int z, RenderBlocks renderer);
}
