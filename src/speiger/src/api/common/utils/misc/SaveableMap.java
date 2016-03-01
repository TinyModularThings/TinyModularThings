package speiger.src.api.common.utils.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.utils.misc.saveHandlers.IDataSaver;

public class SaveableMap<K, V> implements Iterable
{
	Map<K, V> data = new HashMap<K, V>();
	IDataSaver keySave;
	IDataSaver valueSave;
	
	public SaveableMap(Class<? extends IDataSaver<K>> key, Class<? extends IDataSaver<V>> value)
	{
		try
		{
			keySave = key.newInstance();
			valueSave = value.newInstance();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void put(K par1, V par2)
	{
		data.put(par1, par2);
	}
	
	public void putAll(Map<K, V> par1)
	{
		data.putAll(par1);
	}
	
	private void load(List<K> par1, List<V> par2)
	{
		if(par1.size() != par2.size())
		{
			return;
		}
		for(int i = 0;i<par1.size();i++)
		{
			put(par1.get(i), par2.get(i));
		}
	}
	
	public V get(K par1)
	{
		return data.get(par1);
	}
	
	public V remove(K par1)
	{
		return data.remove(par1);
	}
	
	public void clear()
	{
		data.clear();
	}
	
	public boolean containsKey(K par1)
	{
		return data.containsKey(par1);
	}
	
	public boolean containsValue(V par1)
	{
		return data.containsValue(par1);
	}
	
	public Set<K> getKeys()
	{
		return data.keySet();
	}
	
	public Collection<V> getValues()
	{
		return data.values();
	}

	@Override
	public Iterator<Entry<K, V>> iterator()
	{
		return data.entrySet().iterator();
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		clear();
		NBTTagCompound key = nbt.getCompoundTag("Keys");
		NBTTagCompound value = nbt.getCompoundTag("Values");
		List<K> keys = keySave.readFromNBT(key);
		List<V> values = valueSave.readFromNBT(value);
		load(keys, values);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		NBTTagCompound key = new NBTTagCompound();
		NBTTagCompound value = new NBTTagCompound();
		List<K> keyData = new ArrayList<K>();
		keyData.addAll(data.keySet());
		List<V> valueData = new ArrayList<V>();
		valueData.addAll(data.values());
		keySave.writeToNBT(keyData, key);
		valueSave.writeToNBT(valueData, value);
		nbt.setTag("Keys", key);
		nbt.setTag("Values", value);
	}
	
	
}
