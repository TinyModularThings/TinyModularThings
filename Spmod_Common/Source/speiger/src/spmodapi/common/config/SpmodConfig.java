package speiger.src.spmodapi.common.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import speiger.src.api.util.config.ConfigBoolean;
import speiger.src.api.util.config.IConfigHelper;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.configType.ConfigBlock;
import speiger.src.spmodapi.common.config.configType.ConfigItem;
import speiger.src.spmodapi.common.world.SpmodWorldGen;

public class SpmodConfig implements IConfigHelper
{
	public static Configuration config;
	
	public static final String general = "General";
	public static final String items = "Items";
	public static final String blocks = "Blocks";
	public static final String world = "World";
	public static final String retro = "World" + config.CATEGORY_SPLITTER + "RetroGen";
	
	public static boolean logging = true;
	public static boolean retogen = false;
	public static boolean playSounds = true;
	public static boolean loadTiles = true;
	public static int[] ticks = new int[0];
	public static int savingDelay = 24000;
	
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
			
			logging = getBoolean(general, "Active Log", true).setComment(String.format("%s%n%s", "This Config disable every Log in this Mod", "Also the Crashreport!")).getResult(config);
			
			if (!logging)
			{
				System.out.println("[SpmodAPI] Disabeling Log. This is the last i am doing which does not come from Forge/FML)");
				SpmodAPI.log.disable();
			}
			
			retogen = getBoolean(retro, "Retrogen", false).setComment(String.format("%s%n%s", "Set to true to activate the Retrogen", "Do not forget to set the ores you want to regenerate to true")).getResult(config);
			
			playSounds = getBoolean(general, "Play Sounds", true).setComment("Dissable all Sounds in my Mods").getResult(config);
			
			loadTiles = getBoolean(general, "LoadTileEntites", true).setComment("If you get crashes with my Blocks then just set this to false and load the game. My Block are frozen and do nothing in this time").getResult(config);
			
			blockIDs = new ConfigBlock(ConfigBlock.getConfig(config, blocks, 950));
			itemIDs = new ConfigItem(ConfigItem.getConfig(config, items, 15000));
			
			Property tick = config.get(general, "Round Roubin Speed", "1:2:5:10:20:50", "Every number will be math as number * 5 ticks and please write it as the default with a : ");
			
			String[] result = tick.getString().split(":");
			
			ticks = new int[result.length];
			for(int i = 0;i<result.length;i++)
			{
				ticks[i] = Integer.parseInt(result[i]);
			}
			
			Property sDelay = config.get(general, "Structure Storage Backup Delay", 24000, String.format("%s%n%s%n%s%n%s", "This delay says how long the game waits until he make a backup.", "This is required to save all Multistructures", "To prevent that on game/Server Crash that all structure data get lost.", "This is required to let the Multistructure interface be work when the game restarts"));
			savingDelay = Integer.parseInt(sDelay.getString());
			
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
			
			blockIDs.setEnd(config, blocks.toLowerCase());
			itemIDs.setEnd(config, items.toLowerCase());
			
		}
		catch (Exception e)
		{
			SpmodAPI.log.print("Config could not load. Reason: " + e.getLocalizedMessage());
			String i = "";
			for(StackTraceElement el: e.getStackTrace())
			{
				i = String.format("%s%n%s", i, el);
			}
			SpmodAPI.log.print("Error Log: "+i);
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
	
}
