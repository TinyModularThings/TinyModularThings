package speiger.src.api.common.utils.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.common.utils.misc.saveHandlers.IDataSaver;

public class SaveableList<T> implements Iterable
{
	List<T> dataList = new ArrayList<T>();
	IDataSaver dataSaver;
	
	
	public SaveableList(Class<? extends IDataSaver<T>> handler)
	{
		try
		{
			dataSaver = handler.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void addObject(T par1)
	{
		dataList.add(par1);
	}
	
	public void addAllObjects(List<T> par1)
	{
		dataList.addAll(par1);
	}
	
	public boolean contains(T par1)
	{
		return dataList.contains(par1);
	}
	
	public T get(int i)
	{
		return dataList.get(i);
	}
	
	public T remove(int i)
	{
		return dataList.remove(i);
	}
	
	public int size()
	{
		return dataList.size();
	}
	
	public boolean hasData()
	{
		return size() > 0;
	}
	
	public boolean isEmpty()
	{
		return size() == 0;
	}
	
	public void remove(T par1)
	{
		dataList.remove(par1);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return dataList.iterator();
	}
	
	public void clear()
	{
		dataList.clear();
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		clear();
		dataList.addAll(dataSaver.readFromNBT(nbt));
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		dataSaver.writeToNBT(dataList, nbt);
	}

}
