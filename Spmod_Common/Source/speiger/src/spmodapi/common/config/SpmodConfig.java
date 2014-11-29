package speiger.src.spmodapi.common.config;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.api.common.utils.config.IConfigHelper;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.configType.ConfigBlock;
import speiger.src.spmodapi.common.config.configType.ConfigItem;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.world.SpmodWorldGen;

public class SpmodConfig implements IConfigHelper
{
	public static Configuration config;
	
	public static final String general = "General";
	public static final String items = "Items";
	public static final String blocks = "Blocks";
	public static final String world = "World";
	public static final String retro = "World" + config.CATEGORY_SPLITTER + "RetroGen";
	
	public static int[] ticks = new int[0];
	public static HashMap<String, Integer> integerInfos = new HashMap<String, Integer>();
	public static HashMap<String, Boolean> booleanInfos = new HashMap<String, Boolean>();
	
	public static ConfigBlock blockIDs;
	public static ConfigItem itemIDs;
	
	private static SpmodConfig init = new SpmodConfig();
	
	public static SpmodConfig getInstance()
	{
		return init;
	}
	
	public void loadSpmodCondig(File file)
	{
		config = new Configuration(file);
		
		try
		{
			SpmodAPI.log.print("Start Loading Config");
			booleanInfos.put("Logging", getBoolean(general, "Active Log", true).setComment(String.format("%s%n%s", "This Config disable every Log in this Mod", "Also the Crashreport!")).getResult(config));
			if (!booleanInfos.get("Logging"))
			{
				System.out.println("[SpmodAPI] Disabeling Log. This is the last i am doing which does not come from Forge/FML)");
				SpmodAPI.log.disable();
			}
			
			booleanInfos.put("Retrogen", getBoolean(retro, "Retrogen", false).setComment(String.format("%s%n%s", "Set to true to activate the Retrogen", "Do not forget to set the ores you want to regenerate to true")).getResult(config));
			booleanInfos.put("PlaySounds", getBoolean(general, "Play Sounds", true).setComment("Dissable all Sounds in my Mods").getResult(config));
			booleanInfos.put("LoadTileEntities", getBoolean(general, "LoadTileEntites", true).setComment("If you get crashes with my Blocks then just set this to false and load the game. My Block are frozen and do nothing in this time").getResult(config));
			booleanInfos.put("MobMachineEggs", getBoolean(general, "MobMachine Spawn Eggs", true).setComment("Enable that MobMachine drops Spawneggs").getResult(config));
			booleanInfos.put("ForestrySeedOil", getBoolean(general, "Forestry Seed Oil in Fermenter", true).setComment("Enable the usage of SeedOil in a fermenter").getResult(config));
			
			blockIDs = new ConfigBlock(ConfigBlock.getConfig(config, blocks, 950));
			itemIDs = new ConfigItem(ConfigItem.getConfig(config, items, 15000));
			
			Property tick = config.get(general, "Round Roubin Speed", "1:2:5:10:20:50", "Every number will be math as number * 5 ticks and please write it as the default with a : ");
			String[] result = tick.getString().split(":");
			ticks = new int[result.length];
			for (int i = 0; i < result.length; i++)
			{
				ticks[i] = Integer.parseInt(result[i]);
			}
			integerInfos.put("SavingDelay", Integer.parseInt(config.get(general, "Structure Storage Backup Delay", 24000, String.format("%s%n%s%n%s%n%s", "This delay says how long the game waits until he make a backup.", "This is required to save all Multistructures", "To prevent that on game/Server Crash that all structure data get lost.", "This is required to let the Multistructure interface be work when the game restarts")).getString()));
			integerInfos.put("AnimalChunkLoaderRange", Integer.parseInt(config.get(general, "Animal Chunkloader Working Range", 2, "You can choose how big the Working range of the Animal Chunkloader is. It is a Radius and multibled by 10. Max is 3. Min is 1").getString()));
			integerInfos.put("RealisticChunkloaderRange", Integer.parseInt(config.get(general, "EntityChunkloader Range", 2, "Same As in animalChunkloader but the max range is 9").getString()));
			this.handleNumber(1, 3, "AnimalChunkLoaderRange");
			this.handleNumber(1, 9, "RealisticChunkloaderRange");
			
			
			TextureEngine.getTextures().setCurrentMod(SpmodAPILib.ModID.toLowerCase());
			SpmodAPI.log.print("Load Utils");
			APIUtilsConfig.register();
			SpmodAPI.log.print("Utils Loaded");
			SpmodAPI.log.print("Load Blocks");
			APIBlocksConfig.loadBlocks();
			SpmodAPI.log.print("Blocks Loaded");
			SpmodAPI.log.print("Load Items");
			APIItemsConfig.loadItems();
			SpmodAPI.log.print("Items Loaded");
			SpmodAPI.log.print("Register WorldGen");
			SpmodWorldGen.getWorldGen().init(this);
			SpmodAPI.log.print("WorldGen Registered");
			TextureEngine.getTextures().removePath();
			TextureEngine.getTextures().finishMod();
			
			blockIDs.setEnd(config, blocks.toLowerCase());
			itemIDs.setEnd(config, items.toLowerCase());
			
		}
		catch (Exception e)
		{
			SpmodAPI.log.print("Config could not load. Reason: " + e.getLocalizedMessage());
			String i = "";
			for (StackTraceElement el : e.getStackTrace())
			{
				i = String.format("%s%n%s", i, el);
			}
			SpmodAPI.log.print("Error Log: " + i);
		}
		finally
		{
			config.save();
		}
		
	}
	
	public static ConfigBoolean getBoolean(String categorie, String name, boolean defaults)
	{
		ConfigBoolean result = new ConfigBoolean(categorie, name, defaults);
		return result;
	}
	
	@Override
	public String getWorldGenCategorie()
	{
		return world;
	}
	
	@Override
	public String getRetrogenCategorie()
	{
		return retro;
	}
	
	@Override
	public Configuration getConfiguration()
	{
		return config;
	}
	
	@Override
	public ConfigBoolean getConfigBoolean(String categorie, String name, boolean defaults)
	{
		ConfigBoolean result = new ConfigBoolean(categorie, name, defaults);
		return result;
	}
	
	private void handleNumber(int min, int max, String id)
	{
		int ints = integerInfos.get(id);
		if(ints < min)
		{
			integerInfos.put(id, min);
		}
		if(ints > max)
		{
			integerInfos.put(id, max);
		}
	}
	
}
