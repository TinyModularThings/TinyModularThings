package speiger.src.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

public interface IEnergyProvider
{
	EnergyProvider getEnergyProvider(ForgeDirection side);
	
}
