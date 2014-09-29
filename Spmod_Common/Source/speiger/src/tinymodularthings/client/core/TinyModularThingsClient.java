package speiger.src.tinymodularthings.client.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.items.LanguageItem;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.render.carts.CartItemRenderer;
import speiger.src.tinymodularthings.client.render.carts.RenderTCarts;
import speiger.src.tinymodularthings.client.render.items.RendererItemCell;
import speiger.src.tinymodularthings.client.render.pipes.ItemRendererBCPipe;
import speiger.src.tinymodularthings.client.render.pipes.ItemRendererPipe;
import speiger.src.tinymodularthings.client.render.pipes.RenderPipe;
import speiger.src.tinymodularthings.client.render.storage.ItemRendererStorageBlock;
import speiger.src.tinymodularthings.client.render.storage.RenderStorage;
import speiger.src.tinymodularthings.client.render.storage.RenderStorageBlock;
import speiger.src.tinymodularthings.client.render.transport.ItemRenderTransportTile;
import speiger.src.tinymodularthings.client.render.transport.RenderTransport;
import speiger.src.tinymodularthings.client.render.transport.renderTransportTile;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyTank;
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
		
		// Pipes
		RenderingRegistry.registerBlockHandler(EnumIDs.Pipe.getId(), new RenderPipe());
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.smallPipe.blockID, new ItemRendererPipe(TinyBlocks.smallPipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.normalPipe.blockID, new ItemRendererPipe(TinyBlocks.normalPipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.mediumPipe.blockID, new ItemRendererPipe(TinyBlocks.mediumPipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.bigPipe.blockID, new ItemRendererPipe(TinyBlocks.bigPipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.biggerPipe.blockID, new ItemRendererPipe(TinyBlocks.biggerPipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.largePipe.blockID, new ItemRendererPipe(TinyBlocks.largePipe));
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.hugePipe.blockID, new ItemRendererPipe(TinyBlocks.hugePipe));
		
		// Storage Blocks
		RenderingRegistry.registerBlockHandler(EnumIDs.StorageBlock.getId(), new RenderStorage());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyTank.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyTank.class, new RenderStorageBlock());
		
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyChest.itemID, new ItemRendererStorageBlock());
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyTank.itemID, new ItemRendererStorageBlock());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyChest.itemID, new ItemRendererStorageBlock());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyTank.itemID, new ItemRendererStorageBlock());
		
		// Transport Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyHopper.class, new renderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyBlocks.transportBlock.blockID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.interfaceBlock.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyHopper.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyHopper.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.fluidHopper.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advFluidHopper.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.energyHopper.itemID, new ItemRenderTransportTile());
		MinecraftForgeClient.registerItemRenderer(TinyItems.advEnergyHopper.itemID, new ItemRenderTransportTile());
		RenderingRegistry.registerBlockHandler(EnumIDs.TransportBlock.getId(), new RenderTransport());
		
		// Entities
		RenderingRegistry.registerEntityRenderingHandler(TCarts.class, new RenderTCarts());
		MinecraftForgeClient.registerItemRenderer(TinyItems.tinyStorageCart.itemID, new CartItemRenderer(false));
		MinecraftForgeClient.registerItemRenderer(TinyItems.advTinyStorageCart.itemID, new CartItemRenderer(true));
		
		//Items
		MinecraftForgeClient.registerItemRenderer(TinyItems.cell.itemID, new RendererItemCell());
		
		NeiRegistry.getInstance().init();
		
		registerLanguage();
		
	}
	
	private void registerLanguage()
	{
		if (TinyModularThings.LanguagePrint)
		{
			Item[] items = Item.itemsList;
			for (Item cu : items)
			{
				if (cu != null && cu instanceof LanguageItem)
				{
					LanguageItem item = (LanguageItem) cu;
					for (SpmodMod mod : SpmodModRegistry.getRegisteredMods())
					{
						item.registerItems(cu.itemID, mod);
					}
				}
			}
			for (SpmodMod mod : SpmodModRegistry.getRegisteredMods())
			{
				LanguageRegister.printModLanguage(mod);
			}
			
		}
	}
	
	@Override
	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		try
		{
			Pipe pipe = (Pipe) par2.getConstructor(new Class[] { Integer.TYPE }).newInstance(new Object[] { Integer.valueOf(id) });
			MinecraftForgeClient.registerItemRenderer(par1.itemID, ItemRendererBCPipe.pipe);
			
			if (pipe != null)
			{
				par1.setPipesIcons(pipe.getIconProvider());
				par1.setPipeIconIndex(pipe.getIconIndex(ForgeDirection.UNKNOWN));
				
			}
		}
		catch (Exception e)
		{
			
		}
	}
	
}
