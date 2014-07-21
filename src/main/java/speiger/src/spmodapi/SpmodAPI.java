package speiger.src.spmodapi;

import static speiger.src.spmodapi.common.lib.SpmodAPILib.Client;
import static speiger.src.spmodapi.common.lib.SpmodAPILib.Core;
import static speiger.src.spmodapi.common.lib.SpmodAPILib.ModID;
import static speiger.src.spmodapi.common.lib.SpmodAPILib.Name;
import static speiger.src.spmodapi.common.lib.SpmodAPILib.Version;

import java.io.File;

import speiger.src.api.language.LanguageLoader;
import speiger.src.api.util.LogProxy;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.command.CommandRegistry;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.core.Registry;
import speiger.src.spmodapi.common.core.SpmodAPICore;
import speiger.src.spmodapi.common.handler.SpmodPacket;
import speiger.src.spmodapi.common.modHelper.ModHelperLoader;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.WorldLoader;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author Speiger
 * 
 */
@Mod(modid = ModID, name = Name, version = Version, useMetadata = true)
public class SpmodAPI implements SpmodMod
{
	@SidedProxy(clientSide = Client, serverSide = Core)
	public static SpmodAPICore core;
	
	@Instance(ModID)
	public static SpmodAPI instance;
	
	public static LogProxy log;

	public static SimpleNetworkWrapper netChannel;
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		SpmodModRegistry.registerMod(this);
		log = new LogProxy(this);
		LanguageLoader language = new LanguageLoader(this);
		language.registerAllAviableLanguages();
		SpmodConfig.getInstance().loadSpmodCondig(new File(evt.getModConfigurationDirectory().getAbsolutePath() + "/Spmod/SpmodAPIBeta.cfg"));
		netChannel = NetworkRegistry.INSTANCE.newSimpleChannel("Spmod");
		// Probably not the way to do it
		netChannel.registerMessage(SpmodPacket.SpmodPacketHandler.class, SpmodPacket.class, 0, Side.CLIENT);
		netChannel.registerMessage(SpmodPacket.SpmodPacketHandler.class, SpmodPacket.class, 1, Side.SERVER);
		instance = this;
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, core);

	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		core.clientSide();
		core.serverSide();
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent evt)
	{
		WorldLoader.getInstance().registerOres();
		SpmodWorldGen.getWorldGen().init(SpmodConfig.getInstance());
		ModHelperLoader.loadModAdditions();
		CommandRegistry.init();
	}
	
	@EventHandler
	public void onServerStarted(FMLServerStartingEvent evt)
	{
		StructureStorage.instance.readStructureDataFromNBT(evt.getServer());
	}
	
	@EventHandler
	public void onServerStopped(FMLServerStoppedEvent evt)
	{
		StructureStorage.instance.writeStructureDataToNBT(FMLCommonHandler.instance().getMinecraftServerInstance());
	}
	
	@Override
	public String getName()
	{
		return Name;
	}
	
	@Override
	public String getLangFolder()
	{
		return "/assets/spmodapi/lang";
	}
	
	@Override
	public LogProxy getLogger()
	{
		return log;
	}
	
	static
	{
		Registry.register();
		
	}
}