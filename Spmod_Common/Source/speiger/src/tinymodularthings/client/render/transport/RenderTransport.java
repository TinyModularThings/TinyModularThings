package speiger.src.tinymodularthings.client.render.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.inventory.IAcceptor;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTransport implements ISimpleBlockRenderingHandler
{
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		if (block != null && metadata == 0)
		{
			renderer.renderBlockAsItem(block, metadata, 0);
		}
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block != null)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if (meta == 0)
			{
				handleEnderChest(world, x, y, z, block, modelId, renderer);
			}
			else
			{
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if (tile != null && tile instanceof IAcceptor)
				{
					AdvTile adv = (AdvTile) tile;
					renderAcceptor(adv, world, x, y, z, block, renderer);
					
				}
			}
		}
		return false;
	}
	
	public void handleEnderChest(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(block, x, y, z);
	}
	
	public void renderAcceptor(AdvTile tile, IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer)
	{
		renderer.renderStandardBlock(block, x, y, z);
		int meta = world.getBlockMetadata(x, y, z) - 1;
		renderer.setOverrideBlockTexture(renderer.getIconSafe(tile.getIconFromSideAndMetadata(0, 1)));
		renderer.renderStandardBlock(block, x, y, z);
		renderer.clearOverrideBlockTexture();
	}
	
	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}
	
	@Override
	public int getRenderId()
	{
		return 0;
	}
	
}
