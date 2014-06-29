package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.energy.EnergyProvider;
import buildcraft.api.power.PowerHandler.PowerReceiver;

public class SelfPoweredBucketFiller extends BucketFillerBasic
{
	public SelfPoweredBucketFiller()
	{
		this.max = 150;
	}
	
	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(this.provider.getEnergy() < 100)
			{
				this.provider.setEnergy(1000);
			}
		}
		super.onTick();
	}

	@Override
	public EnergyProvider getEnergyProvider(ForgeDirection side)
	{
		return null;
	}

	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return null;
	}
	
}
