package speiger.src.api.common.energy;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.nbt.NBTTagCompound;

public class RFConverter
{
	public int storedRF;
	
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		storedRF = nbt.getInteger("RF");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("RF", storedRF);
	}
	
	public int charge(int amount, EnergyStorage storage, boolean simulate)
	{
		if(simulate)
		{
			int storing = storedRF;
			storing+= amount;
			if(storing >= (storage.getFreeSpace() * 100))
			{
				amount -= (storing - (storage.getFreeSpace() * 100));
			}
			return amount;
		}
		storedRF += amount;
		if(storedRF >= (storage.getFreeSpace() * 100))
		{
			amount -= (storedRF - (storage.getFreeSpace() * 100));
			storedRF = (storage.getFreeSpace() * 100);
		}
		if(storedRF >= 100)
		{
			storedRF -= storage.charge(storedRF / 100) * 100;
		}
		return amount;
	}
	
	public int discharge(int amount, EnergyStorage storage, boolean simulate)
	{
		if(simulate)
		{
			int stored = storedRF;
			if(stored >= amount)
			{
				return amount;
			}
			stored += storage.useEnergy((amount / 100) + 1, true) * 100;
			return Math.min(amount, stored);
		}
		if(storedRF >= amount)
		{
			storedRF -= amount;
			return amount;
		}
		storedRF += storage.useEnergy((amount / 100) + 1) * 100;
		storedRF -= amount;
		if(storedRF < 0)
		{
			amount += storedRF;
			storedRF = 0;
		}
		return amount;
	}
}
