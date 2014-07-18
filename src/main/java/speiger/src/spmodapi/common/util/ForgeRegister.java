package speiger.src.spmodapi.common.util;

import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.handler.BucketHandler;
import speiger.src.spmodapi.common.handler.LivingHandler;
//import speiger.src.spmodapi.common.sound.SoundRegistry;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.retrogen.ChunkCollector;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler;
import speiger.src.spmodapi.common.world.retrogen.RetrogenSave;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class ForgeRegister
{
	public static void registerServer()
	{
		
		regist(new RetrogenSave());
		GameRegistry.registerWorldGenerator(SpmodWorldGen.getWorldGen(), 0);
		regist(ChunkCollector.getInstance());
		
		FMLCommonHandler.instance().bus().register(new CountdownTick());
		regist(LivingHandler.instance);
		if (SpmodConfig.retogen)
		{
			FMLCommonHandler.instance().bus().register(RetroGenTickHandler.getTicks());
		}
		StructureStorage.registerForgeEvent();
		regist(new BucketHandler());
	}
	
	public static void regsiterClient()
	{
		regist(TileIconMaker.getIconMaker());
		// regist(SoundRegistry.getInstance()); SoundRegistry broken at the moment
	}
	
	public static void regist(Object par1)
	{
		MinecraftForge.EVENT_BUS.register(par1);
	}
}
