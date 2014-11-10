package speiger.src.spmodapi.client.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import speiger.src.api.client.render.IBlockRenderer;
import speiger.src.api.client.render.IMetaItemRender;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore;
import speiger.src.spmodapi.client.render.core.ItemRenderSpmodCore;
import speiger.src.spmodapi.client.render.deko.RenderHanfSign;
import speiger.src.spmodapi.client.render.deko.RenderKyroka;
import speiger.src.spmodapi.client.render.deko.RenderLamp;
import speiger.src.spmodapi.common.blocks.deko.KyrokaTheFox;
import speiger.src.spmodapi.common.blocks.deko.MultiPlate;
import speiger.src.spmodapi.common.blocks.deko.TileLamp;
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

	
		// Kyroka
		ClientRegistry.bindTileEntitySpecialRenderer(KyrokaTheFox.class, new RenderKyroka());
	
		RenderingRegistry.registerBlockHandler(new BlockRendererSpmodCore());
		
		for(int i = 0;i<Block.blocksList.length;i++)
		{
			Block block = Block.blocksList[i];
			if(block != null && block instanceof IBlockRenderer)
			{
				IBlockRenderer render = (IBlockRenderer)block;
				if(render.requiresRender())
				{
					MinecraftForgeClient.registerItemRenderer(block.blockID, BlockRendererSpmodCore.instance);
				}
			}
		}
		for(int i = 0;i<Item.itemsList.length;i++)
		{
			Item item = Item.itemsList[i];
			if(item != null && item instanceof IMetaItemRender)
			{
				IMetaItemRender render = (IMetaItemRender)item;
				if(render.doRender())
				{
					MinecraftForgeClient.registerItemRenderer(item.itemID, ItemRenderSpmodCore.instance);
				}
			}
		}
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
