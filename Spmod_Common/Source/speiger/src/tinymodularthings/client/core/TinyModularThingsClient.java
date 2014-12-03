package speiger.src.tinymodularthings.client.core;

import net.minecraft.client.model.ModelMinecart;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.spmodapi.client.render.core.ItemRenderSpmodCore;
import speiger.src.tinymodularthings.client.render.carts.RenderTCarts;
import speiger.src.tinymodularthings.client.render.items.RendererItemCell;
import speiger.src.tinymodularthings.client.render.storage.RenderStorageBlock;
import speiger.src.tinymodularthings.client.render.transport.renderTransportTile;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyTank;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.core.TinyModularThingsCore;
import speiger.src.tinymodularthings.common.entity.minecarts.TCarts;
import speiger.src.tinymodularthings.common.items.minecarts.AdvTinyChestCart;
import speiger.src.tinymodularthings.common.items.minecarts.TinyChestCart;
import speiger.src.tinymodularthings.common.pipes.AluFluidExtractionPipe;
import speiger.src.tinymodularthings.common.pipes.FluidRegstonePipe;
import speiger.src.tinymodularthings.common.pipes.ItemRedstonePipe;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldExtractionPower;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldPower;
import speiger.src.tinymodularthings.common.pipes.RefinedDiamondPowerPipe;
import speiger.src.tinymodularthings.common.utils.nei.NeiRegistry;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeToolTipManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;

public class TinyModularThingsClient extends TinyModularThingsCore
{
	
	@Override
	public void registerRenderer()
	{
		
		// Storage Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyTank.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyTank.class, new RenderStorageBlock());

		// Transport Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyHopper.class, new renderTransportTile());
		
		// Entities
		RenderingRegistry.registerEntityRenderingHandler(TCarts.class, new RenderTCarts());
		
		//Items
		MinecraftForgeClient.registerItemRenderer(TinyItems.cell.itemID, new RendererItemCell());
		
		if(Loader.isModLoaded("NotEnoughItems"))
		{
			NeiRegistry.getInstance().init();
		}
		
		TinyChestCart.cart = new ModelMinecart();
		AdvTinyChestCart.cart = new ModelMinecart();
		
		if(Loader.isModLoaded("BuildCraft|Core"))
		{
			loadPipeToolTips();
		}
		
	}
	
	private void loadPipeToolTips()
	{
		PipeToolTipManager.addToolTip(PipeEmeraldExtractionPower.class, "Transferlimit: 2048");
		PipeToolTipManager.addToolTip(PipeEmeraldPower.class, "Transferlimit: 2048");
		PipeToolTipManager.addToolTip(FluidRegstonePipe.class, "Fluid Redstone Pipe. Emit if full");
		PipeToolTipManager.addToolTip(RefinedDiamondPowerPipe.class, "Powerloss free Pipe 512 MJ Transferlimit");
		PipeToolTipManager.addToolTip(ItemRedstonePipe.class, "Emits a Signal when Items go through it");
		PipeToolTipManager.addToolTip(AluFluidExtractionPipe.class, "Adjustable Fluid Extraction Pipe");
	}
	
	
	@Override
	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		try
		{
			Pipe pipe = (Pipe) par2.getConstructor(new Class[] { Integer.TYPE }).newInstance(new Object[] { Integer.valueOf(id) });			
			MinecraftForgeClient.registerItemRenderer(par1.itemID, ItemRenderSpmodCore.instance);
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
