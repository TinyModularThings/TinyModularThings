package speiger.src.spmodapi.client.render.core;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import speiger.src.api.client.render.IBlockRender;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.client.core.RenderHelper;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.FMLLog;

public class BlockRendererSpmodCore implements ISimpleBlockRenderingHandler
{	
	public static BlockRendererSpmodCore instance = new BlockRendererSpmodCore();
	static TextureEngine engine = TextureEngine.getTextures();
	static TileIconMaker maker = TileIconMaker.getIconMaker();

	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		BlockStack stack = new BlockStack(block, metadata);
		if(block != null)
		{
			IAdvTile tile = maker.getTileEntityFormBlockAndMetadata(block, metadata);
			
			if(tile != null)
			{
				tile.onRenderInv(stack, renderer);
			}
			else if(block instanceof IBlockRender)
			{
				IBlockRender render = (IBlockRender)block;
				render.onRenderInv(stack, renderer);
			}
		}
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		if(block != null)
		{
			IAdvTile tile = getAdvTile(world, x, y, z);
			if(tile != null)
			{
				tile.onRenderWorld(block, renderer);
			}
			else if(block instanceof IBlockRender)
			{
				IBlockRender render = (IBlockRender)block;
				render.onRenderWorld(world, x, y, z, renderer);
			}
		}
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return RenderHelper.getGlobalRenderID();
	}
	
	public IAdvTile getAdvTile(IBlockAccess par1, int x, int y, int z)
	{
		TileEntity tile = par1.getBlockTileEntity(x, y, z);
		if(tile == null || !(tile instanceof IAdvTile))
		{
			return null;
		}
		return (IAdvTile)tile;
	}
}
