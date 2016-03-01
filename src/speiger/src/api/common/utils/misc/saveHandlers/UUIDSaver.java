package speiger.src.api.common.utils.misc.saveHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class UUIDSaver implements IDataSaver<UUID>
{

	@Override
	public void writeToNBT(List<UUID> par1, NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0;i < par1.size();i++)
		{
			NBTTagCompound data = new NBTTagCompound();
			UUID id = par1.get(i);
			data.setLong("Most", id.getMostSignificantBits());
			data.setLong("Least", id.getLeastSignificantBits());
			list.appendTag(data);
		}
		nbt.setTag("Data", list);
	}

	@Override
	public List<UUID> readFromNBT(NBTTagCompound par1)
	{
		List<UUID> ids = new ArrayList<UUID>();
		NBTTagList list = par1.getTagList("Data", 10);
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			ids.add(new UUID(nbt.getLong("Most"), nbt.getLong("Least")));
		}
		return ids;
	}
	
}
