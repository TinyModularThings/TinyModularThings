package speiger.src.spmodapi.client.render.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderUtilsBlock implements ISimpleBlockRenderingHandler
{
	public static int renderID = RenderingRegistry.getNextAvailableRenderId();
	
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
		Tessellator tes = Tessellator.instance;
		Minecraft mc = FMLClientHandler.instance().getClient();
		
		tes.draw();
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		tes.startDrawingQuads();
		tes.setNormal(1F, 1F, 0F);
		blocks.setOverrideBlockTexture(Item.porkRaw.getIconFromDamage(0));
		blocks.renderCrossedSquares(block, x, y, z);
		blocks.clearOverrideBlockTexture();
		tes.draw();
		tes.startDrawingQuads();
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		blocks.renderStandardBlock(block, x, y, z);
		
	}
	
}
