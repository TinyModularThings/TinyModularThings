package speiger.src.spmodapi.common.util.data;

import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.EntityAgeable;
import speiger.src.api.common.registry.animalgas.parts.Resistence;

public enum Infection
{
	Nothing,
	Fever,
	HardFever,
	Poisen,
	FoodPoisen,
	Squits;
	
	
	public void handleEffect(EntityAgeable par1)
	{
		
	}

	public Resistence getResistance()
	{
		switch(this)
		{
			case Fever: return Resistence.Weather;
			case HardFever: return Resistence.Weather;
			case FoodPoisen: return Resistence.Poisen;
			case Poisen: return Resistence.Poisen;
			case Squits: return Resistence.Poisen;
			default: return null;
			
		}
	}

	public static Infection[] getSadValues()
	{
		List<Infection> inv = Arrays.asList(values());
		inv.remove(Nothing);
		return inv.toArray(new Infection[0]);
		
	}
}
