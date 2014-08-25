package speiger.src.spmodapi.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import speiger.src.spmodapi.common.util.FacingUtil;

public abstract class TileIC2Facing extends AdvTile
{
	public short facing = 0;
	public int rotation = 0;
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getShort("facing");
		rotation = nbt.getInteger("rotation");
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("facing", facing);
		nbt.setInteger("rotation", rotation);
	}
	
	public void setFacing(short i)
	{
		facing = i;
	}
	
	public short getFacing()
	{
		return facing;
	}
	
	public void setRotation(int side)
	{
		rotation = side;
	}
	
	
	public int getRotation()
	{
		return rotation;
	}
	
	public int setNextFacing()
	{
		return FacingUtil.getNextFacing(facing, isSixSidedFacing());
	}
	
	public abstract boolean isSixSidedFacing();
	
	
	public int setNextRotation()
	{
		
		return FacingUtil.getNextFacing(rotation, isSixSidedRotation());
	}
	
	public boolean isSixSidedRotation()
	{
		return false;
	}
}
