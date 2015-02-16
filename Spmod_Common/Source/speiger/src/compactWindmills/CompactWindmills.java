package speiger.src.compactWindmills;

import static speiger.src.compactWindmills.common.core.CWPreference.*;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.utils.LogProxy;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.compactWindmills.common.blocks.BlockWindmill;
import speiger.src.compactWindmills.common.blocks.ItemBlockWindmill;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.core.CWPreference;
import speiger.src.compactWindmills.common.core.CompactWindmillsCore;
import speiger.src.compactWindmills.common.items.IceRotor;
import speiger.src.compactWindmills.common.items.ItemRotor;
import speiger.src.compactWindmills.common.utils.WindmillType;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = ModID, version = ModVersion, name = ModName, dependencies = "required-after:SpmodAPI;required-after:IC2")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class CompactWindmills implements SpmodMod
{
	public static LogProxy logger;
	
	@Instance(ModID)
	public static CompactWindmills instance;
	
	@SidedProxy(clientSide = ModClient, serverSide = ModCore)
	public static CompactWindmillsCore proxy;
	
	public static boolean specailRenderer = true;
	public static boolean sharedWindmillModel = false;
	
	public static Block windmill;
	public static Item rotor;
	public static Item iceRotor; 
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		logger = new LogProxy(this);
		SpmodModRegistry.registerMod(this);
		Configuration config = new Configuration(new File(evt.getModConfigurationDirectory().getAbsolutePath() + "/Spmod/CompactWindmills.cfg"));
		try
		{
			specailRenderer = Boolean.parseBoolean(config.get("General", "Specail Renderer for Winmills", true).getString());
			sharedWindmillModel = new ConfigBoolean("General", "Shared Models", false).setComment("Shared Models mean that every rotor use the first seen size of a rotor. That make it using less ram. else it could be that a Highvoltaleg Windmill has the size of a Extrem Lowvaltalege windmill. If you want that set it to true").getResult(config);
			windmill = new BlockWindmill(Integer.parseInt(config.getBlock("Windmill", 2790).getString()));
			rotor = new ItemRotor(Integer.parseInt(config.getItem("Rotor Item", 27900).getString()));
			iceRotor = new IceRotor(Integer.parseInt(config.getItem("IceRotor", 27902).getString()));
			RegisterProxy.RegisterBlock(windmill, ItemBlockWindmill.class, "Windmill");
			RegisterProxy.RegisterTile(windmill, WindMill.class, "WindmillType");
			RegisterProxy.RegisterItem(ModID, "BasicRotors", rotor);
			RegisterProxy.RegisterItem(ModID, "icerotor", iceRotor);
		}
		catch (Exception e)
		{
			logger.print("Error with IDs Please Check that before you run this Mod!");
			for (StackTraceElement stack : e.getStackTrace())
			{
				logger.print(stack);
			}
		}
		finally
		{
			config.save();
		}
		
		TextureEngine engine = TextureEngine.getTextures();
		engine.setCurrentMod(CWPreference.ModID.toLowerCase());
		engine.setCurrentPath("rotors");
		engine.registerTexture(rotor, "rotor.basic.wood", "rotor.basic.wool", "rotor.basic.iron", "rotor.basic.carbon", "rotor.basic.alloy", "rotor.basic.iridium");
		engine.removePath();
		WindmillType.registerTextures(engine);
		engine.finishMod();
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
	public LogProxy getLogger()
	{
		return logger;
	}
	
}
