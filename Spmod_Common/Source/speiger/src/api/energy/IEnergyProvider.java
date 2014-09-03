package speiger.src.api.energy;

import net.minecraftforge.common.ForgeDirection;

public interface IEnergyProvider
{
	IEnergySubject getEnergyProvider(ForgeDirection side);
	
}
