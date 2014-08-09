package speiger.src.tinymodularthings.client.core;

import java.util.Iterator;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.render.carts.CartItemRenderer;
import speiger.src.tinymodularthings.client.render.carts.RenderTCarts;
import speiger.src.tinymodularthings.client.render.pipes.ItemRendererPipe;
import speiger.src.tinymodularthings.client.render.pipes.RenderPipe;
import speiger.src.tinymodularthings.client.render.storage.ItemRendererStorageBlock;
import speiger.src.tinymodularthings.client.render.storage.RenderStorage;
import speiger.src.tinymodularthings.client.render.storage.RenderStorageBlock;
import speiger.src.tinymodularthings.client.render.transport.ItemRenderTransportTile;
import speiger.src.tinymodularthings.client.render.transport.RenderTransport;
import speiger.src.tinymodularthings.client.render.transport.renderTransportTile;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.core.TinyModularThingsCore;
import speiger.src.tinymodularthings.common.entity.minecarts.TCarts;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.utils.nei.NeiRegistry;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class TinyModularThingsClient extends TinyModularThingsCore
{
	
	@Override
	public void registerRenderer()
	{
		registerLanguage();
		
		// Pipes
		RenderingRegistry.registerBlockHandler(EnumIDs.Pipe.getId(), new RenderPipe());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.smallPipe), new ItemRendererPipe(TinyBlocks.smallPipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.normalPipe), new ItemRendererPipe(TinyBlocks.normalPipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.mediumPipe), new ItemRendererPipe(TinyBlocks.mediumPipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.bigPipe), new ItemRendererPipe(TinyBlocks.bigPipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.biggerPipe), new ItemRendererPipe(TinyBlocks.biggerPipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.largePipe), new ItemRendererPipe(TinyBlocks.largePipe));
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.hugePipe), new ItemRendererPipe(TinyBlocks.hugePipe));
		
		// Storage Blocks
		RenderingRegistry.registerBlockHandler(EnumIDs.StorageBlock.getId(), new RenderStorage());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyTank.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyChest.class, new RenderStorageBlock());
		
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyChest, new ItemRendererStorageBlock());
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyTank, new ItemRendererStorageBlock());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyChest, new ItemRendererStorageBlock());
		
		// Transport Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyHopper.class, new renderTransportTile());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TinyBlocks.transportBlock), new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.interfaceBlock, new ItemRenderTransportTile());
		RenderingRegistry.registerBlockHandler(EnumIDs.TransportBlock.getId(), new RenderTransport());
		
		
		
		// Entities
		RenderingRegistry.registerEntityRenderingHandler(TCarts.class, new RenderTCarts());
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyStorageCart, new CartItemRenderer(false));
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyStorageCart, new CartItemRenderer(true));
		
		NeiRegistry.getInstance().init();
		
	}
	
	private void registerLanguage()
	{
		
		if (TinyModularThings.LanguagePrint)
		{
			Iterator items = Item.itemRegistry.iterator();
			while(items.hasNext())
			{
				Item cu = (Item) items.next();
				if (cu != null && cu instanceof LanguageItem)
				{
					LanguageItem item = (LanguageItem) cu;
					item.registerItems(cu, TinyModularThings.instance);
					item.registerItems(cu, SpmodAPI.instance);
				}
			}
			LanguageRegister.printModLanguage(TinyModularThings.instance);
			LanguageRegister.printModLanguage(SpmodAPI.instance);
			
		}
	}
	
	@Override
	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		try
		{
			Pipe pipe = (Pipe)par2.getConstructor(new Class[]{Integer.TYPE}).newInstance(new Object[]{Integer.valueOf(id)});
			if(pipe != null)
			{
				par1.setPipesIcons(pipe.getIconProvider());
				par1.setPipeIconIndex(pipe.getIconIndex(ForgeDirection.VALID_DIRECTIONS[0]));
			}
		}
		catch (Exception e)
		{
		
		}
	}
	
}
