package speiger.src.spmodapi.common.util.data;

import ic2.core.block.machine.tileentity.TileEntityIronFurnace;
import ic2.core.block.machine.tileentity.TileEntityPump;
import ic2.core.block.personal.TileEntityEnergyOMat;
import ic2.core.block.personal.TileEntityPersonalChest;
import ic2.core.block.personal.TileEntityTradeOMat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import mods.railcraft.common.blocks.detector.TileDetector;
import mods.railcraft.common.blocks.machine.alpha.TileAnchorWorld;
import mods.railcraft.common.blocks.machine.alpha.TileFeedStation;
import mods.railcraft.common.blocks.machine.beta.TileEngineSteamHobby;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.Configuration;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.spmodapi.common.blocks.utils.ExpStorage;
import speiger.src.spmodapi.common.blocks.utils.InventoryAccesser;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.tinymodularthings.common.blocks.crafting.CraftingStation;
import speiger.src.tinymodularthings.common.blocks.crafting.OreCrafter;
import speiger.src.tinymodularthings.common.blocks.machine.BucketFillerBasic;
import speiger.src.tinymodularthings.common.blocks.machine.PressureFurnace;
import speiger.src.tinymodularthings.common.blocks.machine.SelfPoweredBucketFiller;
import speiger.src.tinymodularthings.common.blocks.storage.AdvTinyChest;
import speiger.src.tinymodularthings.common.blocks.storage.TinyChest;
import speiger.src.tinymodularthings.common.plugins.BC.TileEntityModifiedFurnace;
import appeng.me.tile.TileChest;
import appeng.me.tile.TileCraftingMonitor;
import appeng.me.tile.TileCraftingTerminal;
import appeng.tech1.tile.TileGrinder;
import buildcraft.core.TileBuffer;
import buildcraft.energy.TileEngineIron;
import buildcraft.energy.TileEngineStone;
import buildcraft.factory.TileAutoWorkbench;
import buildcraft.factory.TileHopper;
import buildcraft.silicon.TileAdvancedCraftingTable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import forestry.apiculture.gadgets.TileApiaristChest;
import forestry.apiculture.gadgets.TileApiary;
import forestry.apiculture.gadgets.TileBeehouse;
import forestry.arboriculture.gadgets.TileArboristChest;
import forestry.energy.gadgets.EngineBronze;
import forestry.energy.gadgets.EngineCopper;
import forestry.energy.gadgets.EngineTin;
import forestry.energy.gadgets.MachineGenerator;
import forestry.factory.gadgets.MachineCarpenter;
import forestry.factory.gadgets.MachineRaintank;
import forestry.factory.gadgets.TileWorktable;
import forestry.lepidopterology.gadgets.TileLepidopteristChest;
import forestry.mail.gadgets.MachineMailbox;
import forestry.mail.gadgets.MachineTrader;

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
				list.addAll(Arrays.asList(TileEntityFurnace.class.getSimpleName(), TileEntityDispenser.class.getSimpleName(),
				TileEntityChest.class.getSimpleName(), TileEntityEnderChest.class.getSimpleName(), TileEntityDropper.class.getSimpleName(), 
				TileEntityBrewingStand.class.getSimpleName(), TileEntityEnchantmentTable.class.getSimpleName(), 
				TileEntityCommandBlock.class.getSimpleName(), TileEntityBeacon.class.getSimpleName(), TileEntityHopper.class.getSimpleName(),
				ExpStorage.class.getSimpleName(), MobMachine.class.getSimpleName()));
		
		try
		{
			if(Loader.isModLoaded("TinyModularThings"))
			{
				list.addAll(Arrays.asList(TinyChest.class.getSimpleName(), AdvTinyChest.class.getSimpleName(), CraftingStation.class.getSimpleName(), OreCrafter.class.getSimpleName(),
						BucketFillerBasic.class.getSimpleName(), PressureFurnace.class.getSimpleName(), SelfPoweredBucketFiller.class.getSimpleName(), TileEntityModifiedFurnace.class.getSimpleName()));

			}
			
			if(Loader.isModLoaded("CompactWindmills"))
			{
				list.add(WindMill.class.getSimpleName());
			}
			
			if(Loader.isModLoaded("BuildCraft|Core"))
			{
				list.addAll(Arrays.asList(TileBuffer.class.getSimpleName(), TileEngineIron.class.getSimpleName(), TileEngineStone.class.getSimpleName(), TileHopper.class.getSimpleName(),
						TileAutoWorkbench.class.getSimpleName(), TileAdvancedCraftingTable.class.getSimpleName()));
			}
			if(Loader.isModLoaded("Forestry"))
			{
				list.addAll(Arrays.asList(TileApiary.class.getSimpleName(), TileBeehouse.class.getSimpleName(), TileArboristChest.class.getSimpleName(), EngineBronze.class.getSimpleName(), EngineCopper.class.getSimpleName(), TileApiaristChest.class.getSimpleName(),
						EngineTin.class.getSimpleName(), MachineGenerator.class.getSimpleName(), TileWorktable.class.getSimpleName(), MachineRaintank.class.getSimpleName(), MachineCarpenter.class.getSimpleName(), TileLepidopteristChest.class.getSimpleName(),
						MachineMailbox.class.getSimpleName(), MachineTrader.class.getSimpleName()));
			}
			if(Loader.isModLoaded("IC2"))
			{
				list.addAll(Arrays.asList(TileEntityTradeOMat.class.getSimpleName(), TileEntityEnergyOMat.class.getSimpleName(), TileEntityPersonalChest.class.getSimpleName(),
						TileEntityPump.class.getSimpleName(), TileEntityIronFurnace.class.getSimpleName()));
			}
			if(Loader.isModLoaded("AppliedEnergistics"))
			{
				list.addAll(Arrays.asList(TileChest.class.getSimpleName(), TileCraftingMonitor.class.getSimpleName(), TileCraftingTerminal.class.getSimpleName(), TileGrinder.class.getSimpleName()));
			}
			if(Loader.isModLoaded("Railcraft"))
			{
				list.addAll(Arrays.asList(TileDetector.class.getSimpleName(), TileAnchorWorld.class.getSimpleName(), TileFeedStation.class.getSimpleName(), TileEngineSteamHobby.class.getSimpleName()));
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
