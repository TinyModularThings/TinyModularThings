package speiger.src.tinymodularthings.common.blocks.redstone.wireless;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.World;
import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessObject;
import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessObject.WirelessMode;
import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessReceiver;
import speiger.src.api.common.world.tiles.interfaces.wirelessredstone.IWirelessSource;

public class WirelessRedstoneNetworkLocal
{
	final World world;
	boolean networkUpdate = false;
	Map<Integer, byte[]> signals = new WeakHashMap<Integer, byte[]>();
	Map<Integer, List<IWirelessSource>> sources = new WeakHashMap<Integer, List<IWirelessSource>>();
	Map<Integer, List<IWirelessReceiver>> receivers = new WeakHashMap<Integer, List<IWirelessReceiver>>();
	Map<Integer, String> customFrequencyNames = new WeakHashMap<Integer, String>();
	
	public WirelessRedstoneNetworkLocal(World par1)
	{
		world = par1;
	}
	
	public void addSource(IWirelessSource par1)
	{
		int id = par1.getFrequency(WirelessMode.Source);
		if(!sources.containsKey(id))
		{
			sources.put(id, new ArrayList<IWirelessSource>());
		}
		if(!sources.get(id).contains(par1))
		{
			sources.get(id).add(par1);
			updateSources(id);
		}
	}
	
	public void removeSource(IWirelessSource par1)
	{
		int id = par1.getFrequency(WirelessMode.Source);
		if(sources.containsKey(id) && sources.get(id).contains(par1))
		{
			sources.remove(par1);
			updateSources(id);
		}
	}
	
	public void addReceiver(IWirelessReceiver par1)
	{
		int id = par1.getFrequency(WirelessMode.Receiver);
		if(!receivers.containsKey(id))
		{
			receivers.put(id, new ArrayList<IWirelessReceiver>());
		}
		if(!receivers.get(id).contains(par1))
		{
			receivers.get(id).add(par1);
			par1.onNetworkChange();
		}
	}
	
	public void updateSource(IWirelessSource par1)
	{
		int id = par1.getFrequency(WirelessMode.Source);
		if(networkUpdate && par1 instanceof IWirelessReceiver)
		{
			return;
		}
		updateSources(id);
	}
	
	private void updateSources(int id)
	{
		byte[] signalStrengh = new byte[6];
		if(sources.containsKey(id))
		{
			for(IWirelessSource source : sources.get(id))
			{
				for(int i = 0;i<6;i++)
				{
					signalStrengh[i] = (byte)Math.max(signalStrengh[i], source.getPowerInput(i));
				}
			}
		}
		signals.put(id, signalStrengh);
		if(receivers.containsKey(id))
		{
			networkUpdate = true;
			for(IWirelessReceiver receiver : receivers.get(id))
			{
				receiver.onNetworkChange();
			}
			networkUpdate = false;
		}
	}
	
	public void changeFrequency(IWirelessObject par1, int oldID, WirelessMode mode)
	{
		if(mode == WirelessMode.Source)
		{
			if(sources.containsKey(oldID))
			{
				sources.get(oldID).remove(par1);
			}
			addSource((IWirelessSource)par1);
		}
		else if(mode == WirelessMode.Receiver)
		{
			if(receivers.containsKey(oldID))
			{
				receivers.get(oldID).remove(par1);
				addReceiver((IWirelessReceiver)par1);
			}
		}
	}
	
	public int getSignalFromSide(IWirelessReceiver par1, int side)
	{
		int id = par1.getFrequency(WirelessMode.Receiver);
		byte[] signal = signals.containsKey(id) ? signals.get(id) : signals.put(id, (signal = new byte[6]));
		if(par1.caresAboutStrengh())
		{
			return signal[side];
		}
		return signal[side] > 0 ? 15 : 0;
	}
}
