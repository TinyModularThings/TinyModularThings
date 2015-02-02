package speiger.src.spmodapi.common.tile;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import speiger.src.spmodapi.common.templates.ITemplateProvider;
import speiger.src.spmodapi.common.util.FacingUtil;

public abstract class TileFacing extends AdvTile
{
	public short facing = 0;
	public short rotation = 0;
	
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
		facing = nbt.getShort("facing");
		rotation = nbt.getShort("rotation");
		NBTTagList list = nbt.getTagList("Opened");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			this.sideOpened.put(data.getString("Key"), data.getInteger("Value"));
		}
	}
	
	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("facing", facing);
		nbt.setShort("rotation", rotation);
		NBTTagList list = new NBTTagList();
		Iterator<Entry<String, Integer>> iter = this.sideOpened.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<String, Integer> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("Value", entry.getValue());
			data.setString("Key", entry.getKey());
		}
		
		nbt.setTag("Opened", list);
	}
	
	public void setFacing(short i)
	{
		facing = i;
	}
	
	public short getFacing()
	{
		return facing;
	}
	
	public void setRotation(short side)
	{
		rotation = side;
	}
	
	public short getRotation()
	{
		return rotation;
	}
	
	public short setNextFacing()
	{
		return FacingUtil.getNextFacing(facing, isSixSidedFacing());
	}
	
	public abstract boolean isSixSidedFacing();
	
	public short setNextRotation()
	{
		return FacingUtil.getNextFacing(rotation, isSixSidedRotation());
	}
	
	public boolean isSixSidedRotation()
	{
		return false;
	}
	
	@Override
	public void onIconMakerLoading()
	{
		super.onIconMakerLoading();
		facing = 3;
	}
	
	@Override
	public void onPlaced(int facing)
	{
		if(!isSixSidedFacing() && facing < 2)
		{
			facing = 2;
		}
		setFacing((short)facing);
		if(this instanceof ITemplateProvider)
		{
			ITemplateProvider provider = (ITemplateProvider)this;
			provider.getTemplate().setupFacing(facing);
		}
	}
	
	@Override
	public void onAdvPlacing(int rotation, int facing)
	{
		super.onAdvPlacing(rotation, facing);
		if(hasRotation())
		{
			if(!isSixSidedRotation() && rotation < 2)
			{
				setRotation((short)rotation);
			}
		}
	}
	
	public boolean hasRotation()
	{
		return false;
	}
}
