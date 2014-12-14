package speiger.src.ic2Fixes;

import static speiger.src.ic2Fixes.common.core.IC2FixesLib.Core;
import static speiger.src.ic2Fixes.common.core.IC2FixesLib.ModID;
import static speiger.src.ic2Fixes.common.core.IC2FixesLib.Name;
import static speiger.src.ic2Fixes.common.core.IC2FixesLib.Version;
import static speiger.src.ic2Fixes.common.core.IC2FixesLib.client;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.Configuration;
import speiger.src.api.common.utils.config.ConfigBoolean;
import speiger.src.ic2Fixes.common.core.IC2FixesCore;
import speiger.src.ic2Fixes.common.energy.GlobalEnergyNet;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModID, name = Name, version = Version, dependencies = "required-after:IC2")
public class IC2Fixes
{
	@Instance(ModID)
	public static IC2Fixes instance;
	
	@SidedProxy(clientSide = client, serverSide = Core)
	public static IC2FixesCore core;
	
	public static Configuration config;
	public static HashMap<String, Boolean> changes = new HashMap<String, Boolean>();
	public static final String general = "General Config";
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt)
	{
		
		config = new Configuration(new File(evt.getSuggestedConfigurationFile().getAbsolutePath()+"/Spmod/IC2Fixes.cfg"));
		try
		{
			changes.put("OverrideEnergyNet", new ConfigBoolean(general, "Override/Fix EnergyNet", true).setComment(String.format("%s%n%s", "This config activates the Overriding of the IC2 Energy Net. It will simply remove it and reloade it.", "That Overriding will fix the Detector Cable and the EU Reader. It is not required.")).getResult(config));
			changes.put("EnableExplosions", new ConfigBoolean(general, "Activate Explosions", true).setComment(String.format("%s%n%s", "Activate the Explosions from IC2 Energy Blocks. Simply its like 1.4.7 or lower", "This Require the Overriding of the EnergyNet.")).getResult(config));
		}
		catch(Exception e)
		{
		}
		finally
		{
			config.save();
		}

	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt)
	{
	}
	
	@EventHandler
	public void posInit(FMLPostInitializationEvent evt)
	{
		new GlobalEnergyNet();
	}
	
}
