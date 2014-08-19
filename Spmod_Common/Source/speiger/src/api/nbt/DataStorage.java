package speiger.src.api.nbt;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import cpw.mods.fml.common.FMLLog;

public class DataStorage
{
	static HashMap<SpmodMod, ArrayList<INBTReciver>> nbtStorage = new HashMap<SpmodMod, ArrayList<INBTReciver>>();
	static ArrayList<SpmodMod> requestedUpdates = new ArrayList<SpmodMod>();
	
	
	public static void registerNBTReciver(INBTReciver par1)
	{
		if(SpmodModRegistry.isModRegistered(par1.getOwner()))
		{
			ArrayList<INBTReciver> nbts = nbtStorage.get(par1.getOwner());
			if(nbts == null)
			{
				nbts = new ArrayList<INBTReciver>();
			}
			nbts.add(par1);
			
			nbtStorage.put(par1.getOwner(), nbts);
		}
	}
	
	public static boolean removeNBTReciver(INBTReciver par1)
	{
		ArrayList<INBTReciver> nbts = nbtStorage.get(par1.getOwner());
		if(nbts == null)
		{
			return false;
		}
		
		return nbts.remove(par1);
	}
	
	private static INBTReciver getReciverFromModAndID(SpmodMod par1, String id)
	{
		ArrayList<INBTReciver> list = nbtStorage.get(par1);
		if(list != null)
		{
			for(INBTReciver cu : list)
			{
				if(cu.getID().equalsIgnoreCase(id))
				{
					return cu;
				}
			}
		}
		return null;
	}
	
	public static void read(MinecraftServer server)
	{
		if(server == null)
		{
			return;
		}
		String path = "";
		if(!server.isDedicatedServer())
		{
			path+="saves/";
		}
		path+=server.getFolderName()+"/spmod";
		
		FMLLog.getLogger().info("Start Loading Data");
		
		try
		{
			SpmodMod[] mods = nbtStorage.keySet().toArray(new SpmodMod[nbtStorage.keySet().size()]);
			for(int i = 0;i<mods.length;i++)
			{
				SpmodMod mod = mods[i];
				
				File file = new File(path, mod.getName()+"_data.dat");
				
				if(file.exists() && file.isFile())
				{
					DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
					
					NBTTagCompound nbt = (NBTTagCompound) NBTBase.readNamedTag(stream);
					
					SpmodMod fileMod = SpmodModRegistry.getModFromName(nbt.getString("Key"));
					
					if(SpmodModRegistry.areModsEqual(mod, fileMod) && SpmodModRegistry.isModRegistered(mod))
					{
						NBTTagList list = nbt.getTagList("Value");
						for(int z = 0;z<list.tagCount();z++)
						{
							NBTTagCompound cu = (NBTTagCompound) list.tagAt(z);
							INBTReciver re = getReciverFromModAndID(mod, cu.getString("ID"));
							if(re != null)
							{
								re.loadFromNBT(cu);
							}
							
						}
					}
					
					stream.close();
				}
			}
			
			for(SpmodMod mod : mods)
			{
				ArrayList<INBTReciver> data = nbtStorage.get(mod);
				if(data != null)
				{
					for(int i = 0;i<data.size();i++)
					{
						INBTReciver nbt = data.get(i);
						nbt.finishLoading();
					}
				}
			}
		}
		catch (Exception e)
		{
		}
		
		FMLLog.getLogger().info("Start Loading Data");
	}
	
	public static void write(MinecraftServer server, boolean forceAll)
	{
		if(server == null)
		{
			return;
		}
		String path = "";
		if(!server.isDedicatedServer())
		{
			path+="saves/";
		}
		path+=server.getFolderName()+"/spmod";
		File paths = new File(path);
		
		if(!paths.exists() || !paths.isDirectory())
		{
			if(!paths.mkdir())
			{
				return;
			}
		}
		
		FMLLog.getLogger().info("Start Saving Data");
		
		if(forceAll)
		{
			requestedUpdates.clear();
			Iterator<SpmodMod> keys = nbtStorage.keySet().iterator();
			while(keys.hasNext())
			{
				requestedUpdates.add(keys.next());
			}
		}
		
		if(requestedUpdates.isEmpty())
		{
			return;
		}
		
		HashMap<String, NBTTagCompound> data = new HashMap<String, NBTTagCompound>();
		
		for(int z = 0;z<requestedUpdates.size();z++)
		{
			SpmodMod mod = requestedUpdates.get(z);
			
			NBTTagList list = new NBTTagList();
			ArrayList<INBTReciver> start = nbtStorage.get(mod);
			
			for(int i = 0;i<start.size();i++)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				start.get(i).saveToNBT(nbt);
				nbt.setString("ID", start.get(i).getID());
				list.appendTag(nbt);
				
			}
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("Value", list);
			nbt.setString("Key", mod.getName());
			
			data.put(mod.getName(), nbt);
		}
		
		requestedUpdates.clear();
		
		try
		{
			String[] keys = data.keySet().toArray(new String[data.keySet().size()]);
			for(int i = 0;i<keys.length;i++)
			{
				File file = new File(path, keys[i]+"_data.dat");
				
				if(!file.exists())
				{
					try
					{
						if(!file.createNewFile())
						{
							continue;
						}
					}
					catch (Exception e)
					{
						FMLLog.getLogger().info("Could not Create "+keys[i]+" Saving File");
						continue;
					}
				}
				
				if(file.exists() && file.isFile())
				{
					DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
					
					NBTTagCompound value = data.get(keys[i]);
					if(value != null)
					{
						NBTBase.writeNamedTag(value, stream);
					}
					
					stream.close();
				}
			}
		}
		catch (Exception e)
		{
			FMLLog.getLogger().info("The Data Saving Failed. Why Ever...");
		}
		
		FMLLog.getLogger().info("Finish Saving Data");
	}
	
	public static boolean hasRequest()
	{
		return requestedUpdates.size() > 0;
	}
	
	public static enum LoadingType
	{
		NotLoaded,
		Loading,
		Loaded;
	}
}
