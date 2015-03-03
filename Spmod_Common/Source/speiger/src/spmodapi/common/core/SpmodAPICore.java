package speiger.src.spmodapi.common.core;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.api.client.gui.IBlockGui;
import speiger.src.api.client.gui.IItemGui;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.commands.GuiCommands;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.interfaces.IEntityGuiProvider;
import speiger.src.tinymodularthings.common.interfaces.IPipeGuiProvider;
import buildcraft.api.transport.IPipe;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * 
 * @author Speiger
 * 
 */
public class SpmodAPICore implements IGuiHandler
{
	public void serverSide()
	{
		ForgeRegister.registerServer();
		Registry.registerStuff();
		
	}
	
	public void clientSide()
	{
	}
	
	public int getArmorTypeFromName(String type)
	{
		return 0;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == EnumGuiIDs.BlockGui.getID())
		{
			int meta = world.getBlockMetadata(x, y, z);
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			BlockPosition pos = new BlockPosition(world, x, y, z);
			if(block != null && block instanceof IBlockGui)
			{
				IBlockGui gui = (IBlockGui)block;
				return gui.getInventory(meta, player.inventory, pos);
			}
		}
		else if (ID == EnumGuiIDs.Tiles.getID())
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof AdvTile)
			{
				return ((AdvTile) tile).getInventory(player.inventory);
			}
		}
		else if (ID == EnumGuiIDs.Commands.getID())
		{
			AdvContainer con = new AdvContainer(player).setOffset(16, 59).setInventory(player.inventory);
			if (con != null)
			{
				return con;
			}
		}
		else if (ID == EnumGuiIDs.Items.getID())
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
		else if((ID == EnumGuiIDs.Entities.getID()))
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
		else if(ID == EnumGuiIDs.Pipes.getID())
		{
			if(SpmodAPI.plugins.isPluginLoaded("BC"))
			{
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if(tile != null && tile instanceof IPipeTile)
				{
					IPipe pipe = ((IPipeTile)tile).getPipe();
					if(pipe != null && pipe instanceof IPipeGuiProvider)
					{
						return ((IPipeGuiProvider)pipe).getInventory(player.inventory);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == EnumGuiIDs.BlockGui.getID())
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
		else if (ID == EnumGuiIDs.Tiles.getID())
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof AdvTile)
			{
				return ((AdvTile) tile).getGui(player.inventory);
			}
		}
		else if (ID == EnumGuiIDs.Commands.getID())
		{
			return new GuiCommands(player.inventory);
		}
		else if (ID == EnumGuiIDs.Items.getID())
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
		else if(ID == EnumGuiIDs.Entities.getID())
		{
			if (y < 0)
			{
				Entity entity = world.getEntityByID(x);
				if(entity != null && entity instanceof IEntityGuiProvider)
				{
					IEntityGuiProvider gui = (IEntityGuiProvider)entity;
					if(gui.hasGui())
					{
						return gui.getGui(player.inventory);
					}
					
				}
			}
		}
		else if(ID == EnumGuiIDs.Pipes.getID())
		{
			if(SpmodAPI.plugins.isPluginLoaded("BC"))
			{
				TileEntity tile = world.getBlockTileEntity(x, y, z);
				if(tile != null && tile instanceof IPipeTile)
				{
					IPipe pipe = ((IPipeTile)tile).getPipe();
					if(pipe != null && pipe instanceof IPipeGuiProvider)
					{
						return ((IPipeGuiProvider)pipe).getGui(player.inventory);
					}
				}
			}
		}
		return null;
	}

	public void onEngineLoad()
	{		
	}
	
	public int getRenderID()
	{
		return 0;
	}
	
	public void updatePlayerInf(EntityPlayer par1)
	{
		try
		{
			if(par1 != null && par1 instanceof EntityPlayerMP)
			{
				EntityPlayerMP mp = (EntityPlayerMP)par1;
				mp.sendContainerToPlayer(par1.inventoryContainer);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
