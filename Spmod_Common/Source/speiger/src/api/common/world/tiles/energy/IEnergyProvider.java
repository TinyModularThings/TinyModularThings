package speiger.src.api.common.world.tiles.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IEnergyProvider
{
	IEnergySubject getEnergyProvider(ForgeDirection side);
	
}
