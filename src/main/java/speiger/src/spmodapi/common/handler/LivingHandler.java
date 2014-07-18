package speiger.src.spmodapi.common.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import speiger.src.api.tiles.IExpProvider;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.entity.EntityOverridenEnderman;
import speiger.src.spmodapi.common.entity.ai.EntityAiAutoFeed;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LivingHandler
{
	public static LivingHandler instance = new LivingHandler();
	
	@SubscribeEvent
	public void onJumping(LivingJumpEvent evt)
	{
		EntityLivingBase entity = evt.entityLiving;
		IAttributeInstance jump = entity.getAttributeMap().getAttributeInstanceByName(APIUtils.jumpBoost.getAttributeUnlocalizedName());
		if (jump != null && jump.getBaseValue() < jump.getAttributeValue())
		{
			double space = jump.getAttributeValue() - jump.getBaseValue();
			entity.motionY += 0.1 * space;
		}
	}
	
	@SubscribeEvent
	public void onCreating(EntityConstructing par0)
	{
		if (par0.entity != null)
		{
			if(par0.entity instanceof EntityLivingBase)
			{
				EntityLivingBase entity = (EntityLivingBase) par0.entity;
				if (!entity.getAttributeMap().getAllAttributes().contains(APIUtils.jumpBoost))
				{
					entity.getAttributeMap().registerAttribute(APIUtils.jumpBoost);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent evt)
	{
		EntityLivingBase entity = evt.entityLiving;
		if(entity != null && !entity.worldObj.isRemote && entity instanceof EntityMob)
		{
			EntityMob mob = (EntityMob) entity;
			ArrayList<TileEntity> tiles = WorldReading.getTileWithAABB((int)mob.posX, (int)mob.posY, (int)mob.posZ, mob.worldObj, 10, APIBlocks.blockUtils, 1);
			for(TileEntity tile : tiles)
			{
				if(tile != null && tile instanceof IExpProvider)
				{
					IExpProvider pro = (IExpProvider) tile;
					if(pro.absorbDeath())
					{
						// TODO Find a way to get mob.experienceValue
						//pro.addExp(mob.experienceValue);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingTick(LivingUpdateEvent par1)
	{
		EntityLivingBase entity = par1.entityLiving;
		if (entity != null)
		{
			if(entity instanceof EntityAnimal)
			{
				EntityAnimal animal = (EntityAnimal) entity;
				if(!this.containsTask(animal.tasks.taskEntries) && animal.createChild(animal) != null)
				{
					animal.tasks.addTask(5, new EntityAiAutoFeed(animal));
				}
			}
			
			if (entity instanceof EntityPlayer)
			{
				
				EntityPlayer player = (EntityPlayer) entity;
				ItemStack armor = player.getCurrentArmor(1);
				if (!player.capabilities.isFlying && armor != null && armor.getItem() == APIItems.hempLeggings)
				{
					if (player.fallDistance > 0.125F)
					{
						player.fallDistance -= 0.125F;
					}
				}
			}
			else
			{
				ItemStack armor = entity.getEquipmentInSlot(2);
				if (armor != null && armor.getItem() == APIItems.hempLeggings && !entity.noClip)
				{
					if (entity.fallDistance > 0.5F)
					{
						entity.fallDistance -= 0.5;
					}
				}
			}
			
			if(entity instanceof EntityMob)
			{
				EntityMob mob = (EntityMob) entity;
				if(mob.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange).getBaseValue() < 25D)
				{
					mob.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange).setBaseValue(25D);
				}
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void zoomingEffect(FOVUpdateEvent evt)
	{
		EntityPlayerSP player = evt.entity;
		ItemStack stack = player.getCurrentArmor(0);
		if (stack != null && stack.getItem() == APIItems.hempBoots && player.isInWater())
		{
			evt.newfov = 1.05F;
		}
	}
	
	public boolean containsTask(List<EntityAITaskEntry> list)
	{
		for(EntityAITaskEntry task : list)
		{
			if(task.action instanceof EntityAiAutoFeed)
			{
				return true;
			}
		}
		return false;
	}
	
	@SubscribeEvent
	public void handleSpmodEnderman(EnderTeleportEvent evt)
	{
		if(evt.entityLiving instanceof EntityOverridenEnderman)
		{
			evt.setCanceled(true);
		}
	}
}
