package speiger.src.spmodapi.common.util.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.common.world.blocks.BlockPosition;

public class BlockPositionList extends ArrayList<BlockPosition>
{
	public BlockPositionList()
	{
		
	}
	
	public BlockPositionList(NBTTagCompound nbt)
	{
		this.readFromNBT(nbt);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.size(); i++)
		{
			BlockPosition pos = this.get(i);
			List<Integer> ints = pos.getAsList();
			NBTTagIntArray array = new NBTTagIntArray("Data", new int[] { ints.get(0), ints.get(1), ints.get(2), ints.get(3) });
			list.appendTag(array);
		}
		
		nbt.setTag("Pos", list);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Pos");
		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagIntArray array = (NBTTagIntArray) list.tagAt(i);
			int[] data = array.intArray;
			this.add(new BlockPosition(data[0], data[1], data[2], data[3]));
		}
	}
	
}
