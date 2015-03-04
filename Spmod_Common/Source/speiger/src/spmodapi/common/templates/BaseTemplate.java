package speiger.src.spmodapi.common.templates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.util.data.StructureStorage;

public abstract class BaseTemplate implements ITemplate
{
	public StructureStorage storage = StructureStorage.instance;
	
	public static BlockStack item;
	public static BlockStack fluid;
	public static BlockStack energy;
	private BlockPosition target;
	private ArrayList<BlockPosition> structure = new ArrayList<BlockPosition>();
	
	
	public BaseTemplate(IAdvTile par1)
	{
		target = par1.getPosition();
	}
	
	public BlockStack getItemInterface()
	{
		if(item == null)
		{
			item = new BlockStack();
		}
		return item;
	}
	
	public BlockStack getFluidInterface()
	{
		if(fluid == null)
		{
			fluid = new BlockStack();
		}
		return fluid;
	}
	
	public BlockStack getEnergyInterface()
	{
		if(energy == null)
		{
			energy = new BlockStack();
		}
		return energy;
	}
	
	public boolean isInterface(BlockStack toCompare)
	{
		if(toCompare.match(getItemInterface()) || toCompare.match(getFluidInterface()) || toCompare.match(getEnergyInterface()))
		{
			return true;
		}
		return false;
	}
	
	public void addToStorage(List<BlockPosition> par1)
	{
		for(BlockPosition pos : par1)
		{
			if(!storage.isRegistered(pos))
			{
				storage.registerStorage(target, pos);
			}
			if(!structure.contains(pos))
			{
				structure.add(pos);
			}
		}
	}
	
	public void removeFromStorage(List<BlockPosition> par1)
	{
		for(BlockPosition pos : par1)
		{
			storage.removePosition(pos);
		}
		storage.removeCore(getCore());
	}
	
	public boolean checkStorage(List<BlockPosition> oldPos)
	{
		List<BlockPosition> pos = new ArrayList<BlockPosition>(oldPos);
		removeAirBlocks(pos);
		boolean result = true;
		int i = 0;
		for(BlockPosition cu : pos)
		{
			boolean me = storage.isRegisteredToMe(target, cu);
			if(storage.isRegistered(cu) && !me)
			{
				result = false;
				break;
			}
			i++;
		}
		return result;
	}
	
	public void removeAirBlocks(List<BlockPosition> pos)
	{
		for(int i = 0;i<pos.size();i++)
		{
			BlockPosition cu = pos.get(i);
			if(cu.isBlockAir())
			{
				pos.remove(i--);
			}
		}
	}
	
	public List<BlockPosition> getStructure()
	{
		return structure;
	}

	@Override
	public int getTotalPatternSize()
	{
		return structure.size();
	}

	@Override
	public BlockPosition getCore()
	{
		return target;
	}

	@Override
	public void onBreaking()
	{
		removeFromStorage(structure);
		structure.clear();
	}

	@Override
	public void onUnload()
	{
		structure.clear();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		structure.clear();
		NBTTagList list = nbt.getTagList("Structure");
		for(int i = 0;i<list.tagCount();i++)
		{
			int[] data = ((NBTTagIntArray)list.tagAt(i)).intArray;
			BlockPosition pos = new BlockPosition(Arrays.asList(data[0], data[1], data[2], data[3]));
			structure.add(pos);
		}
		int[] array = nbt.getIntArray("CorePos");
		target = new BlockPosition(Arrays.asList(array[0], array[1], array[2], array[3]));
		
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(BlockPosition pos : structure)
		{
			List<Integer> data = pos.getAsList();
			list.appendTag(new NBTTagIntArray("Data", new int[]{data.get(0), data.get(1), data.get(2), data.get(3)}));
		}
		nbt.setTag("Structure", list);
		List<Integer> core = target.getAsList();
		nbt.setIntArray("CorePos", new int[]{core.get(0), core.get(1), core.get(2), core.get(3)});
	}
	
	
}
