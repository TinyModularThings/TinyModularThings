package speiger.src.spmodapi.common.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import speiger.src.api.nbt.DataStorage;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;

public class StructureStorage implements INBTReciver
{
	public static StructureStorage instance = new StructureStorage();
	
	private HashMap<List<Integer>, List<Integer>> structures = new HashMap<List<Integer>, List<Integer>>();
	private HashMap<List<Integer>, ArrayList<List<Integer>>> cores = new HashMap<List<Integer>, ArrayList<List<Integer>>>();
	
	public void registerStorage(BlockPosition core, BlockPosition pos)
	{
		structures.put(pos.getAsList(), core.getAsList());
		if(cores.get(core.getAsList()) == null)
		{
			ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
			lists.add(pos.getAsList());
			cores.put(core.getAsList(), lists);
			return;
		}
		cores.get(core.getAsList()).add(pos.getAsList());
	}
	
	public boolean isRegistered(BlockPosition pos)
	{
		return structures.get(pos.getAsList()) != null;
	}
	
	public boolean isRegisteredToMe(BlockPosition core, BlockPosition pos)
	{
		List<Integer> list = structures.get(pos);
		if (list == null)
		{
			return false;
		}
		
		BlockPosition end = new BlockPosition(list.get(0), list.get(1), list.get(2), list.get(3));
		
		return end.worldID == core.worldID && end.xCoord == core.xCoord && end.yCoord == core.yCoord && end.zCoord == core.zCoord;
	}
	
	public BlockPosition getCorePosition(BlockPosition pos)
	{
		List<Integer> list = structures.get(pos.getAsList());
		
		if (list == null)
		{
			return null;
		}
		
		return new BlockPosition(list.get(0), list.get(1), list.get(2), list.get(3));
	}
	
	
	public void removePosition(BlockPosition pos)
	{
		structures.remove(pos.getAsList());
	}
	
	
	
	
	
	
	@ForgeSubscribe
	public void onClickBlock(PlayerInteractEvent evt)
	{
		if (!evt.entity.worldObj.isRemote && evt.action == Action.RIGHT_CLICK_BLOCK && evt.entityPlayer.getCurrentEquippedItem() == null)
		{
			BlockPosition pos = new BlockPosition(evt.entityPlayer.worldObj, evt.x, evt.y, evt.z);
			if (isRegistered(pos))
			{
				BlockPosition core = getCorePosition(pos);
				if (core != null && core.doesBlockExsist())
				{
					TileEntity tile = core.getTileEntity();
					if (tile != null && tile instanceof AdvTile)
					{
						if (!((AdvTile) tile).hasContainer())
						{
							return;
						}
					}
					
					if (core.getAsBlockStack().getBlock().onBlockActivated(evt.entity.worldObj, core.xCoord, core.yCoord, core.zCoord, evt.entityPlayer, 0, 0, 0, 0))
					{
						evt.useItem = Result.DENY;
					}
				}
			}
		}
	}
	

	public static void registerForgeEvent()
	{
		MinecraftForge.EVENT_BUS.register(instance);
		DataStorage.registerNBTReciver(instance);
	}
	
	
	//Loading and Writing to data
	private void addStructure(List<Integer> key, List<Integer> val)
	{
		if(key != null && val != null && key.size() == 4 && val.size() == 4)
		{
			structures.put(val, key);
			if(cores.get(key) == null)
			{
				ArrayList<List<Integer>> lists = new ArrayList<List<Integer>>();
				lists.add(val);
				cores.put(key, lists);
				return;
			}
			cores.get(key).add(val);
		}
	}
	



	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		HashMap<List<Integer>, List<Integer>> backupStructures = new HashMap<List<Integer>, List<Integer>>();
		HashMap<List<Integer>, ArrayList<List<Integer>>> backupCores = new HashMap<List<Integer>, ArrayList<List<Integer>>>();
		
		backupStructures.putAll(structures);
		backupCores.putAll(cores);
		
		structures.clear();
		cores.clear();
		
		NBTTagList list = par1.getTagList("Structures");
		
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
			int[] key = tag.getIntArray("Key");
			int[] value = tag.getIntArray("Value");
			
			addStructure(Arrays.asList(key[0], key[1], key[2], key[3]), Arrays.asList(value[0], value[1], value[2], value[3]));
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound nbt)
	{
		Iterator<Entry<List<Integer>, List<Integer>>> keys = structures.entrySet().iterator();
		NBTTagList tags = new NBTTagList();
		while(keys.hasNext())
		{
			Entry<List<Integer>, List<Integer>> cu = keys.next();
			NBTTagCompound cuNBT = new NBTTagCompound();
			//Switching around because of the reading is the same. Shouldnt be happening but who cares!
			int[] key = new int[]{cu.getValue().get(0), cu.getValue().get(1), cu.getValue().get(2), cu.getValue().get(3)};
			int[] val = new int[]{cu.getKey().get(0), cu.getKey().get(1), cu.getKey().get(2), cu.getKey().get(3)};
			cuNBT.setIntArray("Key", key);
			cuNBT.setIntArray("Value", val);
			tags.appendTag(cuNBT);
		}
		nbt.setTag("Structures", tags);		
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "Structure.Storage";
	}
	
}
