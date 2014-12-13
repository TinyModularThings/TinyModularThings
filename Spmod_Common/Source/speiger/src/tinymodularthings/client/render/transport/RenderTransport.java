package speiger.src.tinymodularthings.client.render.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.client.core.TinyModularThingsClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderTransport implements ISimpleBlockRenderingHandler
{
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if (block != null)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if (meta == 0 || meta == 4)
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
		int meta = world.getBlockMetadata(x, y, z);
		SpmodBlockBase blocks = (SpmodBlockBase)block;
		blocks.setRenderPass(meta, 0);
		renderer.renderStandardBlock(block, x, y, z);
		blocks.setRenderPass(meta, 1);
		renderer.renderStandardBlock(block, x, y, z);		
		
		
	}
	
	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}
	
	@Override
	public int getRenderId()
	{
		return TinyModularThingsClient.MultiID;
	}
	
}
