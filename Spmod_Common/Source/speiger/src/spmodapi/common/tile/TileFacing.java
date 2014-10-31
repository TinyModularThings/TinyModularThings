package speiger.src.spmodapi.common.tile;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.spmodapi.common.interfaces.IRotation;
import speiger.src.spmodapi.common.util.FacingUtil;

public abstract class TileFacing extends AdvTile implements IRotation
{
	public int facing = 0;
	public int rotation = 0;
	
	public HashMap<String, Integer> sideOpened = new HashMap<String, Integer>();
	
	
	
	@Override
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		if(sideOpened.containsKey(par1.username))
		{
			sideOpened.remove(par1.username);
		}
		super.onPlayerCloseContainer(par1);
	}
	
	
	@Override
	public boolean onOpened(EntityPlayer par1, int side)
	{
		sideOpened.put(par1.username, side);
		return super.onOpened(par1, side);
	}

	public int getSideFromPlayer(String name)
	{
		if(sideOpened.containsKey(name))
		{
			return sideOpened.get(name);
		}
		return -1;
	}

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
