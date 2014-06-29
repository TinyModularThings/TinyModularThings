package speiger.src.spmodapi.common.util.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;

public class StructureStorage
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
	}
	
	public void readStructureDataFromNBT(BlockPosition corepos, NBTTagCompound nbt)
	{
		NBTTagList dataStorage = nbt.getTagList("StructureNBT");
		if(dataStorage.tagCount() <= 0 || corepos == null)
		{
			return;
		}
		cores.put(corepos.getAsList(), new ArrayList<List<Integer>>());
		for(int i = 0;i<dataStorage.tagCount();i++)
		{
			NBTTagIntArray array = (NBTTagIntArray) dataStorage.tagAt(i);
			List<Integer> coords = Arrays.asList(array.intArray[0], array.intArray[1], array.intArray[2], array.intArray[3]);
			structures.put(coords, corepos.getAsList());
			cores.get(corepos.getAsList()).add(coords);
		}
	}
	
	public void writeStructureDataToNBT(BlockPosition corepos, NBTTagCompound nbt)
	{
		NBTTagList dataStorage = new NBTTagList();
		ArrayList<List<Integer>> lists = cores.get(corepos.getAsList());
		if(lists != null)
		{
			for(int i = 0;i<lists.size();i++)
			{
				List<Integer> list = lists.get(i);
				NBTTagIntArray data = new NBTTagIntArray("Info", new int[]{list.get(0), list.get(1), list.get(2), list.get(3)});
				dataStorage.appendTag(data);
			}
			nbt.setTag("StructureNBT", dataStorage);
		}
	}
	
}
