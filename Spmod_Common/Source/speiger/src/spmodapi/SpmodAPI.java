package speiger.src.spmodapi;

import static speiger.src.spmodapi.common.lib.SpmodAPILib.*;

import java.io.File;

import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.api.common.data.packets.SpmodPacketHelper;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.registry.helpers.Ticks;
import speiger.src.api.common.utils.LogProxy;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.command.commands.CommandServerTimer;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.core.SpmodAPICore;
import speiger.src.spmodapi.common.network.SpmodPacketHandler;
import speiger.src.spmodapi.common.plugins.PluginLoader;
import speiger.src.spmodapi.common.sound.SoundRegistry;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.data.ClassStorage;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.WorldLoader;
import speiger.src.spmodapi.common.world.retrogen.ChunkCollector;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * 
 * @author Speiger
 * 
 */
@Mod(modid = ModID, name = Name, version = Version)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = "Spmod", packetHandler = SpmodPacketHandler.class)
public class SpmodAPI implements SpmodMod
{

	@SidedProxy(clientSide = Client, serverSide = Core)
	public static SpmodAPICore core;
	
	@SidedProxy(clientSide = ClientMods, serverSide = CoreMods)
	public static PluginLoader plugins;
	
	@SidedProxy(clientSide = ClientSounds, serverSide = CoreSounds)
	public static SoundRegistry soundEngine;
	
	@Instance(ModID)
	public static SpmodAPI instance;
	
	public static LogProxy log;
	
	public static SpmodPacketHandler handler;
		
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		SpmodModRegistry.registerMod(this);
		log = new LogProxy(this);
		TextureEngine.getTextures();
		SpmodConfig.getInstance().loadSpmodCondig(new File(evt.getModConfigurationDirectory().getAbsolutePath() + "/Spmod/SpmodAPIBeta.cfg"));
		handler = new SpmodPacketHandler();
		NetworkRegistry.instance().registerChannel(handler, "Spmod");
		SpmodPacketHelper.handler = handler;
		instance = this;
		NetworkRegistry.instance().registerGuiHandler(instance, core);

	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		core.clientSide();
		core.serverSide();
		Ticks.registerTickReciver(new CommandServerTimer());
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent evt)
	{
		WorldLoader.getInstance().registerOres();
		SpmodWorldGen.getWorldGen().init(SpmodConfig.getInstance());
		plugins.loadModAdditions();
		CommandRegistry.init();
		ChunkCollector.getInstance().loadConfig();
	}
	
	@EventHandler
	public void onServerStarting(FMLServerStartingEvent evt)
	{
		DataStorage.read(evt.getServer());
		ClassStorage.getInstance().onWorldLoad();
	}
	
	@EventHandler
	public void onServerStopping(FMLServerStoppingEvent evt)
	{
		DataStorage.write(FMLCommonHandler.instance().getMinecraftServerInstance(), true);
	}
	
	@Override
	public String getName()
	{
		return Name;
	}
	
	@Override
	public LogProxy getLogger()
	{
		return log;
	}
}