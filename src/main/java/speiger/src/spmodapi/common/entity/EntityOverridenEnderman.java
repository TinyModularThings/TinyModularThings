package speiger.src.spmodapi.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class EntityOverridenEnderman extends EntityEnderman
{

	public EntityOverridenEnderman(World par1World)
	{
		super(par1World);
	}

	@Override
	public Block func_146080_bZ()
	{
		return Blocks.air;
	}

	
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(14.0D);
	}

	@Override
	protected boolean isAIEnabled()
	{
		return true;
	}

	@Override
	public String getCommandSenderName()
	{
		return "Enderman";
	}

	
	
	
	
	
	
}
