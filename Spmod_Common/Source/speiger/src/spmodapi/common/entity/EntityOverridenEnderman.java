package speiger.src.spmodapi.common.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.world.World;

public class EntityOverridenEnderman extends EntityEnderman
{
	
	public EntityOverridenEnderman(World par1World)
	{
		super(par1World);
	}
	
	@Override
	public int getCarried()
	{
		return 0;
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(14.0D);
	}
	
	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}
	
	@Override
	public String getEntityName()
	{
		return "Enderman";
	}
	
}
