package speiger.src.spmodapi.common.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.AxisAlignedBB;


public class EntityAiAutoFeed extends EntityAIBase
{
	EntityAnimal owner = null;
	EntityItem target = null;
	
	
	public EntityAiAutoFeed(EntityAnimal animal)
	{
		this.owner = animal; 
	}


	@Override
	public boolean shouldExecute()
	{
		if(target == null)
		{
			if(owner != null && !owner.isInLove() && owner.getGrowingAge() == 0)
			{
				List<EntityItem> items = owner.worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(owner.posX, owner.posY, owner.posZ, owner.posX+1, owner.posY+1, owner.posZ+1).expand(10, 4, 10));
				EntityItem item = getBreedItem(items);
				if(item != null && owner.getEntitySenses().canSee(item))
				{
					target = item;
					return true;
				}
			}
		}
		else
		{
			if(owner != null && !owner.isInLove() && owner.getGrowingAge() == 0 && owner.getEntitySenses().canSee(target) && owner.getDistanceSqToEntity(target) <= 20D)
			{
				return true;
			}
		}
		return false;
	}
	
	public EntityItem getBreedItem(List<EntityItem> items)
	{
		if(owner != null && items != null && items.size() > 0)
		{
			for(EntityItem cuItem : items)
			{
				if(cuItem != null && cuItem.getEntityItem() != null)
				{
					if(owner.isBreedingItem(cuItem.getEntityItem()))
					{
						return cuItem;
					}
				}
			}
		}
		return null;
	}


	@Override
	public void resetTask()
	{
		target = null;
	}


	@Override
	public void updateTask()
	{
		if(target != null && owner != null)
		{
			owner.getNavigator().tryMoveToEntityLiving(target, 1.2D);
			if(owner.getDistanceSqToEntity(target) < 0.8D)
			{
				EntityItem item = (EntityItem) owner.worldObj.getEntityByID(target.entityId);
				if(item != null && item.getEntityItem() != null && item.getEntityItem().stackSize > 0)
				{
					item.getEntityItem().stackSize--;
					owner.func_110196_bT();
					this.resetTask();
				}
				
			}
		}
	}
	
	
	
}
