package speiger.src.spmodapi.common.blocks.utils;

import java.util.List;

import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeEventFactory;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MobMachineSpawner extends AdvTile
{
	private MobMachineSpawnerLogic[] logics = new MobMachineSpawnerLogic[6];
	
	public MobMachineSpawner()
	{
	}
	
	@Override
	public float getBlockHardness()
	{
		return 4;
	}
	
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 4;
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(APIBlocks.blockUtils, 1, 2);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return TextureEngine.getIcon(APIBlocks.blockUtils, 2)[1];
	}
	
	@Override
	public void onTick()
	{
		if(!isPowered())
		{
			return;
		}
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(getClockTime() % 20 == 0)
			{
				TileEntity tile = getBuffer(dir.ordinal()).getTile();
				if(tile != null && tile instanceof TileEntityMobSpawner)
				{
					TileEntityMobSpawner spawner = (TileEntityMobSpawner)tile;
					checkAndChangeIfNessesary(spawner, dir.ordinal());
				}
				else
				{
					logics[dir.ordinal()] = null;
				}
			}
			
			if(logics[dir.ordinal()] != null)
			{
				logics[dir.ordinal()].updateSpawner();
			}
		}
	}
	
	@Override
	public void init()
	{
		super.init();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			MobMachineSpawnerLogic logic = logics[dir.ordinal()];
			if(logic != null)
			{
				if(!logic.isInited())
				{
					TileEntity tile = getBuffer(dir.ordinal()).getTile();
					if(tile != null && tile instanceof TileEntityMobSpawner)
					{
						logic.setSpawner((TileEntityMobSpawner)tile);
					}
					else
					{
						logics[dir.ordinal()] = null;
					}
				}
			}
		}
	}
	
	private void checkAndChangeIfNessesary(TileEntityMobSpawner tile, int side)
	{
		if(logics[side] == null)
		{
			logics[side] = new MobMachineSpawnerLogic(tile);
		}
		if(logics[side] != null && !logics[side].getEntityNameToSpawn().equalsIgnoreCase(tile.getSpawnerLogic().getEntityNameToSpawn()))
		{
			logics[side] = new MobMachineSpawnerLogic(tile);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		NBTTagList list = par1.getTagList("SpawnerLogic");
		for(int i = 0;i < list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			int side = data.getInteger("Side");
			if(logics[side] != null)
			{
				logics[side].readFromNBT(data);
			}
			else
			{
				logics[side] = new MobMachineSpawnerLogic();
				logics[side].readFromNBT(data);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagList list = new NBTTagList();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(logics[dir.ordinal()] != null)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("Side", dir.ordinal());
				logics[dir.ordinal()].writeToNBT(data);
				list.appendTag(data);
			}
		}
		par1.setTag("SpawnerLogic", list);
	}
	
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		par2.add("Power it with Redstone To activate");
	}



	public static class MobMachineSpawnerLogic
	{
		TileEntityMobSpawner spawner;
		boolean inited;
		
		public int spawnDelay = 20;
		private String mobID = "Pig";
		
		public double field_98287_c;
		public double field_98284_d;
		private int minSpawnDelay = 200;
		private int maxSpawnDelay = 800;
		
		/** A counter for spawn tries. */
		private int spawnCount = 4;
		private Entity field_98291_j;
		private int maxNearbyEntities = 6;
		
		/** The range coefficient for spawning entities around. */
		private int spawnRange = 4;
		
		public String getEntityNameToSpawn()
		{
			return this.mobID;
		}
		
		public void setMobID(String par1Str)
		{
			this.mobID = par1Str;
		}
		
		public MobMachineSpawnerLogic()
		{
			inited = false;
		}
		
		public MobMachineSpawnerLogic(TileEntityMobSpawner tile)
		{
			spawner = tile;
			this.setMobID(tile.getSpawnerLogic().getEntityNameToSpawn());
			inited = true;
		}
		
		public void setSpawner(TileEntityMobSpawner tile)
		{
			spawner = tile;
			this.setMobID(tile.getSpawnerLogic().getEntityNameToSpawn());
			inited = true;
		}
		
		public boolean isInited()
		{
			return inited;
		}
		
		/**
		 * Returns true if there's a player close enough to this mob spawner to
		 * activate it.
		 */
		public boolean canRun()
		{
			return inited;
		}
		
		public void updateSpawner()
		{
			if(this.canRun())
			{
				double d0;
				
				if(this.getSpawnerWorld().isRemote)
				{
					double d1 = (double)((float)this.getSpawnerX() + this.getSpawnerWorld().rand.nextFloat());
					double d2 = (double)((float)this.getSpawnerY() + this.getSpawnerWorld().rand.nextFloat());
					d0 = (double)((float)this.getSpawnerZ() + this.getSpawnerWorld().rand.nextFloat());
					this.getSpawnerWorld().spawnParticle("smoke", d1, d2, d0, 0.0D, 0.0D, 0.0D);
					this.getSpawnerWorld().spawnParticle("flame", d1, d2, d0, 0.0D, 0.0D, 0.0D);
					
					if(this.spawnDelay > 0)
					{
						--this.spawnDelay;
					}
					
					this.field_98284_d = this.field_98287_c;
					this.field_98287_c = (this.field_98287_c + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
				}
				else
				{
					if(this.spawnDelay == -1)
					{
						this.func_98273_j();
					}
					
					if(this.spawnDelay > 0)
					{
						--this.spawnDelay;
						return;
					}
					
					boolean flag = false;
					
					for(int i = 0;i < this.spawnCount;++i)
					{
						Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());
						
						if(entity == null)
						{
							return;
						}
						
						int j = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), AxisAlignedBB.getAABBPool().getAABB((double)this.getSpawnerX(), (double)this.getSpawnerY(), (double)this.getSpawnerZ(), (double)(this.getSpawnerX() + 1), (double)(this.getSpawnerY() + 1), (double)(this.getSpawnerZ() + 1)).expand((double)(this.spawnRange * 2), 4.0D, (double)(this.spawnRange * 2))).size();
						
						if(j >= this.maxNearbyEntities)
						{
							this.func_98273_j();
							return;
						}
						
						d0 = (double)this.getSpawnerX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange;
						double d3 = (double)(this.getSpawnerY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
						double d4 = (double)this.getSpawnerZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange;
						EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
						entity.setLocationAndAngles(d0, d3, d4, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);
                        Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, getSpawnerWorld(), this.getSpawnerX(), getSpawnerY(), getSpawnerZ());

						if(canSpawn != Result.DENY && canSpawn(entityliving))
						{
							this.func_98265_a(entity);
							this.getSpawnerWorld().playAuxSFX(2004, this.getSpawnerX(), this.getSpawnerY(), this.getSpawnerZ(), 0);
							
							if(entityliving != null)
							{
								entityliving.spawnExplosionParticle();
							}
							
							flag = true;
						}
					}
					
					if(flag)
					{
						this.func_98273_j();
					}
				}
			}
		}
		
		public boolean canSpawn(EntityLiving entity)
		{
			if(entity == null)
			{
				return false;
			}
			return entity.worldObj.checkNoEntityCollision(entity.boundingBox) && entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox);
		}
		
		public Entity func_98265_a(Entity par1Entity)
		{
			if(par1Entity instanceof EntityLivingBase && par1Entity.worldObj != null)
			{
				((EntityLiving)par1Entity).onSpawnWithEgg((EntityLivingData)null);
				this.getSpawnerWorld().spawnEntityInWorld(par1Entity);
			}
			
			return par1Entity;
		}
		
		private void func_98273_j()
		{
			if(this.maxSpawnDelay <= this.minSpawnDelay)
			{
				this.spawnDelay = this.minSpawnDelay;
			}
			else
			{
				int i = this.maxSpawnDelay - this.minSpawnDelay;
				this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
			}
			
			this.func_98267_a(1);
		}
		
		public void readFromNBT(NBTTagCompound par1NBTTagCompound)
		{
			this.mobID = par1NBTTagCompound.getString("EntityId");
			this.spawnDelay = par1NBTTagCompound.getShort("Delay");
			
			if(par1NBTTagCompound.hasKey("MinSpawnDelay"))
			{
				this.minSpawnDelay = par1NBTTagCompound.getShort("MinSpawnDelay");
				this.maxSpawnDelay = par1NBTTagCompound.getShort("MaxSpawnDelay");
				this.spawnCount = par1NBTTagCompound.getShort("SpawnCount");
			}
			
			if(par1NBTTagCompound.hasKey("MaxNearbyEntities"))
			{
				this.maxNearbyEntities = par1NBTTagCompound.getShort("MaxNearbyEntities");
			}
			
			if(par1NBTTagCompound.hasKey("SpawnRange"))
			{
				this.spawnRange = par1NBTTagCompound.getShort("SpawnRange");
			}
			
			if(this.getSpawnerWorld() != null && this.getSpawnerWorld().isRemote)
			{
				this.field_98291_j = null;
			}
		}
		
		public void writeToNBT(NBTTagCompound par1NBTTagCompound)
		{
			par1NBTTagCompound.setString("EntityId", this.getEntityNameToSpawn());
			par1NBTTagCompound.setShort("Delay", (short)this.spawnDelay);
			par1NBTTagCompound.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
			par1NBTTagCompound.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
			par1NBTTagCompound.setShort("SpawnCount", (short)this.spawnCount);
			par1NBTTagCompound.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
			par1NBTTagCompound.setShort("SpawnRange", (short)this.spawnRange);
			
		}
		
		/**
		 * Sets the delay to minDelay if parameter given is 1, else return
		 * false.
		 */
		public boolean setDelayToMin(int par1)
		{
			if(par1 == 1 && this.getSpawnerWorld().isRemote)
			{
				this.spawnDelay = this.minSpawnDelay;
				return true;
			}
			else
			{
				return false;
			}
		}
		
		@SideOnly(Side.CLIENT)
		public Entity func_98281_h()
		{
			if(this.field_98291_j == null)
			{
				Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), (World)null);
				entity = this.func_98265_a(entity);
				this.field_98291_j = entity;
			}
			
			return this.field_98291_j;
		}
		
		public void func_98267_a(int i)
		{
			getSpawnerWorld().addBlockEvent(getSpawnerX(), getSpawnerY(), getSpawnerZ(), APIBlocks.blockUtils.blockID, i, 0);
		}
		
		public World getSpawnerWorld()
		{
			if(spawner == null)
			{
				return null;
			}
			return this.spawner.getWorldObj();
		}
		
		public int getSpawnerX()
		{
			return this.spawner.xCoord;
		}
		
		public int getSpawnerY()
		{
			return this.spawner.yCoord;
		}
		
		public int getSpawnerZ()
		{
			return this.spawner.zCoord;
		}
	}
	
}
