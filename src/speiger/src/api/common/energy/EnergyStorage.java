package speiger.src.api.common.energy;

import net.minecraft.nbt.NBTTagCompound;
/**
 * 
 * @author Speiger
 *
 * Energy Storage Class.
 * Energy it is uses as base is MJ
 * with custom Convertierung, which should balance itself.
 * 1 MJ = 100 RF
 * 1 MJ = 2,5EU
 *
 */
public class EnergyStorage
{
	public int storedEnergy;
	public int maxEnergy;
	
	public EnergyStorage(int max, int stored)
	{
		this(max);
		storedEnergy = stored;
	}
	
	public EnergyStorage(int max)
	{
		maxEnergy = max;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		maxEnergy = nbt.getInteger("Max");
		storedEnergy = nbt.getInteger("Stored");
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Max", maxEnergy);
		nbt.setInteger("Stored", storedEnergy);
	}
	
	public int charge(int amount)
	{
		storedEnergy += amount;
		if(storedEnergy > maxEnergy)
		{
			amount -= storedEnergy - maxEnergy;
		}
		return amount;
	}
	
	public int charge(int amount, boolean simulate)
	{
		if(!simulate)
		{
			return charge(amount);
		}
		int stored = storedEnergy;
		stored+= amount;
		if(stored > maxEnergy)
		{
			amount -= stored - maxEnergy;
		}
		return amount;
	}
	
	public int useEnergy(int amount, boolean simulate)
	{
		if(!simulate)
		{
			return useEnergy(amount);
		}
		int stored = storedEnergy;
		stored -= amount;
		if(stored < 0)
		{
			amount += stored;
		}
		return amount;
	}
	
	public int useEnergy(int amount)
	{
		storedEnergy -= amount;
		if(storedEnergy < 0)
		{
			amount += storedEnergy;
			storedEnergy = 0;
		}
		return amount;
	}
	
	public int getStoredEnergy()
	{
		return storedEnergy;
	}
	
	public int getMaxEnergy()
	{
		return maxEnergy;
	}
	
	public int getFreeSpace()
	{
		return maxEnergy - storedEnergy;
	}
}
