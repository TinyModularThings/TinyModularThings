package speiger.src.tinymodularthings.common.utils.fluids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import speiger.src.api.nbt.DataStorage;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.Ticks;
import speiger.src.api.util.Ticks.ITickReader;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.blocks.storage.TinyTank;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

public class FluidStorage implements ITickReader, INBTReciver
{
	static HashMap<List<Integer>, SharedFluidTank> tanks = new HashMap<List<Integer>, SharedFluidTank>();
	static HashMap<SharedFluidTank, ArrayList<List<Integer>>> tankKey = new HashMap<SharedFluidTank, ArrayList<List<Integer>>>();
	static HashMap<List<Integer>, List<Integer>> key = new HashMap<List<Integer>, List<Integer>>();
	static HashMap<List<Integer>, List<Integer>> value = new HashMap<List<Integer>, List<Integer>>();
	
	public static FluidStorage instance;
	
	static int downDelay = 0;
	static int adjustDelay = 0;
	static long minTime = 0;
	
	public FluidStorage()
	{
		instance = this;
		DataStorage.registerNBTReciver(this);
		Ticks.registerTickReciver(this);
	}
	
	public void addTank(TinyTank par1)
	{
		BlockPosition pos = par1.getPosition();
		if(tanks.get(pos.getAsList()) != null)
		{
			TinyModularThings.log.print("Adding 2 Tanks at 1 Position. Please look for errors");
			return;
		}
		
		SharedFluidTank tank = new SharedFluidTank(par1);
		
		tanks.put(pos.getAsList(), tank);
		ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
		list.add(pos.getAsList());
		tankKey.put(tank, list);
		
		this.addTankToOthers(par1);
	}
	
	public void removeTank(TinyTank par1)
	{
		BlockPosition pos = par1.getPosition();
		SharedFluidTank tank = tanks.remove(pos.getAsList());
		if(tank != null)
		{
			tank.removeTank(par1);
			tankKey.remove(tank);
		}
		if(key.get(pos.getAsList()) != null)
		{
			key.remove(pos.getAsList());
		}
		if(value.get(pos.getAsList()) != null)
		{
			value.remove(pos.getAsList());
		}
	}
	
	public void replace(SharedFluidTank par1)
	{
		for(TinyTank tank : par1.getAllTanks())
		{
			tanks.put(tank.getPosition().getAsList(), par1);
			if(tankKey.get(par1) == null)
			{
				ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
				list.add(tank.getPosition().getAsList());
				tankKey.put(par1, list);
			}
			else
			{
				tankKey.get(par1).add(tank.getPosition().getAsList());
			}
		}
	}
	
	private void addTankToOthers(TinyTank par1)
	{
		SharedFluidTank core = tanks.get(par1.getPosition().getAsList());
		for(ForgeDirection dir : WorldReading.getHDirections())
		{
			SharedFluidTank first = tanks.get(par1.getPosition().getPosFromFSide(dir).getAsList());
			if(first != null && !this.isSame(par1.getPosition().getAsList(), par1.getPosition().getPosFromFSide(dir).getAsList()) && core.canAddFluid(first))
			{
				first.addTank(par1);
				this.removeTank(core.getCore());
				tanks.put(par1.getPosition().getAsList(), first);
				if(tankKey.get(first) == null)
				{
					ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
					list.add(par1.getPosition().getAsList());
					tankKey.put(first, list);
				}
				else
				{
					tankKey.get(first).add(par1.getPosition().getAsList());
				}
				break;
			}
		}
		this.syncTanks(par1);
	}
	
	public void syncTanks(TinyTank par1)
	{
		for(ForgeDirection dir : WorldReading.getHDirections())
		{
			SharedFluidTank core = tanks.get(par1.getPosition().getAsList());
			SharedFluidTank next = tanks.get(par1.getPosition().getPosFromFSide(dir).getAsList());
			
			if(next != null && !this.isSame(par1.getPosition().getAsList(), par1.getPosition().getPosFromFSide(dir).getAsList()) && core.canAddFluid(next))
			{
				int first = core.getAllTanks().size();
				int second = next.getAllTanks().size();
				if(first > second)
				{
					core.replace(next);
				}
				else
				{
					next.replace(core);
				}
				FMLLog.getLogger().info("Called");
			}
		}
		FMLLog.getLogger().info("Test: "+tankKey.keySet().size()+":"+tankKey.values().size());
		findFillProgress(par1);
	}
	
	private void findFillProgress(TinyTank par1)
	{
		BlockPosition core = par1.getPosition();
		BlockPosition up = core.getPosFromFSide(ForgeDirection.UP);
		BlockPosition down = core.getPosFromFSide(ForgeDirection.DOWN);
		
		if(tanks.get(up.getAsList()) != null && key.get(up.getAsList()) == null)
		{
			FMLLog.getLogger().info("UP");
			key.put(up.getAsList(), core.getAsList());
			value.put(core.getAsList(), up.getAsList());
		}
		if(tanks.get(down.getAsList()) != null && value.get(down.getAsList()) == null)
		{
			FMLLog.getLogger().info("DOWN");
			key.put(core.getAsList(), down.getAsList());
			value.put(down.getAsList(), core.getAsList());
		}
	}
	
	private void finalize(TinyTank par1)
	{
		SharedFluidTank tank = tanks.get(par1.getPosition().getAsList());
		if(tank != null)
		{
			tank.adjust();
		}
	}
	
	public boolean isSame(List<Integer> par1, List<Integer> par2)
	{
		if(par1.get(0) == par2.get(0) && par1.get(1) == par2.get(1) && par1.get(2) == par2.get(2) && par1.get(3) == par2.get(3))
		{
			return true;
		}
		return false;
	}
	
	public BlockPosition getBottom(BlockPosition par1)
	{
		BlockPosition copy = par1.copy();
		while(true)
		{
			List<Integer> list = key.get(copy.getAsList());
			if(list != null)
			{
				BlockPosition par2 = new BlockPosition(list);
				SharedFluidTank core = tanks.get(par1.getAsList());
				SharedFluidTank test = tanks.get(par2.getAsList());
				if(core.canAddFluid(test))
				{
					copy = par2;
					continue;
				}
			}
			break;
		}
		return copy;
	}
	
	public BlockPosition getTop(BlockPosition par1)
	{
		BlockPosition copy = par1.copy();
		while(true)
		{
			List<Integer> list = value.get(copy.getAsList());
			if(list != null)
			{
				BlockPosition par2 = new BlockPosition(list);
				SharedFluidTank core = tanks.get(par1.getAsList());
				SharedFluidTank test = tanks.get(par2.getAsList());
				if(test != null && test.canAddFluid(core))
				{
					copy = par2;
					continue;
				}
			}
			break;
		}
		return copy;
	}
	
	public int fill(TinyTank tank, FluidStack stack, boolean doFill)
	{
		if(tank == null)
		{
			return 0;
		}
		BlockPosition pos = tank.getPosition();
		if(value.get(pos.getAsList()) != null)
		{
			pos = getTop(pos.copy());
		}
		SharedFluidTank sharedTank = tanks.get(pos.getAsList());
		if(sharedTank == null)
		{
			return 0;
		}
		return sharedTank.fill(stack, doFill);
	}
	
	public FluidStack drain(TinyTank tank, int amount, boolean doDrain)
	{
		if(tank == null)
		{
			return null;
		}
		BlockPosition pos = tank.getPosition();
		if(key.get(pos.getAsList()) != null)
		{
			pos = getBottom(pos.copy());
		}
		SharedFluidTank sharedTank = tanks.get(pos.getAsList());
		if(sharedTank == null)
		{
			return null;
		}
		return sharedTank.drain(amount, doDrain);
	}
	
	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		try
		{
			NBTTagList tank = par1.getTagList("Tank");
			for(int i = 0;i<tank.tagCount();i++)
			{
				NBTTagCompound nbt = (NBTTagCompound) tank.tagAt(i);
				int[] core = nbt.getIntArray("Core");
				BlockPosition posCore = new BlockPosition(Arrays.asList(core[0], core[1], core[2], core[3]));
				SharedFluidTank sTank = new SharedFluidTank((TinyTank) posCore.getTileEntity());
				NBTTagList list = nbt.getTagList("Tanks");
				for(int z = 0;z<list.tagCount();z++)
				{
					NBTTagIntArray array = (NBTTagIntArray) list.tagAt(z);
					int[] data = array.intArray;
					sTank.addTank((TinyTank) new BlockPosition(data[0], data[1], data[2], data[3]).getTileEntity());
				}
				sTank.readFromNBT(nbt);
				this.replace(sTank);
			}
			
			NBTTagList up = par1.getTagList("Up");
			for(int i = 0;i<up.tagCount();i++)
			{
				NBTTagCompound nbt = (NBTTagCompound) up.tagAt(i);
				int[] keys = nbt.getIntArray("Key");
				int[] values = nbt.getIntArray("Value");
				
				key.put(Arrays.asList(keys[0], keys[1], keys[2], keys[3]), Arrays.asList(values[0], values[1], values[2], values[3]));
			}
			
			NBTTagList down = par1.getTagList("Down");
			for(int i = 0;i<down.tagCount();i++)
			{
				NBTTagCompound nbt = (NBTTagCompound) down.tagAt(i);
				int[] keys = nbt.getIntArray("Key");
				int[] values = nbt.getIntArray("Value");
				
				value.put(Arrays.asList(keys[0], keys[1], keys[2], keys[3]), Arrays.asList(values[0], values[1], values[2], values[3]));
			}
		}
		catch (Exception e)
		{
			FMLLog.getLogger().info("Failed Loading Data");
		}
	}
	
	
	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList tankData = new NBTTagList();
		for(SharedFluidTank SharedTank : tanks.values())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			List<Integer> core = SharedTank.getCore().getPosition().getAsList();
			nbt.setIntArray("Core", new int[]{core.get(0), core.get(1), core.get(2), core.get(3)});
			NBTTagList list = new NBTTagList();
			for(TinyTank tank : SharedTank.getAllTanks())
			{
				List<Integer> ints = tank.getPosition().getAsList();
				NBTTagIntArray array = new NBTTagIntArray("N", new int[]{ints.get(0), ints.get(1), ints.get(2), ints.get(3)});
				list.appendTag(array);
			}
			nbt.setTag("Tanks", list);
			SharedTank.writeToNBT(nbt);
			tankData.appendTag(nbt);
		}
		
		NBTTagList up = new NBTTagList();
		Iterator<Entry<List<Integer>, List<Integer>>> iterKey = key.entrySet().iterator();
		while(iterKey.hasNext())
		{
			Entry<List<Integer>, List<Integer>> work = iterKey.next();
			NBTTagCompound nbt = new NBTTagCompound();
			List<Integer> key = work.getKey();
			List<Integer> value = work.getValue();
			nbt.setIntArray("Key", new int[]{key.get(0), key.get(1), key.get(2), key.get(3)});
			nbt.setIntArray("Value", new int[]{value.get(0), value.get(1), value.get(2), value.get(3)});
			up.appendTag(nbt);
		}
		
		NBTTagList down = new NBTTagList();
		Iterator<Entry<List<Integer>, List<Integer>>> iterValue = value.entrySet().iterator();
		while(iterValue.hasNext())
		{
			Entry<List<Integer>, List<Integer>> work = iterValue.next();
			NBTTagCompound nbt = new NBTTagCompound();
			List<Integer> key = work.getKey();
			List<Integer> value = work.getValue();
			nbt.setIntArray("Key", new int[]{key.get(0), key.get(1), key.get(2), key.get(3)});
			nbt.setIntArray("Value", new int[]{value.get(0), value.get(1), value.get(2), value.get(3)});
			down.appendTag(nbt);
		}
		
		par1.setTag("Tank", tankData);
		par1.setTag("Up", up);
		par1.setTag("Down", down);
	}
	
	@Override
	public void finishLoading()
	{
		
	}
	
	@Override
	public String getID()
	{
		return "TinyTank.Fluid.things";
	}
	
	@Override
	public void onTick(SpmodMod sender, Side side)
	{
		if(!side.isServer())
		{
			return;
		}
		
		
		long t = System.currentTimeMillis();
		if(t - minTime < 5)
		{
			return;
		}
		minTime = t;
		
		if(adjustDelay < 40)
		{
			adjustDelay++;
		}
		else
		{
			adjustDelay = 0;
			FMLLog.getLogger().info("Test: "+tankKey.keySet().size());
			for(SharedFluidTank tank : tankKey.keySet())
			{
				tank.onTick();
			}
		}
		
		if(downDelay < 20)
		{
			downDelay++;
		}
		else
		{
			downDelay = 0;
			
			

		}
	}
	
	@Override
	public SpmodMod getOwner()
	{
		return TinyModularThings.instance;
	}
	
}
