package speiger.src.api.common.world.tiles.interfaces.wirelessredstone;

import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessObject.WirelessMode;

public interface IWirelessNetwork
{
	//Will be inited with the WirelessRedstone blocks
	public static IWirelessNetwork wirelessNetwork = null;;
	
	
	
	public void addSource(IWirelessSource par1);
	
	public void removeSource(IWirelessSource par1);
	
	public void addReceiver(IWirelessReceiver par1);
	
	public void removeReceiver(IWirelessReceiver par1);
	
	public void updateSource(IWirelessSource par1);
	
	public void changeFrequency(IWirelessObject par1, int oldID, WirelessMode mode);
	
	public int getSignalFromSide(IWirelessReceiver par1, int side);
	
}
