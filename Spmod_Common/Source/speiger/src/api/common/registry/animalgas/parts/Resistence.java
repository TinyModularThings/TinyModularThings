package speiger.src.api.common.registry.animalgas.parts;

public enum Resistence
{
	Poisen, 
	Weather;
	
	public static enum ResistanceType
	{
		VeryWeak(0.05D),
		Weak(0.1D),
		Normal(0.25D),
		Medium(0.4D),
		High(0.6D),
		Higher(0.75D),
		Highest(1D);
		
		double value;
		
		private ResistanceType(double par1)
		{
			value = par1;
		}
		
		public double getValue()
		{
			return value;
		}
	}
}
