package speiger.src.spmodapi.client.core;

import net.minecraftforge.client.MinecraftForgeClient;
import speiger.src.spmodapi.client.render.deko.ItemRendererLamp;
import speiger.src.spmodapi.client.render.deko.RenderLamp;
import speiger.src.spmodapi.client.render.utils.ItemRendererUtilsBlock;
import speiger.src.spmodapi.client.render.utils.RenderUtilsBlock;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.core.SpmodAPICore;
import speiger.src.spmodapi.common.util.ForgeRegister;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class SpmodAPIClient extends SpmodAPICore
{
	@Override
	public void clientSide()
	{
		ForgeRegister.regsiterClient();
		// Deko
		ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());
		MinecraftForgeClient.registerItemRenderer(APIBlocks.hempLamp.blockID, new ItemRendererLamp());
		
		// Utils
		RenderingRegistry.registerBlockHandler(RenderUtilsBlock.renderID, new RenderUtilsBlock());
		MinecraftForgeClient.registerItemRenderer(APIBlocks.blockUtils.blockID, new ItemRendererUtilsBlock());
	}
	
	@Override
	public int getArmorTypeFromName(String type)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(type);
	}
}
