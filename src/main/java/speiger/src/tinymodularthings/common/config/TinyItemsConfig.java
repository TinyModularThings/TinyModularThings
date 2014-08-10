package speiger.src.tinymodularthings.common.config;

import net.minecraft.init.Items;
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
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
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
		items.ingots = new ItemIngots();
		RegisterItem(items.ingots, "Ingots");
		
		
		items.IridiumDrop = new ItemTinyItem("Unrefined_Iridium", TinyTextureHelper.getTextureStringFromName("ingots/UnrefinedIridium"));
		RegisterItem(items.IridiumDrop, "IridiumDrop");
		
		
		items.bauxitDust = new ItemTinyItem("bauxit_dust", TinyTextureHelper.getTextureStringFromName("dusts/BauxitDust")).setUnlocalizedName("BauxiteDust");
		RegisterItem(items.bauxitDust, "BaxitDust");
		
		
		items.informationBook = new ItemInformationBook();
		RegisterItem(items.informationBook, "ForschungsInfoBuch");
		
		
		items.tinyChest = new ItemTinyChest();
		RegisterItem(items.tinyChest, "TinyChests");
		
		
		items.tinyTank = new ItemTinyTank();
		RegisterItem(items.tinyTank, "TinyTanks");
		
		
		items.tinyStorageCart = new TinyChestCart();
		RegisterItem(items.tinyStorageCart, "TinyChestCart");
		
		
		items.advTinyChest = new ItemAdvTinyChest();
		RegisterItem(items.advTinyChest, "AdvTinyChests");
		
		
		items.advTinyStorageCart = new AdvTinyChestCart();
		RegisterItem(items.advTinyStorageCart, "AdvTinyChestCart");
		
		
		items.playerGame = new ItemTinyInfo();
		RegisterItem(items.playerGame, "PlayerChanger");
		
		
		items.interfaceBlock = new ItemInterfaceBlock();
		RegisterItem(items.interfaceBlock, "InterfaceBlock");
		
		
		TinyItems.tinyHopper = new ItemTinyHopper();
		RegisterItem(items.tinyHopper, "TinyHopper");
		
		
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
			items.emeraldPowerPipeE = BuildItem(PipeEmeraldExtractionPower.class, "pipe.emerald.power.extraction");
	
			if(items.emeraldPowerPipeE != null)
			{
				BuildcraftRecipeRegistry.assemblyTable.addRecipe("Emerald Extraction Pipe", 10000, new ItemStack(items.emeraldPowerPipeE, 8), new ItemStack(BuildCraftTransport.pipeItemsEmerald, 8), new ItemStack(Items.redstone, 8));
				PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[]{items.emeraldPowerPipeE});
			}
			PipeTransportPower.powerCapacities.put(PipeEmeraldPower.class, 2048);
			items.emeraldPowerPipe = BuildItem(PipeEmeraldPower.class, "pipe.emerald.power");
			PathProxy.addSRecipe(new ItemStack(items.emeraldPowerPipe), new Object[]{BuildCraftTransport.pipeItemsEmerald, Items.redstone});
			PathProxy.addSRecipe(new ItemStack(BuildCraftTransport.pipeItemsEmerald), new Object[]{items.emeraldPowerPipe});
		}
		catch (Exception e)
		{
		}
	}
	
	public static Item BuildItem(Class<? extends Pipe> clas, String descr)
	{
		try
		{
			ItemPipe res = new SpmodPipe(descr);
			RegisterProxy.RegisterItem(TinyModularThingsLib.ModID, res);
			if(res != null)
			{
				BlockGenericPipe.pipes.put(res, clas);
				TinyModularThings.core.loadPipe(res, clas);
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
