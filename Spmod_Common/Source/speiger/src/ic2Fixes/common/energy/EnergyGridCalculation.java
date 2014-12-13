package speiger.src.ic2Fixes.common.energy;

import java.util.concurrent.Callable;

public class EnergyGridCalculation implements Callable<Iterable<EnergyNode>>
{
	
	private final EnergyGrid grid;
	
	public EnergyGridCalculation(EnergyGrid grid)
	{
		this.grid = grid;
	}
	
	@Override
	public Iterable<EnergyNode> call() throws Exception
	{
		
		return enw ;
	}
	
}
