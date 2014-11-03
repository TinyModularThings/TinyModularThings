package speiger.src.spmodapi.client.render.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
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
	
	public void loadEntityCatcher(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks render)
	{
		Tessellator tes = Tessellator.instance;
		Minecraft mc = FMLClientHandler.instance().getClient();
		tes.draw();
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		tes.startDrawingQuads();
		tes.setNormal(1F, 1F, 0F);
		render.setOverrideBlockTexture(Item.porkRaw.getIconFromDamage(0));
		render.renderCrossedSquares(block, x, y, z);
		render.clearOverrideBlockTexture();
		tes.draw();
		tes.startDrawingQuads();
		mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		render.renderStandardBlock(block, x, y, z);
		
	}
	
}
