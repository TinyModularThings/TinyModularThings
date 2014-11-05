package speiger.src.tinymodularthings;

import static speiger.src.tinymodularthings.common.lib.TinyModularThingsLib.Client;
import static speiger.src.tinymodularthings.common.lib.TinyModularThingsLib.Core;
import static speiger.src.tinymodularthings.common.lib.TinyModularThingsLib.ModID;
import static speiger.src.tinymodularthings.common.lib.TinyModularThingsLib.Name;
import static speiger.src.tinymodularthings.common.lib.TinyModularThingsLib.Version;

import java.io.File;

import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.registry.helpers.Ticks;
import speiger.src.api.common.utils.LogProxy;
import speiger.src.spmodapi.common.util.TickHelper;
import speiger.src.tinymodularthings.common.config.TinyConfig;
import speiger.src.tinymodularthings.common.core.TinyModularThingsCore;
import speiger.src.tinymodularthings.common.entity.EntityRegister;
import speiger.src.tinymodularthings.common.plugins.ModularModLoader;
import speiger.src.tinymodularthings.common.recipes.RecipesCore;
import speiger.src.tinymodularthings.common.utils.HopperBackupSystem;
import speiger.src.tinymodularthings.common.world.WorldRegister;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ModID, name = Name, version = Version, dependencies = "after:BuildCraft|Core")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class TinyModularThings implements SpmodMod
{
	
	@SidedProxy(clientSide = Client, serverSide = Core)
	public static TinyModularThingsCore core;
	
	@Instance(ModID)
	public static TinyModularThings instance;
	
	public static LogProxy log;
	
	public static boolean LanguagePrint = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		SpmodModRegistry.registerMod(this);
		log = new LogProxy(this);
		TinyConfig.getConfig().loadTinyConfig(new File(evt.getModConfigurationDirectory().getAbsolutePath() + "/Spmod/TinyModularThings.cfg"));
		instance = this;
		NetworkRegistry.instance().registerGuiHandler(instance, core);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		RecipesCore.loadAllRecipes();
		WorldRegister.registerOres();
		core.registerRenderer();
		core.registerServer();
		EntityRegister.getInstance().registerEntities();
		HopperBackupSystem.getSystem();
		Ticks.registerTickReciver(TickHelper.getInstance());
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent evt)
	{
		
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent evt)
	{
		ModularModLoader.LoadAddonsSave();
	}
	
	@EventHandler
	public void onStopped(FMLServerStoppedEvent evt)
	{
		core.registerRenderer();
	}
	
	@Override
	public String getName()
	{
		return Name;
	}
	
	@Override
	public String getLangFolder()
	{
		return "/assets/tinymodularthings/lang";
	}
	
	@Override
	public LogProxy getLogger()
	{
		return log;
	}
}
