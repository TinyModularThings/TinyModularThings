package speiger.src.api.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class EnergyUsageProvider
{
	private EnergyProvider energyProvider;
	private PowerReceiver bcPower;
	private ForgeDirection face;
	
	private EnergyUsageProvider()
	{
	}
	
	public static EnergyUsageProvider createUsageProvider(IEnergyProvider par0, int side)
	{
		EnergyUsageProvider provider = new EnergyUsageProvider();
		provider.setUpEnergy(par0, side);
		provider.face = ForgeDirection.getOrientation(side);
		return provider;
	}
	
	public static EnergyUsageProvider createUsageProvider(TileEntity par0, int side)
	{
		EnergyUsageProvider provider = new EnergyUsageProvider();
		if (par0 instanceof IEnergyProvider)
		{
			provider.setUpEnergy((IEnergyProvider) par0, side);
			provider.face = ForgeDirection.getOrientation(side);
		}
		else if (par0 instanceof IPowerReceptor)
		{
			provider.setUpEnergy((IPowerReceptor) par0, side);
			provider.face = ForgeDirection.getOrientation(side);
		}
		else
		{
			return null;
		}
		return provider;
	}
	
	public static EnergyUsageProvider createUsageProvider(IPowerReceptor par0, int side)
	{
		EnergyUsageProvider provider = new EnergyUsageProvider();
		provider.setUpEnergy(par0, side);
		provider.face = ForgeDirection.getOrientation(side);
		return provider;
	}
	
	private void setUpEnergy(IEnergyProvider par0, int side)
	{
		energyProvider = par0.getEnergyProvider(ForgeDirection.getOrientation(side));
	}
	
	private void setUpEnergy(IPowerReceptor par0, int side)
	{
		bcPower = par0.getPowerReceiver(ForgeDirection.getOrientation(side));
	}
	
	public boolean requestEnergy()
	{
		if (energyProvider != null)
		{
			return energyProvider.requestEnergy();
		}
		if (bcPower != null)
		{
			return Math.min(bcPower.getMaxEnergyReceived(), bcPower.getMaxEnergyStored() - bcPower.getEnergyStored()) - 20 > 0.0F;
		}
		
		return false;
	}
	
	public int useEnergy(int requesting)
	{
		if (energyProvider != null)
		{
			return energyProvider.useEnergy(requesting, false);
		}
		if (bcPower != null)
		{
			return (int) bcPower.receiveEnergy(Type.MACHINE, -requesting, face);
		}
		
		return 0;
	}
	
	public int addEnergy(int energy)
	{
		if (energyProvider != null)
		{
			return energyProvider.addEnergy(energy, false);
		}
		if (bcPower != null)
		{
			return (int) bcPower.receiveEnergy(Type.MACHINE, energy, face);
		}
		return 0;
	}
	
	public int getEnergyStored()
	{
		if (energyProvider != null)
		{
			return energyProvider.getEnergy();
		}
		if (bcPower != null)
		{
			return (int) bcPower.getEnergyStored();
		}
		return 0;
	}
	
	public int getMaxEnergyStored()
	{
		if (energyProvider != null)
		{
			return energyProvider.getMaxStorage();
		}
		if (bcPower != null)
		{
			return (int) bcPower.getMaxEnergyStored();
		}
		return 0;
	}
	
}
