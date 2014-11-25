package speiger.src.api.common.registry.animalgas.parts;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import speiger.src.api.common.data.utils.IStackInfo;

public interface IEntityFoodInfo extends IEntityInfo
{
	/**
	 * @EntityLiving simply the Entity.
	 * @return simply the Item which it can eat.
	 * @Note: StackSize count of how much it need to eat. For 1 point!
	 */
	public List<IStackInfo> getFoodItems(EntityLiving par1);
	
	/**
	 * @return the amount it is eating over time.
	 * The time how long it takes is set already.
	 */
	public int getEatingAmount(EntityLiving par1);
	
	/**
	 * @return simply the minium level of long hungerring should be.
	 * So entities can starve now. When they produce gas.
	 */
	public int getMinimumReserves(EntityLiving par1);
	
	/**
	 * @return simply returns the level when the Entities are extreme Fat.
	 */
	public int getMaximumReserves(EntityLiving par1);
}
