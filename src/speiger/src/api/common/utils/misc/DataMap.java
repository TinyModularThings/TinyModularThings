package speiger.src.api.common.utils.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DataMap<T>
{
	Map<String, T> data = new HashMap<String, T>();
	
	public void addData(String par1, T par2)
	{
		data.put(par1, par2);
	}
	
	public T getData(String par1)
	{
		return data.get(par1);
	}
	
	public boolean containsData(String par1)
	{
		return data.containsKey(par1);
	}
	
	public Set<Entry<String, T>> entrySet()
	{
		return data.entrySet();
	}
	
	public void remove(String par1)
	{
		data.remove(par1);
	}
	
	public void clear()
	{
		data.clear();
	}
}
