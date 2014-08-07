package speiger.src.tinymodularthings.common.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.hopper.HopperRegistry;
import speiger.src.api.hopper.HopperUpgrade;
import speiger.src.api.hopper.IHopper;
import speiger.src.api.hopper.IUpgradeGuiProvider;
import speiger.src.api.items.IItemGui;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.handler.FuelHandler;
import speiger.src.tinymodularthings.common.handler.TinyCraftingHandler;
import speiger.src.tinymodularthings.common.interfaces.IEntityGuiProvider;
import speiger.src.tinymodularthings.common.upgrades.hoppers.HopperItemFilter;
import buildcraft.transport.ItemPipe;
import buildcraft.transport.Pipe;
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
		else if(ID == EnumIDs.HopperUpgrades.getId())
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("UpgradeGui") && tile != null && tile instanceof IHopper)
			{
				HopperUpgrade hopper = HopperRegistry.getHopperUpgradeFromNBT(stack.getTagCompound().getString("UpgradeGui"));
				if(hopper != null && hopper instanceof IUpgradeGuiProvider)
				{
					return ((IUpgradeGuiProvider)hopper).getInventory(player.inventory, (IHopper)tile);
				}
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
		else if(ID == EnumIDs.HopperUpgrades.getId())
		{
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("UpgradeGui") && tile != null && tile instanceof IHopper)
			{
				HopperUpgrade hopper = HopperRegistry.getHopperUpgradeFromNBT(stack.getTagCompound().getString("UpgradeGui"));
				if(hopper != null && hopper instanceof IUpgradeGuiProvider)
				{
					return ((IUpgradeGuiProvider)hopper).getGui(player.inventory, (IHopper)tile);
				}
			}
		}
		return null;
	}
	
	public void registerServer()
	{
		GameRegistry.registerCraftingHandler(new TinyCraftingHandler());
		HopperRegistry.registerHopperUpgrade(new HopperItemFilter());
		FuelHandler.init();
	}

	public void loadPipe(ItemPipe par1, int id, Class<? extends Pipe> par2)
	{
		
	}
	
}
