package speiger.src.spmodapi.common.util.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.data.utils.IDataInfo;
import speiger.src.api.common.data.utils.IStackInfo;
import speiger.src.spmodapi.SpmodAPI;

public class ClassStorage
{
	private static ClassStorage instance = new ClassStorage();
	
	public static ClassStorage getInstance()
	{
		return instance;
	}
	
	private HashMap<String, IStackInfo> itemBlocks = new HashMap<String, IStackInfo>();
	private HashMap<String, IDataInfo> objects = new HashMap<String, IDataInfo>();
	private HashSet<String> requestedInits = new HashSet<String>();
	
	public void requestItem(String id, Class clz, String objectName)
	{
		itemBlocks.put(id, new DataStack(clz, objectName));
	}
	
	public void requestItem(Class clz, List<String> ids, List<String> searchedObjects)
	{
		if(ids.size() != searchedObjects.size())
		{
			SpmodAPI.log.print("Someone requested some weird stuff");
			return;
		}
		for(int i = 0;i<ids.size();i++)
		{
			requestItem(ids.get(i), clz, searchedObjects.get(i));
		}
	}
	
	public IDataInfo getData(String id)
	{
		return objects.get(id);
	}
	
	public IDataInfo removeData(String id)
	{
		requestedInits.remove(id);
		return objects.remove(id);
	}
	
	public IStackInfo getStack(String id)
	{
		return itemBlocks.get(id);
	}
	
	public IStackInfo removeStack(String id)
	{
		return itemBlocks.remove(id);
	}
	
	public void addRequestData(String id, IDataInfo data, boolean requestInitOnWorldLoad)
	{
		objects.put(id, data);
		if(requestInitOnWorldLoad)
		{
			if(!requestedInits.contains(id))
			{
				requestedInits.add(id);
			}
		}
	}
	
	public void onWorldLoad()
	{
		if(requestedInits.isEmpty() || objects.isEmpty())
		{
			return;
		}
		Iterator<String> iter = requestedInits.iterator();
		for(;iter.hasNext();)
		{
			objects.get(iter.next()).initData();
		}
	}
	
	public static class DataStack implements IStackInfo
	{
		Class par1;
		String par2;
		ItemStack result;
		
		public DataStack(Class clz, String id)
		{
			par1 = clz;
			par2 = id;
		}
		
		@Override
		public ItemStack getResult()
		{
			if(result == null)
			{
				try
				{
					Object obj = par1.getField(par2).get(null);
					if(obj != null)
					{
						if(obj instanceof Item)
						{
							result = new ItemStack((Item)obj, 1, 0);
						}
						else if(obj instanceof Block)
						{
							result = new ItemStack((Block)obj, 1, 0);
						}
						else if(obj instanceof ItemStack)
						{
							result = ((ItemStack)obj).copy();
						}
					}
				}
				catch(Exception e)
				{
					result = new ItemStack(Block.bedrock);
				}
			}
			return result;
		}
		
	}
}
