package speiger.src.tinymodularthings.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.spmodapi.client.render.core.ItemRenderSpmodCore;
import speiger.src.spmodapi.client.render.effects.SprinkleEffect;
import speiger.src.spmodapi.common.util.proxy.CodeProxy;
import speiger.src.tinymodularthings.client.render.carts.RenderTCarts;
import speiger.src.tinymodularthings.client.render.items.RendererItemCell;
import speiger.src.tinymodularthings.client.render.machine.RenderWaterSpender;
import speiger.src.tinymodularthings.client.render.storage.RenderStorageBlock;
import speiger.src.tinymodularthings.client.render.transport.RenderTransport;
import speiger.src.tinymodularthings.client.render.transport.renderTransportTile;
import speiger.src.tinymodularthings.common.blocks.machine.MachineWaterSpender;
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
import speiger.src.tinymodularthings.common.pipes.*;
import speiger.src.tinymodularthings.common.utils.nei.NeiRegistry;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeToolTipManager;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Loader;

public class TinyModularThingsClient extends TinyModularThingsCore
{
	public static int MultiID = RenderingRegistry.getNextAvailableRenderId();
	@Override
	public void registerRenderer()
	{
		
		// Storage Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(TinyTank.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyChest.class, new RenderStorageBlock());
		ClientRegistry.bindTileEntitySpecialRenderer(AdvTinyTank.class, new RenderStorageBlock());
		
		//Machine Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(MachineWaterSpender.class, new RenderWaterSpender());
		
		// Transport Blocks
		ClientRegistry.bindTileEntitySpecialRenderer(TinyHopper.class, new renderTransportTile());
		RenderingRegistry.registerBlockHandler(MultiID, new RenderTransport());
		
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
	
	@Override
	public void spawnParticle(World world, double x, double y, double z, float speed, float rotation)
	{
		try
		{
			for(int g = 0;g<3;g++)
			{
				int i = 1 + CodeProxy.getRandom().nextInt(10);
				float rSpeed = speed / i;
				SprinkleEffect effect = new SprinkleEffect(world, x, y, z);
				float dSpeed = rSpeed / 10;
				MathUtils.setEntityRotation(effect, rotation, dSpeed);
				Minecraft.getMinecraft().effectRenderer.addEffect(effect);
			}
		}
		catch(Exception e)
		{
		}
	}
	
}
