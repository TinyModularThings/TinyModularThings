package speiger.src.api.common.registry.animalgas.parts;

public enum Resistence
{
	Poisen, 
	Nausea, 
	FoodPoisen, 
	Squits;
	
	public static enum ResistanceType
	{
		VeryWeak,
		Weak,
		Normal,
		Medium,
		High,
		Higher,
		Highest;
	}
}
