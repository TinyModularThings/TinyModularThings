package speiger.src.spmodapi.common.util.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.inventory.slot.TankSlot;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AdvContainer extends Container
{
	
	private ArrayList<Slot> slots = new ArrayList<Slot>();
	private ArrayList<Slot> fakeSlots = new ArrayList<Slot>();
	private ArrayList<TankSlot> tanks = new ArrayList<TankSlot>();
	private AdvTile tile = null;
	
	public void setTile(AdvTile par1)
	{
		tile = par1;
	}
	
	public AdvTile getTile()
	{
		return tile;
	}
	
	public AdvContainer(InventoryPlayer par1, int x, int y)
	{
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, x + var4 * 18, y + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			addSlotToContainer(new Slot(par1, var3, x + var3 * 18, y + 58));
		}
	}
	
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
	
	public void setInventory(InventoryPlayer par1)
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
	
	public void setInventory(InventoryPlayer par1, int x, int y)
	{
		int var3;
		
		for (var3 = 0; var3 < 3; ++var3)
		{
			for (int var4 = 0; var4 < 9; ++var4)
			{
				addSpmodSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, x + var4 * 18, y + var3 * 18));
			}
		}
		
		for (var3 = 0; var3 < 9; ++var3)
		{
			addSpmodSlotToContainer(new Slot(par1, var3, x + var3 * 18, y+58));
		}
	}
	
	public AdvContainer(InventoryPlayer par1, AdvTile par2)
	{
		par2.onPlayerOpenContainer(par1.player);
		tile = par2;
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
	
	
	
	@Override
	public void onContainerClosed(EntityPlayer par1EntityPlayer)
	{
		super.onContainerClosed(par1EntityPlayer);
		if(tile != null)
		{
			tile.onPlayerCloseContainer(par1EntityPlayer);
		}
	}
	
	

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		if(tile != null)
		{
			for(int i = 0;i<this.crafters.size();i++)
			{
				ICrafting craft = (ICrafting)crafters.get(i);
				tile.onSendingGuiInfo(this, craft);
			}
		}
	}
	
	

	@Override
	protected boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
	{
		while(!((Slot)this.inventorySlots.get(par2)).isItemValid(par1ItemStack) && par2 < par3)
		{
			par2++;
		}
		if(par2 == par3)
		{
			return false;
		}
		return super.mergeItemStack(par1ItemStack, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if(tile != null)
		{
			tile.onReciveGuiInfo(par1, par2);
		}
	}

	public void clearInventory()
	{
		fakeSlots.clear();
		slots.clear();
		tanks.clear();
	}
	
}
