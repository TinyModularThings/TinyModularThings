package speiger.src.tinymodularthings.common.plugins.BC.core;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import buildcraft.BuildCraftCore;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;

public class TileEntityModifiedFurnace extends TileEntityFurnace implements
		IActionReceptor
{
	public boolean paused = false;
	
	@Override
	public void updateEntity()
	{
		
		if (paused)
		{
			if (!worldObj.isRemote)
			{
				if (furnaceBurnTime > 0)
				{
					furnaceBurnTime--;
				}
			}
		}
		else
		{
			super.updateEntity();
		}
		if (!worldObj.isRemote && paused)
		{
			paused = false;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		paused = par1.getBoolean("Paused");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setBoolean("Paused", paused);
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		if (action == BuildCraftCore.actionOn)
		{
			paused = false;
		}
		else if (action == BuildCraftCore.actionOff)
		{
			paused = true;
		}
	}
	
}
