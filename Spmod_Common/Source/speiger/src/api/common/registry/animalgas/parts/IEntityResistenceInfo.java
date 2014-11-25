package speiger.src.api.common.registry.animalgas.parts;

import java.util.EnumSet;

import net.minecraft.entity.EntityLiving;

public interface IEntityResistenceInfo extends IEntityInfo
{
	/**
	 * This returns the Base Resistance that the Entity get by creating Data for it.
	 * Note you can breed your Entities to be more resistance.
	 */
	public EnumSet<Resistence> getEntityBaseResistence(EntityLiving par1);
	
	/**
	 * This thing ask for the Resistance Base of the Entity. So if it has a weak resistance then it can be
	 * faster Effected. If it is very High then it can be a really small chance to get ill. It is also based on the Type of effection.
	 * Note: If you do not make the Entity to 100% Resistand then there is always a chance that it get ill.
	 */
	public Resistence.ResistanceType getResistanceType(EntityLiving par1);
	
}
