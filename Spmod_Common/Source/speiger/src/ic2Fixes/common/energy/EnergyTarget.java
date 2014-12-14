package speiger.src.ic2Fixes.common.energy;

import ic2.api.Direction;
import net.minecraft.tileentity.TileEntity;

class EnergyTarget
{
	TileEntity tileEntity;
	Direction direction;
	
	EnergyTarget(TileEntity tileEntity, Direction direction)
	{
		this.tileEntity = tileEntity;
		this.direction = direction;
	}
}
