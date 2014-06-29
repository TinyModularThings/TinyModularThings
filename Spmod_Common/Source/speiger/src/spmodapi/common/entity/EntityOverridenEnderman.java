package speiger.src.spmodapi.common.entity;

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
