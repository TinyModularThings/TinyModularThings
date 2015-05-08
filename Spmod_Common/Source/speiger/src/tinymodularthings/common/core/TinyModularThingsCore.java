package speiger.src.tinymodularthings.common.core;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.handler.FuelHandler;
import speiger.src.tinymodularthings.common.handler.TinyCraftingHandler;
import speiger.src.tinymodularthings.common.items.tools.ItemNetherCrystal;
import speiger.src.tinymodularthings.common.world.WorldCrafter;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class TinyModularThingsCore
{
	
	public void registerRenderer()
	{
		
	}
	
	public void registerRenderer(Block par1)
	{
		
	}
	
	public void registerServer()
	{
		GameRegistry.registerCraftingHandler(new TinyCraftingHandler());
		FuelHandler.init();
		DataStorage.registerNBTReciver((ItemNetherCrystal) TinyItems.netherCrystal);
		initHopperUpgrades();
		ForgeRegister.regist(new WorldCrafter());
	}
	
	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		
	}
	
	public void initHopperUpgrades()
	{
	}

	public void spawnParticle(World world, double x, double y, double z, float speed, float rotation)
	{
	
	}
}
