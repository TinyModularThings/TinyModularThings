package speiger.src.spmodapi.common.world.event;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.event.BlockPlacedEvent;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.Ticks;
import speiger.src.api.util.Ticks.ITickReader;
import speiger.src.spmodapi.SpmodAPI;
import cpw.mods.fml.relauncher.Side;

/*
* Copyright (c) 2014, AEnterprise
* http://buildcraftadditions.wordpress.com/
* Eureka is distributed under the terms of LGPLv3
* Please check the contents of the license located in
* http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */

/**
 * 
 * @author Speiger and also in Eureka i created the code!
 *
 */

public class BlockDetector implements ITickReader
{
	public static BlockDetector instance;
	public ArrayList<PlacePosition> placed = new ArrayList<PlacePosition>();


	public BlockDetector()
	{
		instance = this;
		Ticks.registerTickReciver(this);
	}
	
	@Override
	public void onTick(SpmodMod par1, Side par2)
	{
		if(par2 != par2.SERVER)
		{
			return;
		}
		
		if(placed.size() <= 0 || placed.isEmpty() || placed == null)
		{
			return;
		}
		ArrayList<PlacePosition> remove = new ArrayList<PlacePosition>();
		for(PlacePosition cu : placed)
		{
			if(cu.check())
			{
				remove.add(cu);
			}
		}
		if(remove.size() > 0)
		{
			placed.removeAll(remove);
		}
		
	}
	
	//Has to be lowest because we want to know if another mod says Nope you are not allowed to place Block.
	@ForgeSubscribe(priority=EventPriority.LOWEST)
	public void onItemUse(PlayerInteractEvent evt)
	{
		if(evt.action == Action.LEFT_CLICK_BLOCK)
		{
			//Ignoring Hitting Block Simply
			return;
		}
		if(evt.isCanceled())
		{
			return;
		}
		if(evt.useItem == evt.useItem.DENY)
		{
			return;
		}
		
		
		//That is just another possebility of Placing Block. Just a backup.
		int[] pos = getAdjustedCoords(evt.entity.worldObj, evt.x, evt.y, evt.z, evt.face);
		
		placed.add(new PlacePosition(evt.entity.worldObj, evt.x, evt.y, evt.z, evt.entityPlayer));
		if(pos[0] != evt.x || pos[1] != evt.y || pos[2] != evt.z)
		{
			placed.add(new PlacePosition(evt.entity.worldObj, pos[0], pos[1], pos[2], evt.entityPlayer));
		}
		
	}
	
	private int[] getAdjustedCoords(World world, int x, int y, int z, int facing)
	{
		if(facing == -1)
		{
			return new int[]{x,y,z};
		}
		
		int block = world.getBlockId(x, y, z);
		if(block > 0)
		{
			int xCoord = x;
			int yCoord = y;
			int zCoord = z;
			
	        if (block == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1)
	        {
	        	
	        }
	        else if (block != Block.vine.blockID && block != Block.tallGrass.blockID && block != Block.deadBush.blockID && (Block.blocksList[block] == null || !Block.blocksList[block].isBlockReplaceable(world, x, y, z)))
	        {
	            if (facing == 0)
	            {
	                --yCoord;
	            }

	            if (facing == 1)
	            {
	                ++yCoord;
	            }

	            if (facing == 2)
	            {
	                --zCoord;
	            }

	            if (facing == 3)
	            {
	                ++zCoord;
	            }

	            if (facing == 4)
	            {
	                --xCoord;
	            }

	            if (facing == 5)
	            {
	                ++xCoord;
	            }
	            
	            return new int[]{xCoord, yCoord, zCoord};
			}
		}
		
		return new int[]{x,y,z};
	}
	
	
	public static class PlacePosition
	{
		//Data of May Placing.
		World world;
		int x;
		int y;
		int z;
		EntityPlayer placer;
		
		//Infos about the block before placed.
		Block block;
		int metadata;
	
		int tests = 10;
		
		public PlacePosition()
		{
			
		}
		
		public PlacePosition(World world, int x, int y, int z, EntityPlayer placer)
		{
			this.world = world;
			this.x = x;
			this.y = y;
			this.z = z;
			this.placer = placer;
			
			this.block = Block.blocksList[world.getBlockId(x, y, z)];
			this.metadata = world.getBlockMetadata(x, y, z);
		}
		
		public boolean check()
		{
			if(world.isRemote)
			{
				return false;
			}
			
			if(tests <= 0)
			{
				return true;
			}
			
			tests--;
			
			if(Block.blocksList[world.getBlockId(x, y, z)] != block)// || world.getBlockMetadata(x, y, z) != metadata)
			{
				MinecraftForge.EVENT_BUS.post(new BlockPlacedEvent(new BlockPosition(world, x, y, z), placer));
				return true;
			}
			return false;
		}
	}


	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}
}
