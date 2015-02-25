package speiger.src.tinymodularthings.common.items.energy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.items.energy.IBCBattery;
import speiger.src.api.common.world.items.energy.IBCBattery.BatteryType;

public class ItemEnergyNet
{
	static ItemEnergyNet energyNet = new ItemEnergyNet();
	
	public static ItemEnergyNet getEnergyNet()
	{
		return energyNet;
	}
	
	boolean randomMode = false;
	boolean ignoreTransferlimit = false;
	boolean setMultiPacket = false;
	SendingMode send = SendingMode.All;
	RequestingMode req = RequestingMode.All;
	
	public ItemEnergyNet activeRandomMode()
	{
		randomMode = true;
		return this;
	}
	
	public ItemEnergyNet setIgnoreTransferlimit()
	{
		ignoreTransferlimit = true;
		return this;
	}
	
	public ItemEnergyNet setRequestMode(RequestingMode par1)
	{
		req = par1;
		return this;
	}
	
	public ItemEnergyNet setSendingMode(SendingMode par1)
	{
		send = par1;
		return this;
	}
	
	public ItemEnergyNet loadSettingsFromItem(ItemStack par1)
	{
		NBTTagCompound data = NBTHelper.getTag(par1, "EnergyNet");
		if(data.getBoolean("Random"))
		{
			randomMode = true;
		}
		if(data.getBoolean("Limit"))
		{
			ignoreTransferlimit = true;
		}
		if(data.getBoolean("Multi"))
		{
			setMultiPacket = true;
		}
		if(data.hasKey("SendMode"))
		{
			int size = data.getInteger("SendMode");
			SendingMode[] array = SendingMode.values();
			if(size >= 0 && array.length > size)
			{
				send = array[size];
			}
		}
		if(data.hasKey("ReqMode"))
		{
			int size = data.getInteger("ReqMode");
			RequestingMode[] array = RequestingMode.values();
			if(size >= 0 && array.length > size)
			{
				req = array[size];
			}
		}
		return this;
	}
	
	private void resetSettings()
	{
		randomMode = false;
		ignoreTransferlimit = false;
		send = SendingMode.All;
		req = RequestingMode.All;
	}
	
	/**
	 * The ItemStack have To Be a IBCBattery!
	 * Sends energy To The PlayerItems which accept Energy.
	 * Send Random means that it picks random from all Valid Items one and charge it.
	 * All Changes to the EnergyNet will be resetet after a work.
	 */
	public void sendEnergyToItems(ItemStack par1, EntityPlayer par2)
	{
		if(par1 == null || !(par1.getItem() instanceof IBCBattery))
		{
			return;
		}
		BatteryContainer sender = new BatteryContainer(par1);
		InventoryPlayer inv = par2.inventory;
		
		if(randomMode)
		{
			List<Integer> ints = getValidReceivers(inv, true);
			if(!ints.isEmpty())
			{
				Collections.shuffle(ints);
				for(int randSlot : ints)
				{
					BatteryContainer receiver = new BatteryContainer(inv.getStackInSlot(randSlot));
					boolean flag = sendEnergy(sender, receiver);
					if(flag && setMultiPacket)
					{
						continue;
					}
					break;
				}
			}
		}
		else
		{
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack slotStack = inv.getStackInSlot(i);
				if(slotStack != null)
				{
					if(slotStack.getItem() instanceof IBCBattery)
					{
						BatteryContainer receiver = new BatteryContainer(slotStack);
						BatteryType type = receiver.getType();
						if((send == SendingMode.All && (type == BatteryType.Machine || type == BatteryType.Storage)) || (send == SendingMode.Battery && type == BatteryType.Storage) || (send == SendingMode.Machine && type == BatteryType.Machine))
						{
							boolean flag = sendEnergy(sender, receiver);
							if(flag && setMultiPacket)
							{
								continue;
							}
							break;
						}
					}
				}
			}
		}
		resetSettings();
	}
	
	public void requestEnergy(ItemStack par1, EntityPlayer par2)
	{
		if(par1 == null || !(par1.getItem() instanceof IBCBattery))
		{
			return;
		}
		BatteryContainer receiver = new BatteryContainer(par1);
		InventoryPlayer inv = par2.inventory;
		if(randomMode)
		{
			List<Integer> ints = getValidReceivers(inv, false);
			if(!ints.isEmpty())
			{
				Collections.shuffle(ints);
				for(int randSlot : ints)
				{
					BatteryContainer sender = new BatteryContainer(inv.getStackInSlot(randSlot));
					boolean flag = sendEnergy(sender, receiver);
					if(flag && setMultiPacket)
					{
						continue;
					}
					break;
				}
			}
		}
		else
		{
			for(int i = 0;i<inv.getSizeInventory();i++)
			{
				ItemStack slotStack = inv.getStackInSlot(i);
				if(slotStack != null)
				{
					if(slotStack.getItem() instanceof IBCBattery)
					{
						BatteryContainer sender = new BatteryContainer(slotStack);
						BatteryType type = sender.getType();
						if((req == RequestingMode.All && (type == BatteryType.Generator || type == BatteryType.Storage) || (req == RequestingMode.Battery && type == BatteryType.Storage) || (req == RequestingMode.Generator && type == BatteryType.Storage)))
						{
							boolean flag = sendEnergy(sender, receiver);
							if(flag && setMultiPacket)
							{
								continue;
							}
							break;
						}
					}
				}
			}
		}
		resetSettings();
	}
	
	/**
	 * Provider Means the Sender is a Provider!
	 */
	private List<Integer> getValidReceivers(InventoryPlayer par1, boolean provider)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		for(int i = 0;i<par1.getSizeInventory();i++)
		{
			ItemStack stack = par1.getStackInSlot(i);
			if(stack != null)
			{
				if(stack.getItem() instanceof IBCBattery)
				{
					if(provider)
					{
						BatteryContainer receiver = new BatteryContainer(stack);
						BatteryType type = receiver.getType();
						if((send == SendingMode.All && (type == BatteryType.Machine || type == BatteryType.Storage)) || (send == SendingMode.Battery && type == BatteryType.Storage) || (send == SendingMode.Machine && type == BatteryType.Machine))
						{
							if(receiver.requestedEnergy() > 0)
							{
								ints.add(i);
							}
						}
					}
					else
					{
						BatteryContainer sender = new BatteryContainer(stack);
						BatteryType type = sender.getType();
						if((req == RequestingMode.All && (type == BatteryType.Generator || type == BatteryType.Storage) || (req == RequestingMode.Battery && type == BatteryType.Storage) || (req == RequestingMode.Generator && type == BatteryType.Storage)))
						{
							if(sender.getEnergyToSend() > 0)
							{
								ints.add(i);
							}
						}
					}
				}
			}
		}
		return ints;
	}
	
	private boolean sendEnergy(BatteryContainer sender, BatteryContainer receiver)
	{
		if(ignoreTransferlimit)
		{
			int toSend = sender.getEnergyToSend();
			if(toSend <= 0)
			{
				return false;
			}
			int toDraw = receiver.charge(toSend);
			if(toDraw <= 0)
			{
				return false;
			}
			sender.discharge(toDraw);
		}
		else
		{
			int toSend = Math.min(sender.getEnergyToSend(), sender.getTransferlimit());
			if(toSend <= 0)
			{
				return false;
			}
			int toReceive = Math.min(toSend, receiver.getTransferlimit());
			if(toReceive <= 0)
			{
				return false;
			}
			int received = receiver.charge(toReceive);
			if(received <= 0)
			{
				return false;
			}
			sender.discharge(received);
		}
		return true;
	}
	
	public static enum SendingMode
	{
		Machine,
		Battery,
		All;
	}
	
	public static enum RequestingMode
	{
		Generator,
		Battery,
		All;
	}
	
	public static class BatteryContainer
	{
		final ItemStack batteryData;
		final IBCBattery battery;
		
		public BatteryContainer(ItemStack par1)
		{
			this((IBCBattery)par1.getItem(), par1);
		}
		
		public BatteryContainer(IBCBattery par1, ItemStack par2)
		{
			battery = par1;
			batteryData = par2;
		}
		
		public BatteryType getType()
		{
			return battery.getType(batteryData);
		}
		
		public int getStoredEnergy()
		{
			return battery.getStoredMJ(batteryData);
		}
		
		public int getMaxStorage()
		{
			return battery.getMaxMJStorage(batteryData);
		}
		
		public int requestedEnergy()
		{
			if(battery.requestEnergy(batteryData))
			{
				return battery.getRequestedAmount(batteryData);
			}
			return 0;
		}
		
		public int getEnergyToSend()
		{
			if(battery.wantToSendEnergy(batteryData))
			{
				return battery.energyToSend(batteryData);
			}
			return 0;
		}
		
		public int getTransferlimit()
		{
			return battery.getTransferlimit(batteryData);
		}
		
		public boolean isNotFullAndNotEmpty()
		{
			if(battery.isEmpty(batteryData) || battery.isFull(batteryData))
			{
				return false;
			}
			return true;
		}
		
		public int charge(int amount)
		{
			return charge(amount, false);
		}
		
		public int charge(int amount, boolean simulate)
		{
			return battery.charge(batteryData, amount, simulate);
		}
		
		public int discharge(int amount)
		{
			return discharge(amount, false);
		}
		
		public int discharge(int amount, boolean simulate)
		{
			return battery.discharge(batteryData, amount, simulate);
		}
	}
}
