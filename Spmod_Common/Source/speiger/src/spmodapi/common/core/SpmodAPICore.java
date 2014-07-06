package speiger.src.spmodapi.common.core;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import speiger.src.spmodapi.client.gui.commands.GuiCommands;
import speiger.src.spmodapi.common.blocks.utils.InventoryCrafter;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.ForgeRegister;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import cpw.mods.fml.common.FMLLog;
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
		FMLLog.getLogger().info("Test: "+ID);
		if(ID == EnumGuiIDs.WorkBench.getID())
		{
			return new InventoryCrafter(player.inventory, world, x, y, z);
		}
		else if(ID == EnumGuiIDs.Tiles.getID())
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if(tile != null && tile instanceof AdvTile)
			{
				return ((AdvTile)tile).getInventory(player.inventory);
			}
		}
		else if(ID == EnumGuiIDs.Commands.getID())
		{
			AdvContainer con = new AdvContainer(player.inventory){

				@Override
				public boolean canInteractWith(EntityPlayer entityplayer)
				{
					return true;
				}
			};
			if(con != null)
			{
				return con;
			}
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == EnumGuiIDs.WorkBench.getID())
		{
			return new GuiCrafting(player.inventory, world, x, y, z);
		}
		else if(ID == EnumGuiIDs.Tiles.getID())
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if(tile != null && tile instanceof AdvTile)
			{
				return ((AdvTile)tile).getGui(player.inventory);
			}
		}
		else if(ID == EnumGuiIDs.Commands.getID())
		{
			return new GuiCommands(player.inventory);
		}
		return null;
	}
}
