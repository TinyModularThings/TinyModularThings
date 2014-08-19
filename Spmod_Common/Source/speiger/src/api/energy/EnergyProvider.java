package speiger.src.api.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;

public class EnergyProvider
{
	/**
	 * Stored Energy
	 */
	int storedEnergy = 0;
	
	/**
	 * Max Storage of the Provider
	 */
	int maxStorage;
	
	/**
	 * Has Powerloss
	 */
	boolean powerLoss = false;
	
	/**
	 * Powerloss overTime in promille
	 */
	int powerloss = 0;
	
	/**
	 * Current waiting Time.
	 */
	int count = 0;
	
	/**
	 * Time before the Powerloss get appled.
	 */
	final int countdown = Short.MAX_VALUE / 3;
	
	/**
	 * How much the BC Energy Provider can recive.
	 */
	int transferlimit = 0;
	
	/**
	 * If the transferlimit get Forced.
	 */
	boolean forceTransferlimit = false;
	
	
	public EnergyProvider(TileEntity tile, int maxStorage)
	{
		this.maxStorage = maxStorage;
		if (tile != null && tile instanceof IPowerReceptor)
		{
			initBC((IPowerReceptor) tile);
		}
	}
	
	public EnergyProvider setPowerLoss(int promille)
	{
		int loss = promille;
		if (loss > 999)
		{
			loss = 999;
		}
		powerloss = loss;
		powerLoss = true;
		return this;
	}
	
	public void update()
	{
		if (powerLoss)
		{
			count++;
			if (count >= countdown)
			{
				count = 0;
				applyPowerLoss();
			}
		}
		if (BCPower != null)
		{
			BCUpdate();
		}
	}
	
	public boolean isFull()
	{
		return getRequestedEnergy() == 0;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		if (BCPower != null)
		{
			BCPower.writeToNBT(nbt, "BCPower");
		}
		nbt.setInteger("Countdown", count);
		nbt.setInteger("PowerLoss", powerloss);
		nbt.setInteger("MaxStorage", maxStorage);
		nbt.setInteger("Stored", storedEnergy);
		nbt.setBoolean("lossEnabled", powerLoss);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (BCPower != null)
		{
			BCPower.readFromNBT(nbt, "BCPower");
		}
		count = nbt.getInteger("Countdown");
		powerloss = nbt.getInteger("PowerLoss");
		maxStorage = nbt.getInteger("MaxStorage");
		storedEnergy = nbt.getInteger("Stored");
		powerLoss = nbt.getBoolean("lossEnabled");
	}
	
	public int getMaxStorage()
	{
		return maxStorage;
	}
	
	public int useEnergy(int wantToUse, boolean simulate)
	{
		if (wantToUse > storedEnergy)
		{
			int between = storedEnergy;
			
			if (!simulate)
			{
				storedEnergy = 0;
			}
			return between;
		}
		else
		{
			if (!simulate)
			{
				storedEnergy -= wantToUse;
			}
			return wantToUse;
		}
	}
	
	/**
	 * Powerloss function. But not so extreme as BC
	 */
	private void applyPowerLoss()
	{
		if (storedEnergy > 0)
		{
			double loss = (double)storedEnergy / (double)1000;
			int losedPower = (int) (loss * powerloss);
			storedEnergy -= losedPower;
		}
	}
	
	public int getEnergy()
	{
		return storedEnergy;
	}
	
	public void setEnergy(int amount)
	{
		storedEnergy = amount;
	}
	
	public int addEnergy(int amount, boolean simulate)
	{
		if (storedEnergy <= 0)
		{
			storedEnergy = 0;
		}
		int limit = amount;
		if(this.forceTransferlimit)
		{
			limit = Math.min(limit, transferlimit);
		}
		
		if(storedEnergy + limit > this.maxStorage)
		{
			int added = limit - ((storedEnergy + limit) - maxStorage);
			
			if(!simulate)
			{
				storedEnergy = this.maxStorage;
			}
			return added;
		}
		else
		{
			if(!simulate)
			{
				storedEnergy += limit;
			}
			return limit;
		}
	}
	
	public int getRequestedEnergy()
	{
		int left = maxStorage - storedEnergy;
		return left;
	}
	
	public void setTransferlimtit(int limit)
	{
		if(limit < 0)
		{
			transferlimit = 0;
			forceTransferlimit = false;
		}
		else
		{
			transferlimit = limit;
			forceTransferlimit = true;
		}
	}
	
	/** BuildCraft Side **/
	
	private PowerHandler BCPower;
	
	private void initBC(IPowerReceptor par0)
	{
		BCPower = new PowerHandler(par0, Type.MACHINE);
		BCPower.configure(1, 1000, 0, 2000);
		BCPower.setPerdition(new NullPowerLossCalculator());
	}
	
	/**
	 * Normal PowerHandler.
	 * 
	 * @return BCPowerHandler
	 */
	public PowerHandler getBCPowerProvider()
	{
		return BCPower;
	}
	
	/**
	 * Save powerhandler that get not so much powerloss as normal powerHandler,
	 * 
	 * @return PowerHandler
	 */
	public PowerReceiver getSaveBCPowerProvider()
	{
		if (BCPower.getEnergyStored() > 0 && storedEnergy >= maxStorage)
		{
			return null;
		}
		return BCPower.getPowerReceiver();
	}
	
	private void BCUpdate()
	{
		if(forceTransferlimit)
		{
			int between = maxStorage - storedEnergy;
			int minimum = Math.min(transferlimit, between);
			BCPower.configure(1, minimum, 0, 2000);
		}
		else
		{
			int between = maxStorage - storedEnergy;
			int minimum = Math.min(1000, between);
			BCPower.configure(1, minimum, 0, 2000);
		}
		
		double energy = BCPower.getEnergyStored();
		int roundedEnergy = (int) energy;
		roundedEnergy -= addEnergy(roundedEnergy, false);
		BCPower.setEnergy(roundedEnergy);
		
	}
	
	public boolean requestEnergy()
	{
		if ((maxStorage - storedEnergy) - 50 > 0)
		{
			return true;
		}
		return false;
	}
	
}
