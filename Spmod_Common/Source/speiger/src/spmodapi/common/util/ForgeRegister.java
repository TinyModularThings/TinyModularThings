package speiger.src.spmodapi.common.util;

import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.handler.BucketHandler;
import speiger.src.spmodapi.common.handler.LivingHandler;
import speiger.src.spmodapi.common.sound.SoundRegistry;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.retrogen.ChunkCollector;
import speiger.src.spmodapi.common.world.retrogen.RetroGenTickHandler;
import speiger.src.spmodapi.common.world.retrogen.RetrogenSave;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ForgeRegister
{
	public static void registerServer()
	{
		
		regist(new RetrogenSave());
		GameRegistry.registerWorldGenerator(SpmodWorldGen.getWorldGen());
		regist(ChunkCollector.getInstance());
		
		TickRegistry.registerTickHandler(new CountdownTick(), Side.SERVER);
		regist(LivingHandler.instance);
		if (SpmodConfig.retogen)
		{
			TickRegistry.registerTickHandler(RetroGenTickHandler.getTicks(), Side.SERVER);
		}
		StructureStorage.registerForgeEvent();
		regist(new BucketHandler());
	}
	
	public static void regsiterClient()
	{
		regist(TileIconMaker.getIconMaker());
		regist(SoundRegistry.getInstance());
	}
	
	public static void regist(Object par1)
	{
		MinecraftForge.EVENT_BUS.register(par1);
	}
}
