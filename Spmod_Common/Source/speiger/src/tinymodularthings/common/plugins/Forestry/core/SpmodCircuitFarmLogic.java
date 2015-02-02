package speiger.src.tinymodularthings.common.plugins.Forestry.core;

import java.util.List;

import forestry.farming.circuits.CircuitFarmLogic;
import forestry.farming.logic.FarmLogic;

public class SpmodCircuitFarmLogic extends CircuitFarmLogic
{

	public SpmodCircuitFarmLogic(String uid, Class<? extends FarmLogic> logicClass, String[] descriptions)
	{
		super(uid, logicClass, descriptions);
	}

	@Override
	public String getName()
	{
		return "IC2 Crops";
	}

	@Override
	public void addTooltip(List arg0)
	{
		arg0.add("Handle Ic2 Crops");
	}

	@Override
	public String getUID()
	{
		return super.getUID();
	}
	
	
	
}
