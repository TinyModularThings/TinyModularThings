package speiger.src.tinymodularthings.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import speiger.src.api.nbt.DataStorage;
import speiger.src.api.nbt.DataStorage.LoadingType;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.util.SpmodMod;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.transport.TinyHopper;

/**
 * 
 * @Author Speiger
 *
 * This system is just a backup System for Hoppers on ServerCrash because minecraft makes no Backups.
 *
 */
public class HopperBackupSystem implements INBTReciver
{
	public LoadingType loadingState = LoadingType.NotLoaded;
	
	private static HopperBackupSystem system;
	
	private HashMap<List<Integer>, NBTTagCompound> data = new HashMap<List<Integer>, NBTTagCompound>();
	private ArrayList<List<Integer>> waiters = new ArrayList<List<Integer>>();
	
	public static HopperBackupSystem getSystem()
	{
		if(system == null)
		{
			system = new HopperBackupSystem();
		}
		return system;
	}
	
	public HopperBackupSystem()
	{
		DataStorage.registerNBTReciver(this);
	}
	
	public void backupData(TinyHopper par1)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		par1.writeToNBT(nbt);
		data.put(par1.getPosition().getAsList(), nbt);
	}
	
	public void removeBackupData(TinyHopper par1)
	{
		if(data.containsKey(par1.getPosition().getAsList()))
		{
			data.remove(par1.getPosition().getAsList());
		}
	}
	
	public void requestReloadFromBackup(TinyHopper par1)
	{
		if(loadingState != LoadingType.Loaded)
		{
			waiters.add(par1.getPosition().getAsList());
			return;
		}
		if(waiters.size() > 0)
		{
			for(List<Integer> wait : waiters)
			{
				World world = DimensionManager.getWorld(wait.get(0));
				if(world != null)
				{
					NBTTagCompound nbt = data.get(wait);
					TileEntity tile = world.getBlockTileEntity(wait.get(1), wait.get(2), wait.get(3));
					if(tile != null && nbt != null)
					{
						tile.readFromNBT(nbt);
					}
				}
			}
			waiters.clear();
		}
		NBTTagCompound nbt = data.get(par1.getPosition().getAsList());
		if(nbt != null)
		{
			par1.readFromNBT(nbt);
		}
	}

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{		
		loadingState = LoadingType.Loading;
		data.clear();
		NBTTagList list = par1.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound nbt = (NBTTagCompound)list.tagAt(i);
			
			int[] par2 = nbt.getIntArray("Key");
			NBTTagCompound value = nbt.getCompoundTag("Value");
			List<Integer> key = Arrays.asList(par2[0], par2[1], par2[2], par2[3]);
			data.put(key, value);
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		Iterator<Entry<List<Integer>, NBTTagCompound>> iter = data.entrySet().iterator();
		for(;iter.hasNext();)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			Entry<List<Integer>, NBTTagCompound> next = iter.next();
			List<Integer> key = next.getKey();
			NBTTagCompound value = next.getValue();
			nbt.setIntArray("Key", new int[]{key.get(0), key.get(1), key.get(2), key.get(3)});
			nbt.setCompoundTag("Value", value);
			list.appendTag(nbt);
		}
		par1.setTag("Data", list);
	}

	@Override
	public void finishLoading()
	{
		loadingState = LoadingType.Loaded;
	}

	@Override
	public SpmodMod getOwner()
	{
		return TinyModularThings.instance;
	}

	@Override
	public String getID()
	{
		return "HopperBackup";
	}
	
}
