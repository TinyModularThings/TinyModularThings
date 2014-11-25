package speiger.src.api.common.registry.animalgas.parts;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import speiger.src.api.common.data.utils.IFluidInfo;

public interface IEntityDrinkInfo extends IEntityInfo
{
	/**
	 * simply the fluids that it is drinking.
	 * Fluid amount counts for how much it is drinking. for 1 point (100 Points is 100%)
	 */
	public List<IFluidInfo> getDrinks(EntityAgeable par1);
	
	/**
	 * @return the amount it is drinking for every tick of that.
	 */
	public int getDrinkingAmount(EntityAgeable par1);
}
