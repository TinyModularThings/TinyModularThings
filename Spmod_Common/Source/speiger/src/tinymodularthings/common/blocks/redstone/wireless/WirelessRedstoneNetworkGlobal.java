package speiger.src.tinymodularthings.common.blocks.redstone.wireless;

import java.util.HashMap;

import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.*;
import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessObject.WirelessMode;

public class WirelessRedstoneNetworkGlobal implements IWirelessNetwork
{
	static HashMap<Integer, WirelessRedstoneNetworkLocal> wirelessDimension = new HashMap<Integer, WirelessRedstoneNetworkLocal>();
	
	@Override
	public void addSource(IWirelessSource par1)
	{
		if(par1 == null)
		{
			return;
		}
		if(par1.isGlobal())
		{
			
		}
	}

	@Override
	public void removeSource(IWirelessSource par1)
	{
		if(par1 == null)
		{
			return;
		}
	}

	@Override
	public void addReceiver(IWirelessReceiver par1)
	{
		if(par1 == null)
		{
			return;
		}
	}

	@Override
	public void removeReceiver(IWirelessReceiver par1)
	{
		if(par1 == null)
		{
			return;
		}
	}

	@Override
	public void updateSource(IWirelessSource par1)
	{
		if(par1 == null)
		{
			return;
		}
	}

	@Override
	public void changeFrequency(IWirelessObject par1, int oldID, WirelessMode mode)
	{
		if(par1 == null)
		{
			return;
		}
	}

	@Override
	public int getSignalFromSide(IWirelessReceiver par1, int side)
	{
		if(par1 == null)
		{
			return 0;
		}
		return 0;
	}
	
}
