package speiger.src.tinymodularthings.common.blocks.redstone.detector;

import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.tiles.machine.IDetector;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;

public class DetectorInventory implements IDetectorModul
{
	int type;
	
	public DetectorInventory(int par1)
	{
		type = par1;
	}
	
	@Override
	public void onUnloading(IDetector detector)
	{
		
	}
	
	@Override
	public int getTickRate(IDetector detector)
	{
		return 5;
	}
	
	@Override
	public boolean doesHaveTileEntityTick(IDetector detector)
	{
		return true;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		
	}
	
	@Override
	public void onTileEntityTick(IDetector par1)
	{
		boolean working = false;
		TileEntity tile = par1.getBlockInfront().getTileEntity();
		int dir = ForgeDirection.getOrientation(par1.getFacing()).getOpposite().ordinal();
		switch(type)
		{
			case 0:
				working = empty(tile, dir);
				break;
			case 1:
				working = notEmpty(tile, dir);
				break;
			case 2:
				working = full(tile, dir);
				break;
			case 3:
				working = completeFull(tile, dir);
				break;
		}
		if(working)
		{
			par1.setRedstoneSignal(15);
			return;
		}
		par1.setRedstoneSignal(0);
	}
	
	@Override
	public void onBlockUpdate(IDetector par1)
	{
		
	}
	
	private boolean completeFull(TileEntity par1, int facing)
	{
		if(par1 == null)
		{
			return false;
		}
		if(!(par1 instanceof IInventory))
		{
			return false;
		}
		IInventory inv = (IInventory)par1;
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory sidedInv = (ISidedInventory)inv;
			int[] slots = sidedInv.getAccessibleSlotsFromSide(facing);
			if(slots == null || slots.length == 0)
			{
				return false;
			}
			boolean flag = false;
			for(int i = 0;i<slots.length;i++)
			{
				flag = !checkSlot(inv, slots[i], 2) || flag;
			}
			if(flag)return false;
			return true;
		}
		boolean flag = false;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			flag = !checkSlot(inv, i, 2) || flag;
		}
		if(flag)return false;
		return true;
	}
	
	private boolean full(TileEntity par1, int facing)
	{
		if(par1 == null)
		{
			return false;
		}
		if(!(par1 instanceof IInventory))
		{
			return false;
		}
		IInventory inv = (IInventory)par1;
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory sidedInv = (ISidedInventory)inv;
			int[] slots = sidedInv.getAccessibleSlotsFromSide(facing);
			if(slots == null || slots.length == 0)
			{
				return false;
			}
			boolean flag = false;
			for(int i = 0;i<slots.length;i++)
			{
				flag = !checkSlot(sidedInv, slots[i], 1) || flag;
			}
			if(flag)return false;
			return true;
		}
		boolean flag = false;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			flag = !checkSlot(inv, i, 1) || flag;
		}
		if(flag)return false;
		return true;
	}
	
	private boolean notEmpty(TileEntity par1, int facing)
	{
		if(par1 == null)
		{
			return false;
		}
		if(!(par1 instanceof IInventory))
		{
			return false;
		}
		IInventory inv = (IInventory)par1;
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory sidedInv = (ISidedInventory)inv;
			int[] slots = sidedInv.getAccessibleSlotsFromSide(facing);
			if(slots == null || slots.length == 0)
			{
				return false;
			}
			boolean flag = false;
			for(int i = 0;i<slots.length;i++)
			{
				flag = checkSlot(sidedInv, slots[i], 1) || flag;
			}
			if(flag) return true;
			return false;
		}
		boolean flag = false;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			flag = checkSlot(inv, i, 1) || flag;
		}
		if(flag) return true;
		return false;
	}
	
	private boolean empty(TileEntity par1, int facing)
	{
		if(par1 == null)
		{
			return false;
		}
		if(!(par1 instanceof IInventory))
		{
			return false;
		}
		IInventory inv = (IInventory)par1;
		if(inv instanceof ISidedInventory)
		{
			ISidedInventory sidedInv = (ISidedInventory)inv;
			int[] slots = sidedInv.getAccessibleSlotsFromSide(facing);
			if(slots == null || slots.length == 0)
			{
				return false;
			}
			boolean result = false;
			for(int i = 0;i<slots.length;i++)
			{
				result = !checkSlot(sidedInv, slots[i], 0) || result;
			}
			if(!result) return true;
			return false;
		}
		boolean result = false;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			result = !checkSlot(inv, i, 0) || result;
		}
		if(!result)	return true;
		return false;
	}
	
	public boolean checkSlot(IInventory par1, int slotID, int checkingType)
	{
		ItemStack stack = par1.getStackInSlot(slotID);
		switch(checkingType)
		{
			case 0: return stack == null || stack.stackSize <= 0;
			case 1: return stack != null;
			case 2: return stack != null && stack.stackSize >= stack.getMaxStackSize();
		}
		return false;
	}

	@Override
	public void addItemInformation(List par1)
	{
		switch(type)
		{
			case 0:
				par1.add("Detects if the Inventory is Totally Empty");
				break;
			case 1:
				par1.add("Detects if the Inventory is Not Empty");
				break;
			case 2:
				par1.add("Detects if the Inventory is Full");
				break;
			case 3:
				par1.add("Detects if the Inventory is 100% Full");
				break;
		}
	}
}
