package speiger.src.spmodapi.common.util.data;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import speiger.src.api.common.registry.animalgas.parts.Resistence;

public enum Infection
{
	Nothing;
	
	
	public void handleEffect(EntityAgeable par1)
	{
		
	}

	public Resistence getResistance()
	{
		return null;
	}

	public static Infection[] getSadValues()
	{
		List<Infection> inv = Arrays.asList(values());
		inv.remove(Nothing);
		return inv.toArray(new Infection[0]);
		
	}
}
