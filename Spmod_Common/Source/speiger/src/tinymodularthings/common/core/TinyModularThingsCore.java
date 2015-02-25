package speiger.src.tinymodularthings.common.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.client.gui.IItemGui;
import speiger.src.api.client.gui.IUpgradeGuiProvider;
import speiger.src.api.common.data.nbt.DataStorage;
import speiger.src.api.common.registry.hopper.HopperRegistry;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;
import speiger.src.api.common.world.tiles.interfaces.IHopper;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;
import speiger.src.tinymodularthings.common.commands.RenderCommand;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.enums.HopperUpgradeIDs;
import speiger.src.tinymodularthings.common.handler.FuelHandler;
import speiger.src.tinymodularthings.common.handler.TinyCraftingHandler;
import speiger.src.tinymodularthings.common.interfaces.IEntityGuiProvider;
import speiger.src.tinymodularthings.common.interfaces.IPipeGuiProvider;
import speiger.src.tinymodularthings.common.items.tools.ItemNetherCrystal;
import speiger.src.tinymodularthings.common.world.WorldCrafter;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.common.network.IGuiHandler;
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
		new RenderCommand();
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
