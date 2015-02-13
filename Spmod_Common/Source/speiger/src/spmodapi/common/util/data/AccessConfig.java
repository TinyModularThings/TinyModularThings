package speiger.src.spmodapi.common.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.Configuration;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.config.SpmodConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class AccessConfig
{
	Configuration config;
	static ArrayList<String> tileMaps = new ArrayList<String>();
	public static boolean everyOneCanUse = false;
	
	public AccessConfig(SpmodConfig par1)
	{
		config = par1.getConfiguration();
		loadConfig();
	}
	
	private void loadConfig()
	{
		everyOneCanUse = new ConfigBoolean(SpmodConfig.general, "Access Adding No OP", true).setComment("Sets that everyone can add TileEntity to the Inventory Access Terminal. Return false to make it only OP useage").getResult(config);
		String tiles = config.get(SpmodConfig.general, "Access Tiles", "", String.format("%s%n%s", "All Tiles that will be accepted by the Inventory Access Terminal", "Just add the simple names and cut it with ':'")).getString();
		String[] tileEntities = tiles.split(":");
		Map<String,Class> teMappings = ObfuscationReflectionHelper.getPrivateValue(TileEntity.class, null, "field_" + "70326_a", "nameToClassMap", "a");
		ArrayList<String> list = new ArrayList<String>();
		for(Class clz : teMappings.values())
		{
			
			if(clz != null)
			{
				list.add(clz.getSimpleName());
			}
		}
		tileMaps.addAll(list);
		for(String cu : tileEntities)
		{
			addTileEntity(cu);
		}
		config.save();
	}
	
	public static void addTileEntity(String tile)
	{
		if(tileMaps.contains(tile))
		{
			InventoryAccesser.addTileEntity(tile);
		}	
	}
	
	public static void removeTileEntity(String tile)
	{
		if(tileMaps.contains(tile))
		{
			InventoryAccesser.removeTileEntity(tile);
		}
	}
	
	public void loadLaterData()
	{
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList("TileEntityFurnace", "TileEntityDispenser", "TileEntityChest", "TileEntityEnderChest", "TileEntityDropper", 
		"TileEntityBrewingStand", "TileEntityEnchantmentTable", "TileEntityCommandBlock", "TileEntityBeacon", "TileEntityHopper",
		"ExpStorage", "MobMachine"));
		
		try
		{
			if(Loader.isModLoaded("TinyModularThings"))
			{
				list.addAll(Arrays.asList("TinyChest", "AdvTinyChest", "CraftingStation", "OreCrafter",
						"BucketFillerBasic", "PressureFurnace", "SelfPoweredBucketFiller", "TileEntityModifiedFurnace"));
			}
			
			if(Loader.isModLoaded("CompactWindmills"))
			{
				list.add("WindMill");
			}
			
			if(Loader.isModLoaded("BuildCraft|Core"))
			{
				list.addAll(Arrays.asList("TileBuffer", "TileEngineIron", "TileEngineStone", "TileHopper", "TileAutoWorkbench", "TileAdvancedCraftingTable"));
			}
			if(Loader.isModLoaded("Forestry"))
			{
				list.addAll(Arrays.asList("TileApiary", "TileBeehouse", "TileArboristChest", "EngineBronze", "EngineCopper", "TileApiaristChest",
						"EngineTin", "MachineGenerator", "TileWorktable", "MachineRaintank", "MachineCarpenter", "TileLepidopteristChest",
						"MachineMailbox", "MachineTrader"));
			}
			if(Loader.isModLoaded("IC2"))
			{
				list.addAll(Arrays.asList("TileEntityTradeOMat", "TileEntityEnergyOMat", "TileEntityPersonalChest", "TileEntityPump", "TileEntityIronFurnace"));
			}
			if(Loader.isModLoaded("AppliedEnergistics"))
			{
				list.addAll(Arrays.asList("TileChest", "TileCraftingMonitor", "TileCraftingTerminal", "TileGrinder"));
			}
			if(Loader.isModLoaded("Railcraft"))
			{
				list.addAll(Arrays.asList("TileDetector", "TileAnchorWorld", "TileFeedStation", "TileEngineSteamHobby"));
			}
		}
		catch(Exception e)
		{
		}
		
		for(String data : list)
		{
			addTileEntity(data);
		}
	}
}
