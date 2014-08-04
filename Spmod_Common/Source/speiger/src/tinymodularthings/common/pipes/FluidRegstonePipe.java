package speiger.src.tinymodularthings.common.pipes;

import speiger.src.tinymodularthings.common.handler.PipeIconHandler;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import buildcraft.BuildCraftTransport;
import buildcraft.api.core.IIconProvider;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportFluids;

public class FluidRegstonePipe extends Pipe<PipeTransportFluids>
{
	public boolean isFull = false;
	
	public FluidRegstonePipe(int itemID)
	{
		super(new PipeTransportFluids(), itemID);
		transport.flowRate = 40;
		transport.travelDelay = 4;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIconProvider getIconProvider()
	{
		return PipeIconHandler.getIcons();
	}

	@Override
	public int getIconIndex(ForgeDirection direction)
	{
		if(isFull)
		{
			return 3;
		}
		return 2;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!this.getWorld().isRemote)
		{
			int i = ((PipeTransportFluids)this.transport).internalTanks[ForgeDirection.UNKNOWN.ordinal()].getAvailable();
			if(i < 250 && isFull)
			{
				isFull = false;
				this.updateNeighbors(true);
			}
			else if(!isFull && !(i < 250))
			{
				isFull = true;
				this.updateNeighbors(true);
			}
		}
		
	}

	@Override
	public int isPoweringTo(int side)
	{
		if(isFull)
		{
			return 15;
		}
		return 0;
	}

	@Override
	public int isIndirectlyPoweringTo(int l)
	{
		return isPoweringTo(l);
	}
	
	
	
}
