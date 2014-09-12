package speiger.src.compactWindmills;

import static speiger.src.compactWindmills.common.core.CWPreference.ModClient;
import static speiger.src.compactWindmills.common.core.CWPreference.ModCore;
import static speiger.src.compactWindmills.common.core.CWPreference.ModID;
import static speiger.src.compactWindmills.common.core.CWPreference.ModName;
import static speiger.src.compactWindmills.common.core.CWPreference.ModVersion;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import speiger.src.api.language.LanguageLoader;
import speiger.src.api.util.LogProxy;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.api.util.config.ConfigBoolean;
import speiger.src.compactWindmills.common.blocks.BlockWindmill;
import speiger.src.compactWindmills.common.blocks.ItemBlockWindmill;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.core.CompactWindmillsCore;
import speiger.src.compactWindmills.common.items.ItemAdvancedRotor;
import speiger.src.compactWindmills.common.items.ItemRotor;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModID, version = ModVersion, name = ModName, dependencies = "required-after:SpmodAPI")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CompactWindmills implements SpmodMod
{
	public static LogProxy logger;
	
	@Instance(ModID)
	public static CompactWindmills instance;
	
	@SidedProxy(clientSide = ModClient, serverSide = ModCore)
	public static CompactWindmillsCore proxy;
	
	public static boolean oldIC2 = false;
	public static boolean specailRenderer = true;
	public static boolean sharedWindmillModel = false;
	
	public static Block windmill;
	public static Item rotor;
	public static Item advRotor;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		logger = new LogProxy(this);
		SpmodModRegistry.registerMod(this);
		LanguageLoader loader = new LanguageLoader(this);
		loader.registerAllAviableLanguages();
		Configuration config = new Configuration(new File(evt.getModConfigurationDirectory().getAbsolutePath() + "/Spmod/CompactWindmills.cfg"));
		try
		{
			oldIC2 = Boolean.parseBoolean(config.get("General", "IC2 Vanilla WindMills", false).getString());
			specailRenderer = Boolean.parseBoolean(config.get("General", "Specail Renderer for Winmills", true).getString());
			sharedWindmillModel = new ConfigBoolean("General", "Shared Models", false).setComment("Shared Models mean that every rotor use the first seen size of a rotor. That make it using less ram. else it could be that a Highvoltaleg Windmill has the size of a Extrem Lowvaltalege windmill. If you want that set it to true").getResult(config);
			windmill = new BlockWindmill(Integer.parseInt(config.getBlock("Windmill", 2790).getString()));
			rotor = new ItemRotor(Integer.parseInt(config.getItem("Rotor Item", 27900).getString()));
			advRotor = new ItemAdvancedRotor(Integer.parseInt(config.getItem("Advanced Rotor Item", 27901).getString()));
			RegisterProxy.RegisterBlock(windmill, ItemBlockWindmill.class, "Windmill");
			RegisterProxy.RegisterTile(windmill, WindMill.class, "WindmillType");
			RegisterProxy.RegisterItem(ModID, "Windmill", rotor);
		}
		catch (Exception e)
		{
			for (StackTraceElement stack : e.getStackTrace())
			{
				logger.print(stack);
			}
		}
		finally
		{
			config.save();
		}
	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		proxy.onClientLoad(this);
		proxy.onServerLoad(this);
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent evt)
	{
		
	}
	
	@Override
	public String getName()
	{
		return ModID;
	}
	
	@Override
	public String getLangFolder()
	{
		return "/assets/compactwindmills/lang";
	}
	
	@Override
	public LogProxy getLogger()
	{
		return logger;
	}
	
}
