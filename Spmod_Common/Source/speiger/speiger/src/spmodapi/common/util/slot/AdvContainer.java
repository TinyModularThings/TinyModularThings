package speiger.src.spmodapi.common.util.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import speiger.src.api.inventory.TankSlot;

public abstract class AdvContainer extends Container
{
	
	private ArrayList<Slot> slots = new ArrayList<Slot>();
	private ArrayList<Slot> fakeSlots = new ArrayList<Slot>();
	private ArrayList<TankSlot> tanks = new ArrayList<TankSlot>();
	
	public AdvContainer(InventoryPlayer par1)
	{
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			addSlotToContainer(new Slot(par1, var3, 8 + var3 * 18, 142));
		}
	}
	
	public AdvContainer()
	{
	};
	
	public void addSpmodSlotToContainer(Slot par1)
	{
		slots.add(par1);
		addSlotToContainer(par1);
	}
	
	public void addTankSlot(TankSlot par1)
	{
		tanks.add(par1);
	}
	
	public ArrayList<TankSlot> getTanks()
	{
		return tanks;
	}
	
	public ArrayList<Slot> getAllSlots()
	{
		return slots;
	}
	
	public void addFakeSlot(Slot par1)
	{
		fakeSlots.add(par1);
	}
	
	public ArrayList<Slot> getFakeSlots()
	{
		return fakeSlots;
	}
	
}
