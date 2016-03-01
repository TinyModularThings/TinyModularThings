package speiger.src.api.common.data.nbt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import speiger.src.api.common.core.SpmodMod;
import speiger.src.api.common.core.SpmodModRegistry;
import speiger.src.api.common.data.nbt.INBTListener.LoadingState;
import cpw.mods.fml.common.FMLLog;

public class NBTStorage
{
	private static Map<SpmodMod, List<INBTListener>> listeners = new HashMap<SpmodMod, List<INBTListener>>();
	private static List<SpmodMod> requestedUpdates = new LinkedList<SpmodMod>();
	
	public static void registerNBTListener(INBTListener par1)
	{
		if(SpmodModRegistry.isAddonRegistered(par1.getOwner()))
		{
			List<INBTListener> listener = listeners.get(par1.getOwner());
			if(listener == null)
			{
				listener = new ArrayList<INBTListener>();
			}
			listener.add(par1);
			listeners.put(par1.getOwner(), listener);
		}
	}
	
	public static boolean removeNBTListener(INBTListener par1)
	{
		List<INBTListener> listener = listeners.get(par1.getOwner());
		if(listener == null)
		{
			return false;
		}
		return listener.remove(par1);
	}
	
	public static void markDirty(SpmodMod par1)
	{
		requestedUpdates.add(par1);
	}
	
	private static INBTListener getReciverFromModAndID(SpmodMod par1, String id)
	{
		List<INBTListener> list = listeners.get(par1);
		if (list != null)
		{
			for (INBTListener cu : list)
			{
				if (cu.getID().equalsIgnoreCase(id))
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
		if (!server.isDedicatedServer())
		{
			path += "saves/";
		}
		path += server.getFolderName() + "/spmod";
		
		FMLLog.getLogger().info("Start Loading Data");
		try
		{
			for(SpmodMod mod : listeners.keySet())
			{
				try
				{
					File file = new File(path, mod.getModName()+"_data.dat");
					if(file.exists() && file.isFile())
					{
						InputStream fileStream = new DataInputStream(new FileInputStream(file));
						byte[] data = new byte[fileStream.available()];
						fileStream.read(data);
						fileStream.close();
						DataInputStream stream = new DataInputStream(new GZIPInputStream(new ByteArrayInputStream(data)));
						
						NBTTagCompound nbt = CompressedStreamTools.read(stream);
						SpmodMod fileMod = SpmodModRegistry.getModFromName(nbt.getString("ID"));
						if(SpmodModRegistry.areAddonsEqual(mod, fileMod))
						{
							NBTTagList list = nbt.getTagList("Data", 10);
							for(int i = 0;i<list.tagCount();i++)
							{
								NBTTagCompound fileData = list.getCompoundTagAt(i);
								INBTListener receiver = getReciverFromModAndID(mod, fileData.getString("ID"));
								receiver.setLoadingState(LoadingState.Loading);
								try
								{
									receiver.readFromNBT(fileData.getCompoundTag("Data"));
									receiver.setLoadingState(LoadingState.Loaded);
								}
								catch(Exception e)
								{
									FMLLog.getLogger().info("The NBTListener from: "+mod.getModName()+" Crashed on data loading. Listener ID: "+receiver.getID());
									receiver.setLoadingState(LoadingState.FailedLoading);
									e.printStackTrace();
								}
							}
						}
						stream.close();
					}	
				}
				catch(Exception e)
				{
					FMLLog.getLogger().info("The Mod: "+mod.getModName()+" Could not load his data. Why Ever");
					e.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			FMLLog.getLogger().info("Something happend that the loading Progress is stopped");
			e.printStackTrace();
		}
		for(List<INBTListener> list : listeners.values())
		{
			for(INBTListener nbt : list)
			{
				nbt.setLoadingState(LoadingState.Done);
			}
		}
		FMLLog.getLogger().info("Finished Loading Data");
	}
	
	public static void write(MinecraftServer server, boolean forceAll, boolean override)
	{
		if(server == null)
		{
			return;
		}
		String path = "";
		if (!server.isDedicatedServer())
		{
			path += "saves/";
		}
		path += server.getFolderName() + "/spmod";
		File paths = new File(path);
		
		if (!paths.exists() || !paths.isDirectory())
		{
			if (!paths.mkdir())
			{
				return;
			}
		}
		if (forceAll)
		{
			requestedUpdates.clear();
			Iterator<SpmodMod> keys = listeners.keySet().iterator();
			while (keys.hasNext())
			{
				requestedUpdates.add(keys.next());
			}
		}
		if(requestedUpdates.isEmpty() || (server.worldServers[0].levelSaving && !override))
		{
			return;
		}
		
		Map<SpmodMod, NBTTagCompound> data = new HashMap<SpmodMod, NBTTagCompound>();
		
		for(SpmodMod mod : requestedUpdates)
		{
			NBTTagList list = new NBTTagList();
			List<INBTListener> targets = listeners.get(mod);
			for(INBTListener cu : targets)
			{
				try
				{
					cu.setLoadingState(forceAll ? LoadingState.Saving : LoadingState.RequestedSaving);
					NBTTagCompound nbt = new NBTTagCompound();
					cu.writeToNBT(nbt);
					NBTTagCompound key = new NBTTagCompound();
					key.setString("ID", cu.getID());
					key.setTag("Data", nbt);
					list.appendTag(key);
				}
				catch(Exception e)
				{
					cu.setLoadingState(LoadingState.FailedSaving);
					FMLLog.getLogger().info("A NBTListener crashed on Saving: Owner: "+mod.getModName()+" ListenerID: "+cu.getID());
					e.printStackTrace();
				}
			}
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("ID", mod.getModName());
			nbt.setTag("Data", list);
			data.put(mod, nbt);
		}
		requestedUpdates.clear();
		try
		{
			for(Entry<SpmodMod, NBTTagCompound> entry : data.entrySet())
			{
				SpmodMod mod = entry.getKey();
				NBTTagCompound nbt = entry.getValue();
				File file = new File(path, entry.getKey().getModName()+"_data.dat");
				if(!file.exists())
				{
					try
					{
						if(!file.createNewFile())
						{
							FMLLog.getLogger().info("Could not create File for saving. Skipping Mod: "+mod.getModName());
							continue;
						}
					}
					catch(Exception e)
					{
						FMLLog.getLogger().info("Could not create File for saving. Skipping Mod: "+mod.getModName());
						e.printStackTrace();
						continue;
					}
				}
				if(file.exists() && file.isFile())
				{
					//Stupid complex made... Yes but makes it less hacky for people that are using TextEditors.. You know...
					ByteArrayOutputStream byteStorage = new ByteArrayOutputStream();
					DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(byteStorage));
					CompressedStreamTools.write(nbt, stream);
					stream.close();
					byte[] totalBytes = byteStorage.toByteArray();
					OutputStream fileStream = new DataOutputStream(new FileOutputStream(file));
					fileStream.write(totalBytes);
					fileStream.close();
				}
			}
		}
		catch(Exception e)
		{
			FMLLog.getLogger().info("Saving of the Data Failed");
			e.printStackTrace();
		}
		
		for(List<INBTListener> list : listeners.values())
		{
			for(INBTListener nbt : list)
			{
				nbt.setLoadingState(LoadingState.Done);
			}
		}
		FMLLog.getLogger().info("Finished Saving Data");
	}
	
	public static boolean hasSavingRequests()
	{
		return requestedUpdates.size() > 0;
	}
	
	
}
