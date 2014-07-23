package speiger.src.tinymodularthings.common.config;

import java.io.File;

import net.minecraftforge.common.Configuration;
import speiger.src.api.util.config.ConfigBoolean;
import speiger.src.spmodapi.common.config.configType.ConfigBlock;
import speiger.src.spmodapi.common.config.configType.ConfigItem;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.plugins.BC.BCRegistry;

public class TinyConfig
{
	private static TinyConfig instance = new TinyConfig();
	
	public static TinyConfig getConfig()
	{
		return instance;
	}
	
	public static Configuration config;
	
	public static final String general = "General";
	public static final String items = "Items";
	public static final String blocks = "Blocks";
	public static final String world = "World";
	public static final String retro = "World" + config.CATEGORY_SPLITTER + "RetroGen";
	
	public static ConfigBlock block;
	public static ConfigItem item;
	public static ConfigItem pipes;
	
	public static boolean logging = true;
	
	public void loadTinyConfig(File file)
	{
		config = new Configuration(file);
		try
		{
			logging = getBoolean(general, "Active Log", true).setComment("Same thing as SpmodAPI").getResult(config);
			
			if (!logging)
			{
				TinyModularThings.log.print("Disable Logging. That also Includes Crashes");
				TinyModularThings.log.disable();
			}
			
			BCRegistry.overrideVanilla = getBoolean(general, "Vanilla BC Intigration", true).setComment("Make the Vanilla Furnace Compatioble to Gates").getResult(config);
			
			block = new ConfigBlock(ConfigBlock.getConfig(config, blocks, 1300));
			item = new ConfigItem(ConfigItem.getConfig(config, items, 13000));
			pipes = new ConfigItem(ConfigItem.getConfig(config, "Pipe StartID", items, 12000));
			
			TinyBlocksConfig.initBlocks();
			TinyItemsConfig.initItems();
			
			block.setEnd(config, blocks.toLowerCase());
			item.setEnd(config, items.toLowerCase());
			
		}
		catch (Exception e)
		{
			TinyModularThings.log.print("Could not Load TinyModularThings Config");
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
	
}
