package speiger.src.spmodapi.client.core;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
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
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.spmodapi.common.util.TextureEngine;
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
		TextureEngine engine = TextureEngine.getTextures();
		engine.setCurrentMod(SpmodAPILib.ModID.toLowerCase());
		engine.setCurrentPath("core");
		engine.registerGuiTexture("SlotTexture", "GuiSlot");
		engine.registerGuiTexture("TankSlotTexture", "GuiTankSlot");
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
