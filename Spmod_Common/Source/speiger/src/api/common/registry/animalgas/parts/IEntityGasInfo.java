package speiger.src.api.common.registry.animalgas.parts;

import net.minecraft.entity.EntityLiving;

public interface IEntityGasInfo extends IEntityInfo
{
	/**
	 * get the minimal Gas it can produce. (Internal)
	 */
	public int getMinProducingGas(EntityLiving par1);
	
	/**
	 * get the maximal gas Production.
	 */
	public int getMaxProducingGas(EntityLiving par1);
	
	/**
	 * get the Speed how fast it can maximum can produce. (This simply is for infections and co)
	 */
	public EntitySpeed getGasProductionSpeed(EntityLiving par1);
	
}
