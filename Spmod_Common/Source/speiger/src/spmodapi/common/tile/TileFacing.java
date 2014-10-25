package speiger.src.spmodapi.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import speiger.src.spmodapi.common.interfaces.IRotation;
import speiger.src.spmodapi.common.util.FacingUtil;

public abstract class TileFacing extends AdvTile implements IRotation
{
	public int facing = 0;
	public int rotation = 0;
	

	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		facing = nbt.getInteger("facing");
		rotation = nbt.getInteger("rotation");
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("facing", facing);
		nbt.setInteger("rotation", rotation);
	}
	
	@Override
	public void setFacing(int i)
	{
		facing = i;
	}
	
	@Override
	public int getFacing()
	{
		return facing;
	}
	
	@Override
	public void setRotation(int side)
	{
		rotation = side;
	}
	
	@Override
	public int getRotation()
	{
		return rotation;
	}
	
	@Override
	public int setNextFacing()
	{
		return FacingUtil.getNextFacing(facing, isSixSidedFacing());
	}
	
	public abstract boolean isSixSidedFacing();
	
	@Override
	public int setNextRotation()
	{
		
		return FacingUtil.getNextFacing(rotation, isSixSidedRotation());
	}
	
	public boolean isSixSidedRotation()
	{
		return false;
	}
	
}
