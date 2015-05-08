package speiger.src.spmodapi.common.network.packets.base;

import java.io.DataInput;
import java.io.DataOutput;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.common.data.packets.ISpmodPacket;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;


public abstract class PositionPacket implements ISpmodPacket
{
	int dimID;
	int x;
	int y;
	int z;
	
	public PositionPacket()
	{
		
	}
	
	public PositionPacket(World par1, int par2, int par3, int par4)
	{
		this(par1.provider.dimensionId, par2, par3, par4);
	}
	
	public PositionPacket(int dim, int xCoord, int yCoord, int zCoord)
	{
		dimID = dim;
		x = xCoord;
		y = yCoord;
		z = zCoord;
	}
	
	@Override
	public void readData(DataInput par1) throws Exception
	{
		dimID = par1.readInt();
		x = par1.readInt();
		y = par1.readInt();
		z = par1.readInt();
	}

	@Override
	public void writeData(DataOutput par1) throws Exception
	{
		par1.writeInt(dimID);
		par1.writeInt(x);
		par1.writeInt(y);
		par1.writeInt(z);
	}
	
	public TileEntity getTileEntity(Side par1)
	{
		World world;
		if(par1 == Side.CLIENT) world = SpmodAPI.core.getClientWorld(dimID);
		else world = DimensionManager.getWorld(dimID);
		
		if(world != null)
		{
			return world.getBlockTileEntity(x, y, z);
		}
		return null;
	}
	
}
