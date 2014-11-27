package speiger.src.api.common.registry.animalgas.parts;

public enum EntitySpeed
{
	VerySlow(0.01D),
	Slow(0.05D),
	Normal(0.1D),
	Medium(0.25D),
	Fast(0.35D),
	Faster(0.45D),
	Fastest(0.5D);
	double speed;
	
	private EntitySpeed(double par1)
	{
		speed = par1;
	}
	
	public double getSpeed()
	{
		return speed;
	}
}
