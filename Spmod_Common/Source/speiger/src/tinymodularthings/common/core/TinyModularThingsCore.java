package speiger.src.tinymodularthings.common.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.common.blocks.machine.OilGenerator;
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
import speiger.src.tinymodularthings.common.upgrades.hoppers.all.FilterUpgrade;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
import buildcraft.transport.TileGenericPipe;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class TinyModularThingsCore implements IGuiHandler
{
	
	public void registerRenderer()
	{
		
	}
	
	public void registerRenderer(Block par1)
	{
		
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (ID == EnumIDs.ADVTiles.getId())
		{
			if (tile instanceof AdvTile)
			{
				AdvTile advtile = (AdvTile) tile;
				if (advtile.hasContainer())
				{
					return advtile.getInventory(player.inventory);
				}
			}
		}
		else if (ID == EnumIDs.Entities.getId())
		{
			if (y < 0)
			{
				Entity entity = world.getEntityByID(x);
				if (entity != null && entity instanceof IEntityGuiProvider)
				{
					IEntityGuiProvider gui = (IEntityGuiProvider) entity;
					if (gui.hasGui())
					{
						return gui.getInventory(player.inventory);
					}
					
				}
			}
		}
		else if (ID == EnumIDs.Items.getId())
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null && stack.getItem() instanceof IItemGui)
			{
				IItemGui container = (IItemGui) stack.getItem();
				if (container.hasContainer(stack))
				{
					return container.getContainer(player.inventory, stack);
				}
			}
		}
		else if (EnumIDs.HopperUpgrades.isInRange(ID))
		{
			if (tile != null && tile instanceof TinyHopper)
			{
				TinyHopper hopper = (TinyHopper) tile;
				int left = ID - EnumIDs.HopperUpgrades.getId();
				HopperUpgradeIDs upgrade = HopperUpgradeIDs.values()[left];
				HopperUpgrade iUpgrade = HopperRegistry.getHopperUpgradeFromNBT(upgrade.getUpgradeClass());
				if (iUpgrade != null)
				{
					if (iUpgrade instanceof IUpgradeGuiProvider)
					{
						return ((IUpgradeGuiProvider) iUpgrade).getInventory(player.inventory, hopper);
					}
				}
			}
		}
		else if(ID == EnumIDs.BCPipes.getId())
		{
			try
			{
				if(tile != null && tile instanceof TileGenericPipe)
				{
					TileGenericPipe pipe = (TileGenericPipe)tile;
					if(pipe.pipe != null && pipe.pipe instanceof IPipeGuiProvider)
					{
						return ((IPipeGuiProvider)pipe.pipe).getInventory(player.inventory);
					}
				}
			}
			catch(Exception e)
			{
				
			}
		}
		else if(ID == EnumIDs.BlockGui.getId())
		{
			int meta = world.getBlockMetadata(x, y, z);
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if(block != null && block instanceof IBlockGui)
			{
				IBlockGui gui = (IBlockGui)block;
				
				BlockPosition pos = new BlockPosition(world, x, y, z);
				return gui.getInventory(meta, player.inventory, pos);
			}
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (ID == EnumIDs.ADVTiles.getId())
		{
			if (tile instanceof AdvTile)
			{
				AdvTile advtile = (AdvTile) tile;
				if (advtile.hasContainer())
				{
					return advtile.getGui(player.inventory);
				}
			}
		}
		else if (ID == EnumIDs.Entities.getId())
		{
			if (y < 0)
			{
				Entity entity = world.getEntityByID(x);
				if (entity != null && entity instanceof IEntityGuiProvider)
				{
					IEntityGuiProvider gui = (IEntityGuiProvider) entity;
					if (gui.hasGui())
					{
						return gui.getGui(player.inventory);
					}
					
				}
			}
		}
		else if (ID == EnumIDs.Items.getId())
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if (stack != null && stack.getItem() instanceof IItemGui)
			{
				IItemGui container = (IItemGui) stack.getItem();
				if (container.hasGui(stack))
				{
					return container.getGui(player.inventory, stack);
				}
			}
		}
		else if (EnumIDs.HopperUpgrades.isInRange(ID))
		{
			if (tile != null && tile instanceof IHopper)
			{
				IHopper hopper = (IHopper) tile;
				int left = ID - EnumIDs.HopperUpgrades.getId();
				HopperUpgradeIDs upgrade = HopperUpgradeIDs.values()[left];
				HopperUpgrade iUpgrade = HopperRegistry.getHopperUpgradeFromNBT(upgrade.getUpgradeClass());
				if (iUpgrade != null)
				{
					if (iUpgrade instanceof IUpgradeGuiProvider)
					{
						return ((IUpgradeGuiProvider) iUpgrade).getGui(player.inventory, hopper);
					}
				}
			}
		}
		else if(ID == EnumIDs.BCPipes.getId())
		{
			try
			{
				if(tile != null && tile instanceof TileGenericPipe)
				{
					TileGenericPipe pipe = (TileGenericPipe)tile;
					if(pipe.pipe != null && pipe.pipe instanceof IPipeGuiProvider)
					{
						return ((IPipeGuiProvider)pipe.pipe).getGui(player.inventory);
					}
				}
			}
			catch(Exception e)
			{
				
			}
		}
		else if(ID == EnumIDs.BlockGui.getId())
		{
			int meta = world.getBlockMetadata(x, y, z);
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			BlockPosition pos = new BlockPosition(world, x, y, z);
			if(block != null && block instanceof IBlockGui)
			{
				IBlockGui gui = (IBlockGui)block;
				return gui.getGui(meta, player.inventory, pos);
			}
		}
		return null;
	}
	
	public void registerServer()
	{
		GameRegistry.registerCraftingHandler(new TinyCraftingHandler());
		FuelHandler.init();
		DataStorage.registerNBTReciver((ItemNetherCrystal) TinyItems.netherCrystal);
		DataStorage.registerNBTReciver(TileIconMaker.getIconMaker().getTileEntityFromClass(OilGenerator.class));
		new RenderCommand();
		initHopperUpgrades();
	}
	
	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		
	}
	
	public void initHopperUpgrades()
	{
		HopperRegistry.registerHopperUpgrade(new FilterUpgrade());
	}

	public void spawnParticle(World world, double x, double y, double z, float speed, float rotation)
	{
		
	}
}
