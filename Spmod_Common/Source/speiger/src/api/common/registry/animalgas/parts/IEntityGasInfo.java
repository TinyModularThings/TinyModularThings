package speiger.src.api.common.registry.animalgas.parts;

import net.minecraft.entity.EntityAgeable;

public interface IEntityGasInfo extends IEntityInfo
{
	/**
	 * get the minimal Gas it can produce. (Internal)
	 */
	public int getMinProducingGas(EntityAgeable par1);
	
	/**
	 * get the maximal gas Production.
	 */
	public int getMaxProducingGas(EntityAgeable par1);
	
	/**
	 * get the Speed how fast it can maximum can produce. (This simply is for infections and co)
	 */
	public EntitySpeed getGasProductionSpeed(EntityAgeable par1);
	
}
