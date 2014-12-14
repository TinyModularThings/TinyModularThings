package speiger.src.ic2Fixes.common.energy;

import ic2.api.Direction;

class EnergyBlockLink
{
	Direction direction;
	double loss;
	
	EnergyBlockLink(Direction direction, double loss)
	{
		this.direction = direction;
		this.loss = loss;
	}
	
}
