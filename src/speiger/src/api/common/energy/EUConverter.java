package speiger.src.api.common.energy;

import net.minecraft.nbt.NBTTagCompound;

public class EUConverter
{
	public double storedEU;
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		storedEU += nbt.getDouble("EU");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("EU", storedEU);
	}
	
	public double charge(double amount, EnergyStorage storage)
	{
		double copy = amount;
		storedEU += amount;
		if(storedEU >= ((double)storage.getFreeSpace() * 2.5D))
		{
			amount -= (storedEU - ((double)storage.getFreeSpace() * 2.5D));
			storedEU = ((double)storage.getFreeSpace() * 2.5D);
		}
		if(storedEU >= 2.5D)
		{
			storedEU -= (storage.charge((int)(storedEU / 2.5D)) * 2.5D);
		}
		return copy - amount;
	}
	
	public void discharge(double amount, EnergyStorage storage)
	{
		if(storedEU >= amount)
		{
			storedEU -= amount;
			return;
		}
		storedEU += storage.useEnergy((int)(amount / 2.5) + 1) * 2.5D;
		storedEU -= amount;
	}
}
