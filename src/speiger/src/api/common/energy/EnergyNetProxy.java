package speiger.src.api.common.energy;

import net.minecraft.inventory.IInventory;

public class EnergyNetProxy
{
	public static IItemEnergyNet energyNet;
	
	public interface IItemEnergyNet
	{
		public void startCalculation(IInventory inv);
		
		public void addElectricItemToSlot(int slot, EnergyNetType type);
		
		public void finishCalculation(IInventory inv);
	}
	
	public enum EnergyNetType
	{
		Provider,
		Battery,
		PassiveRequester,
		ActiveRequester;
	}
}
