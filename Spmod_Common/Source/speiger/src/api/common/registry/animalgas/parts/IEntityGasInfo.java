package speiger.src.api.common.registry.animalgas.parts;

import net.minecraft.entity.EntityAgeable;

public interface IEntityGasInfo extends IEntityInfo
{
	/**
	 * get the minimal Gas it can produce. a number between 0 and 10 (Internal)
	 */
	public int getMinProducingGas(EntityAgeable par1);
	
	/**
	 * get the maximal gas Production. a number between 1 and 10. Everything else is not allowed.
	 */
	public int getMaxProducingGas(EntityAgeable par1);
	
	/**
	 * get the Speed how fast it can maximum can produce. (This simply is for infections and co)
	 */
	public EntitySpeed getGasProductionSpeed(EntityAgeable par1);
	
}
