package speiger.src.spmodapi.common.util.data;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import speiger.src.api.common.event.BlockPlacedEvent;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.Ticks;
import speiger.src.api.common.registry.helpers.Ticks.ITickReader;
import speiger.src.api.common.world.utils.IStructureBox;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author Speiger
 *
 * This class is for Block MultiStructures Not TileEntity MultiStructures
 * The Difference between Block & TileEntity Multistructures is that a
 * TileEntity Multistructure is only build out of TileEntities there are no basic blocks.
 * The Block MultiStructure has only 1 core and the rest is made out of basic blocks like cobble.
 * There is the case that other interaction TileEntity blocks are joining with it.
 * But you can Compare that with XYCraft MultiTanks... I never saw Soyrans Code. (Just for note)s
 *
 */
public class StructureStorage implements ITickReader
{
	public static StructureStorage instance = new StructureStorage();
	
	private WeakHashMap<World, BoxList> updateBoxes = new WeakHashMap<World, BoxList>();
	private WeakHashMap<World, BoxList> interactionBoxes = new WeakHashMap<World, BoxList>();
	//Needed because else they would not notify that the block broke..
	private List<IStructureBox> breakNotifies = new ArrayList<IStructureBox>();
	
	
	public boolean isAnyUpdateBoxColiding(World world, IStructureBox...par1)
	{
		boolean flag = false;
		for(IStructureBox box : par1)
		{
			flag = isUpdateBoxColiding(world, box);
			if(flag)
			{
				break;
			}
		}
		return flag;
	}
	
	/**
	 * function if coliding with other boxes
	 * return false = nope
	 */
	public boolean isUpdateBoxColiding(World world, IStructureBox par1)
	{
		BoxList list = getUpdateBoxList(world);
		if(list == null)
		{
			return false;
		}
		return list.isColidingWithother(par1);
	}
	
	
	/**
	 * Multi Check function. nothing Special
	 */
	public boolean isAnyInteractionBoxColiding(World world, IStructureBox...par1)
	{
		boolean flag = false;
		for(IStructureBox box : par1)
		{
			flag = isInteractionBoxColiding(world, box);
			if(flag)
			{
				break;
			}
		}
		return flag;
	}
	
	/**
	 * function if Coliding with other Boxes.
	 * return false = nope. 
	 */
	public boolean isInteractionBoxColiding(World world, IStructureBox par1)
	{
		BoxList list = getInteractionBoxList(world);
		if(list == null)
		{
			return false;
		}
		return list.isColidingWithother(par1);
	}
	
	/**
	 * removes the Player Interaction Box from the World.
	 */
	public void removeInteractionBox(World world, IStructureBox...par1)
	{
		BoxList list = getInteractionBoxList(world);
		if(list == null)
		{
			return;
		}
		for(IStructureBox box : par1)
		{
			list.remove(box);
		}
	}
	
	/**
	 * removes the the Update box from the World
	 */
	public void removeUpdateBoxes(World world, IStructureBox...par1)
	{
		BoxList list = getUpdateBoxList(world);
		if(list == null)
		{
			return;
		}
		for(IStructureBox box : par1)
		{
			list.remove(box);
		}
		notifyUpdateBoxes(world);
	}
	
	private void notifyUpdateBoxes(World world)
	{
		BoxList list = getUpdateBoxList(world);
		if(list == null)
		{
			return;
		}
		for(IStructureBox box : list)
		{
			box.onBlockChange();
		}
	}
	
	
	/**
	 * Same as the registerInteractionBoxes. But these are for blockEvents
	 * like block Placed and Block Broken. You do not require these but they will be simply fired
	 * anyways and that reduces the amount of TileEntity ticks that you have to use since it will be handed
	 * by events. So the Event sender gets the lag. I will maybe even make a system catch so that
	 * blocks only fire events but the load of it will be handled at the end of a tick..
	 * 
	 * @return if the boxes colide with any other boxes.
	 */
	public boolean registerUpdateBoxes(World world, IStructureBox...par1)
	{
		BoxList list = getUpdateBoxList(world);
		if(list == null)
		{
			//World == null = List == null. You get what you give.
			return false;
		}
		boolean flag = true;
		for(IStructureBox box : par1)
		{
			if(list.isColidingWithother(box))
			{
				flag = false;
			}
			list.add(box);
		}
		notifyUpdateBoxes(world);
		return flag;
	}
	
	
	
	
	
	/**
	 * Here you register the PlayerInteraction MultiStructure Boxes.
	 * You can register multible if you want to have multibe results.
	 * May Change and the Information of the possition will be sended with the interaction.
	 * But here you register your boxes.
	 * World is required because differend dimensions and co with support. You know :D
	 * @return false if any boxes colide withsomething else.
	 * 
	 */
	public boolean registerInteractionBoxes(World world, IStructureBox...par1)
	{
		BoxList list = getInteractionBoxList(world);
		if(list == null)
		{
			//World == null = List == null. You get what you give.
			return false;
		}
		boolean flag = true;
		for(IStructureBox box : par1)
		{
			if(list.isColidingWithother(box))
			{
				flag = false;
			}
			list.add(box);
		}
		return false;
	}
	
	
	
	
	
	
	
	
	/*	New Multi Structure Logic. Its way more clean then the old one.
	 *  Instead of having every position stored in data and reloaded i allow structures
	 * 	now to register their Bounding Boxes. Which is for interaction and Updates really usefull
	 *  That also prevent that people need to update the Structure Check every tick (every 20 ticks or what they setted up)
	 *  Because as soon something in their structure box (that is used for the structure itself) changes the structures get an information
	 *  and also what kind of change it is. So the Lag increases and Decreases by the amount of interactions it makes...
	 *	
	 *	There are now Two Boxs list. 1 For player interaction if they allow that you can click on them then they will get a notify when
	 *	a player want a interaction with the structure.
	 *	The Other list is for every kind of WorldUpdate. as BlockPlaced/BlockBreaked events. So updates happens differend...
	 */
	
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onClickBlock(PlayerInteractEvent evt)
	{
		if (!evt.entity.worldObj.isRemote && !evt.isCanceled() && evt.action == Action.RIGHT_CLICK_BLOCK && !evt.entityPlayer.isSneaking())
		{
			BoxList list = getInteractionBoxList(evt.entityPlayer.worldObj);
			if(list != null && list.size() > 0)
			{
				IStructureBox box = list.getBoxFromCoords(evt.x, evt.y, evt.z);
				if(box != null && box.isBoxActive())
				{
					box.onInteract(evt.entityPlayer);
					evt.useItem = Result.DENY;
				}
			}
		}
	}
	
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockEvent.BreakEvent evt)
	{
		if(!evt.world.isRemote && !evt.isCanceled())
		{
			BoxList list = getUpdateBoxList(evt.world);
			if(list != null && list.size() > 0)
			{
				IStructureBox box = list.getBoxFromCoords(evt.x, evt.y, evt.z);
				if(box != null)
				{
					this.breakNotifies.add(box);
				}
			}
		}
	}
	
	@ForgeSubscribe(priority = EventPriority.LOWEST)
	public void onBlockPlaced(BlockPlacedEvent event)
	{
		if(!event.world.isRemote)
		{
			BoxList list = getUpdateBoxList(event.world);
			if(list != null && list.size() > 0)
			{
				IStructureBox box = list.getBoxFromCoords(event.x, event.y, event.z);
				//Here it always get called since the update happends here.
				//At all other it needs a check.
				if(box != null)
				{
					box.onBlockChange();
				}
			}
		}
	}
	
	public BoxList getUpdateBoxList(World world)
	{
		if(world == null)
		{
			return null;
		}
		if(!updateBoxes.containsKey(world))
		{
			updateBoxes.put(world, new BoxList());
		}
		return updateBoxes.get(world);
	}
	
	public BoxList getInteractionBoxList(World world)
	{
		if(world == null)
		{
			return null;
		}
		if(!interactionBoxes.containsKey(world))
		{
			interactionBoxes.put(world, new BoxList());
		}
		return interactionBoxes.get(world);
	}
	
	
	// Loading and Writing to data
	public static void registerForgeEvent()
	{
		MinecraftForge.EVENT_BUS.register(instance);
		Ticks.registerTickReciver(instance);
	}
	

	

	
	public static class BoxList extends ArrayList<IStructureBox>
	{
		public boolean isColidingWithother(IStructureBox par1)
		{
			int minXCheck = par1.getMinX();
			int minYCheck = par1.getMinY();
			int minZCheck = par1.getMinZ();
			int maxXCheck = par1.getMaxX();
			int maxYCheck = par1.getMaxY();
			int maxZCheck = par1.getMaxZ();
			
			for(int i = 0;i<this.size();i++)
			{
				IStructureBox box = this.get(i);
				int minX = box.getMinX();
				int minY = box.getMinY();
				int minZ = box.getMinZ();
				int maxX = box.getMaxX();
				int maxY = box.getMaxY();
				int maxZ = box.getMaxZ();
				
				boolean same = minXCheck == minX && minYCheck == minY && minZCheck == minZ && maxXCheck == maxX && maxYCheck == maxY && maxZCheck == maxZ;
				if(same)
				{
					continue;
				}
				
				boolean XColide = (minX >= minXCheck && minX <= maxXCheck) || (maxX >= minXCheck && maxX <= maxXCheck);
				boolean YColide = (minY >= minYCheck && minY <= maxYCheck) || (maxY >= minYCheck && maxY <= maxYCheck);
				boolean ZColide = (minZ >= minZCheck && minZ <= maxZCheck) || (maxZ >= minZCheck && maxZ <= maxZCheck);
				if(XColide && YColide && ZColide)
				{
					return true;
				}
				
			}
			return false;
		}
		
		public IStructureBox getBoxFromCoords(int x, int y, int z)
		{
			for(int i = 0;i<this.size();i++)
			{
				IStructureBox box = this.get(i);
				if(box.contains(x, y, z))
				{
					return box;
				}
			}
			return null;
		}
		
	}





	@Override
	public void onTick(SpmodMod sender, Side side)
	{
		if(side == Side.CLIENT)
		{
			return;
		}
		for(IStructureBox box : this.breakNotifies)
		{
			box.onBlockBreak();
		}
		this.breakNotifies.clear();
	}

	@Override
	public boolean needTick()
	{
		return this.breakNotifies.size() > 0;
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}
}
