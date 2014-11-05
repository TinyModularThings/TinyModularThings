package speiger.src.spmodapi.client.core;

import net.minecraftforge.client.MinecraftForgeClient;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore;
import speiger.src.spmodapi.client.render.deko.ItemRendererLamp;
import speiger.src.spmodapi.client.render.deko.ItemRendererStatue;
import speiger.src.spmodapi.client.render.deko.RenderHanfSign;
import speiger.src.spmodapi.client.render.deko.RenderKyroka;
import speiger.src.spmodapi.client.render.deko.RenderLamp;
import speiger.src.spmodapi.client.render.utils.ItemRendererUtilsBlock;
import speiger.src.spmodapi.client.render.utils.RenderUtilsBlock;
import speiger.src.spmodapi.common.blocks.deko.KyrokaTheFox;
import speiger.src.spmodapi.common.blocks.deko.MultiPlate;
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
		ClientRegistry.bindTileEntitySpecialRenderer(MultiPlate.class, new RenderHanfSign());
		MinecraftForgeClient.registerItemRenderer(APIBlocks.hempLamp.blockID, new ItemRendererLamp());
		
		// Utils
		RenderingRegistry.registerBlockHandler(RenderUtilsBlock.renderID, new RenderUtilsBlock());
		MinecraftForgeClient.registerItemRenderer(APIBlocks.blockUtils.blockID, new ItemRendererUtilsBlock());
	
		// Kyroka
		MinecraftForgeClient.registerItemRenderer(APIBlocks.statues.blockID, new ItemRendererStatue());
		ClientRegistry.bindTileEntitySpecialRenderer(KyrokaTheFox.class, new RenderKyroka());
	
		RenderingRegistry.registerBlockHandler(new BlockRendererSpmodCore());
		
	
	}
	
	@Override
	public int getArmorTypeFromName(String type)
	{
		return RenderingRegistry.addNewArmourRendererPrefix(type);
	}

	@Override
	public void onEngineLoad()
	{
	}
	
	
}
