package speiger.src.api.common.registry.animalgas.parts;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import speiger.src.api.common.data.utils.IStackInfo;

public interface IEntityFoodInfo extends IEntityInfo
{
	/**
	 * @EntityAgeable simply the Entity.
	 * @return simply the Item which it can eat.
	 * @Note: StackSize count of how much it need to eat. For 1 point!
	 */
	public List<IStackInfo> getFoodItems(EntityAgeable par1);
	
	/**
	 * @return the amount it is eating over time.
	 * this simply mean how much food get Used that he already ate.
	 * The time how long it takes is set already.
	 */
	public int getEatingAmount(EntityAgeable par1);
	
	/**
	 * @return simply the minium level of long hungerring should be.
	 * So entities can starve now. When they produce gas.
	 */
	public int getMinimumReserves(EntityAgeable par1);
	
	/**
	 * @return simply returns the level when the Entities are extreme Fat.
	 */
	public int getMaximumReserves(EntityAgeable par1);
}
