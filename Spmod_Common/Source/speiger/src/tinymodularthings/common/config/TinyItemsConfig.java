package speiger.src.tinymodularthings.common.config;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import speiger.src.spmodapi.common.util.proxy.PathProxy;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.ItemIngots;
import speiger.src.tinymodularthings.common.items.ItemTinyItem;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemAdvTinyChest;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemTinyChest;
import speiger.src.tinymodularthings.common.items.itemblocks.storage.ItemTinyTank;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemTinyHopper;
import speiger.src.tinymodularthings.common.items.minecarts.AdvTinyChestCart;
import speiger.src.tinymodularthings.common.items.minecarts.TinyChestCart;
import speiger.src.tinymodularthings.common.items.study.ItemInformationBook;
import speiger.src.tinymodularthings.common.items.tools.ItemTinyInfo;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldExtractionPower;
import speiger.src.tinymodularthings.common.pipes.PipeEmeraldPower;
import speiger.src.tinymodularthings.common.pipes.SpmodPipe;
import speiger.src.tinymodularthings.common.utils.TinyTextureHelper;
import buildcraft.BuildCraftTransport;
import buildcraft.api.recipes.AssemblyRecipe;
import buildcraft.transport.BlockGenericPipe;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportPower;
import buildcraft.transport.TransportProxyClient;
import cpw.mods.fml.common.FMLLog;

public class TinyItemsConfig
{
	private static TinyItems items;
	private static TinyConfig config;
	
	public static void initItems()
	{
		items.ingots = new ItemIngots(config.item.getCurrentID());
		RegisterItem(items.ingots, "Ingots");
		config.item.updateToNextID();
		
		items.IridiumDrop = new ItemTinyItem(config.item.getCurrentID(), "Unrefined_Iridium", TinyTextureHelper.getTextureStringFromName("ingots/UnrefinedIridium"));
		RegisterItem(items.IridiumDrop, "IridiumDrop");
		config.item.updateToNextID();
		
		items.bauxitDust = new ItemTinyItem(config.item.getCurrentID(), "bauxit_dust", TinyTextureHelper.getTextureStringFromName("dusts/BauxitDust")).setUnlocalizedName("BauxiteDust");
		RegisterItem(items.bauxitDust, "BaxitDust");
		config.item.updateToNextID();
		
		items.informationBook = new ItemInformationBook(config.item.getCurrentID());
		RegisterItem(items.informationBook, "ForschungsInfoBuch");
		config.item.updateToNextID();
		
		items.tinyChest = new ItemTinyChest(config.item.getCurrentID());
		RegisterItem(items.tinyChest, "TinyChests");
		config.item.updateToNextID();
		
		items.tinyTank = new ItemTinyTank(config.item.getCurrentID());
		RegisterItem(items.tinyTank, "TinyTanks");
		config.item.updateToNextID();
		
		items.tinyStorageCart = new TinyChestCart(TinyConfig.item.getCurrentID());
		RegisterItem(items.tinyStorageCart, "TinyChestCart");
		config.item.updateToNextID();
		
		items.advTinyChest = new ItemAdvTinyChest(TinyConfig.item.getCurrentID());
		RegisterItem(items.advTinyChest, "AdvTinyChests");
		config.item.updateToNextID();
		
		items.advTinyStorageCart = new AdvTinyChestCart(TinyConfig.item.getCurrentID());
		RegisterItem(items.advTinyStorageCart, "AdvTinyChestCart");
		config.item.updateToNextID();
		
		items.playerGame = new ItemTinyInfo(TinyConfig.item.getCurrentID());
		RegisterItem(items.playerGame, "PlayerChanger");
		config.item.updateToNextID();
		
		items.interfaceBlock = new ItemInterfaceBlock(TinyConfig.item.getCurrentID());
		RegisterItem(items.interfaceBlock, "InterfaceBlock");
		config.item.updateToNextID();
		
		TinyItems.tinyHopper = new ItemTinyHopper(config.item.getCurrentID());
		RegisterItem(items.tinyHopper, "TinyHopper");
		config.item.updateToNextID();
		
	}
	
	public static void RegisterItem(Item par1, String name)
	{
		RegisterProxy.RegisterItem(TinyModularThingsLib.ModID, name, par1);
	}
	
	
	public static void onPipeLoad()
	{
		try
		{
			PipeTransportPower.powerCapacities.put(PipeEmeraldExtractionPower.class, 2048);
			items.emeraldPowerPipeE = BuildItem(config.pipes.getCurrentID(), PipeEmeraldExtractionPower.class, "pipe.emerald.power.extraction");
			config.pipes.updateToNextID();
			if(items.emeraldPowerPipeE != null)
			{
				AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[]{new ItemStack(BuildCraftTransport.pipeItemsEmerald, 8), new ItemStack(Item.redstone, 8)}, 10000, new ItemStack(items.emeraldPowerPipeE, 8)));
				PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[]{items.emeraldPowerPipeE});
			}
			PipeTransportPower.powerCapacities.put(PipeEmeraldPower.class, 2048);
			items.emeraldPowerPipe = BuildItem(config.pipes.getCurrentID(), PipeEmeraldPower.class, "pipe.emerald.power");
			PathProxy.addSRecipe(new ItemStack(items.emeraldPowerPipe), new Object[]{BuildCraftTransport.pipeItemsEmerald, Item.redstone});
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[]{items.emeraldPowerPipe});
		}
		catch (Exception e)
		{
		}
	}
	
	public static Item BuildItem(int defaultID, Class<? extends Pipe> clas, String descr)
	{
		try
		{
			ItemPipe res = new SpmodPipe(defaultID, descr);
			RegisterProxy.RegisterItem(TinyModularThingsLib.ModID, res);
			if(res != null)
			{
				BlockGenericPipe.pipes.put(res.itemID, clas);
				TinyModularThings.core.loadPipe(res, res.itemID, clas);
				MinecraftForgeClient.registerItemRenderer(res.itemID, ((TransportProxyClient)TransportProxyClient.proxy).pipeItemRenderer);
			}
			
			return res;
		}
		catch (Exception e)
		{
			for(int i = 0;i<e.getStackTrace().length;i++)
			{
				FMLLog.getLogger().info(""+e.getStackTrace()[i]);
			}
			return null;
		}
	}
}
