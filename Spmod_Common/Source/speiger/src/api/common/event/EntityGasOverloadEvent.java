package speiger.src.api.common.event;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingEvent;
import speiger.src.api.common.registry.animalgas.parts.IEntityLogic;

public class EntityGasOverloadEvent extends LivingEvent
{
	/**
	 * You can cast this class to EntityProcessor that is the Only function for the IEntityLogic.
	 * So if you implement my Mod to your coding base that allow you to read data.^^"
	 */
	public final IEntityLogic entityLogic;
	public final Explosion result;
	
	
	public EntityGasOverloadEvent(EntityAgeable entity, Explosion par1, IEntityLogic par2)
	{
		super(entity);
		result = par1;
		entityLogic = par2;
	}
	
}
