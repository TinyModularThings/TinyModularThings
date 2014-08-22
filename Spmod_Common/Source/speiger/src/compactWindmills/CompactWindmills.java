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
import speiger.src.api.util.LogProxy;
import speiger.src.api.util.SpmodMod;
import speiger.src.compactWindmills.common.blocks.BlockWindmill;
import speiger.src.compactWindmills.common.blocks.ItemBlockWindmill;
import speiger.src.compactWindmills.common.blocks.WindMill;
import speiger.src.compactWindmills.common.core.CompactWindmillsCore;
import speiger.src.compactWindmills.common.items.ItemRotor;
import speiger.src.spmodapi.common.util.proxy.RegisterProxy;
import cpw.mods.fml.common.FMLLog;
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
	
	public static Block windmill;
	public static Item rotor;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		logger = new LogProxy(this);
		Configuration config = new Configuration(new File(evt.getModConfigurationDirectory().getAbsolutePath()+"/Spmod/CompactWindmills.cfg"));
		FMLLog.getLogger().info("Test");
		try
		{
			FMLLog.getLogger().info("Test1");
			oldIC2 = Boolean.parseBoolean(config.get("General", "IC2 Vanilla WindMills", false).getString());
			FMLLog.getLogger().info("Test2");
			specailRenderer = Boolean.parseBoolean(config.get("General", "Specail Renderer for Winmills", true).getString());
			FMLLog.getLogger().info("Test3");
			windmill = new BlockWindmill(Integer.parseInt(config.getBlock("Windmill", 2790).getString()));
			FMLLog.getLogger().info("Test4");
			rotor = new ItemRotor(Integer.parseInt(config.getItem("Rotor Item", 27900).getString()));
			FMLLog.getLogger().info("Test5");
			RegisterProxy.RegisterBlock(windmill, ItemBlockWindmill.class, "Windmill");
			FMLLog.getLogger().info("Test6");
			RegisterProxy.RegisterTile(windmill, WindMill.class, "WindmillType");
			FMLLog.getLogger().info("Test7: "+rotor);
			RegisterProxy.RegisterItem(ModID, "Windmill", rotor);
			FMLLog.getLogger().info("Test8");
		}
		catch (Exception e)
		{
			for(StackTraceElement stack : e.getStackTrace())
			{
				logger.print(stack);
			}
		}
		finally
		{
			config.save();
			FMLLog.getLogger().info("Test9");
		}
		FMLLog.getLogger().info("Test10");
	}
	
	@EventHandler
	public void load(FMLInitializationEvent evt)
	{
		
	}
	
	@EventHandler
	public void modsLoaded(FMLPostInitializationEvent evt)
	{
		
	}
	
	@Override
	public String getName()
	{
		return ModName;
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
