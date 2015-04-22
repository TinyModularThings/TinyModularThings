package speiger.src.spmodapi.common.util;

import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.config.SpmodConfig;
import speiger.src.spmodapi.common.handler.BlockHandler;
import speiger.src.spmodapi.common.handler.BucketHandler;
import speiger.src.spmodapi.common.handler.InventoryHandler;
import speiger.src.spmodapi.common.handler.LivingHandler;
import speiger.src.spmodapi.common.sound.SoundRegistryClient;
import speiger.src.spmodapi.common.util.data.ServerTick;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.world.SpmodWorldGen;
import speiger.src.spmodapi.common.world.event.BlockDetector;
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
		regist(new BlockDetector());
		TickRegistry.registerTickHandler(new CountdownTick(), Side.SERVER);
		StructureStorage.registerForgeEvent();
		TickRegistry.registerTickHandler(new ServerTick(), Side.SERVER);
		regist(InventoryHandler.instance);
		regist(BlockHandler.getInstance());
		if (SpmodConfig.booleanInfos.get("Retrogen"))
		{
			TickRegistry.registerTickHandler(RetroGenTickHandler.getTicks(), Side.SERVER);
		}
		if(!SpmodConfig.booleanInfos.get("APIOnly"))
		{
			regist(new BucketHandler());
			regist(LivingHandler.instance);
		}

	}
	
	public static void regsiterClient()
	{
		regist(SpmodAPI.soundEngine);
		TickRegistry.registerTickHandler((SoundRegistryClient)SpmodAPI.soundEngine, Side.CLIENT);
	}
	
	public static void regist(Object par1)
	{
		MinecraftForge.EVENT_BUS.register(par1);
	}
}
