package speiger.src.spmodapi.common.lib.bc;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Utils
{
	public static IInventory getInventory(IInventory inv)
	{
		if (inv instanceof TileEntityChest)
		{
			TileEntityChest chest = (TileEntityChest) inv;
			Position pos = new Position(chest.xCoord, chest.yCoord, chest.zCoord);
			TileEntity tile;
			IInventory chest2 = null;
			tile = Utils.getTile(chest.getWorldObj(), pos, ForgeDirection.WEST);
			if (tile instanceof TileEntityChest)
			{
				chest2 = (IInventory) tile;
			}
			tile = Utils.getTile(chest.getWorldObj(), pos, ForgeDirection.EAST);
			if (tile instanceof TileEntityChest)
			{
				chest2 = (IInventory) tile;
			}
			tile = Utils.getTile(chest.getWorldObj(), pos, ForgeDirection.NORTH);
			if (tile instanceof TileEntityChest)
			{
				chest2 = (IInventory) tile;
			}
			tile = Utils.getTile(chest.getWorldObj(), pos, ForgeDirection.SOUTH);
			if (tile instanceof TileEntityChest)
			{
				chest2 = (IInventory) tile;
			}
			if (chest2 != null)
			{
				return new InventoryLargeChest("", inv, chest2);
			}
		}
		return inv;
	}
	
	public static TileEntity getTile(World world, Position pos, ForgeDirection step)
	{
		Position tmp = new Position(pos);
		tmp.orientation = step;
		tmp.moveForwards(1.0);
		
		return world.getTileEntity((int) tmp.x, (int) tmp.y, (int) tmp.z);
	}
}
