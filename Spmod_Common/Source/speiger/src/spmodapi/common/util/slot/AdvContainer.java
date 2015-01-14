package speiger.src.spmodapi.common.util.slot;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.inventory.slot.TankSlot;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AdvContainer extends Container
{
	
	private ArrayList<SpmodSlot> slots = new ArrayList<SpmodSlot>();
	private ArrayList<SpmodSlot> fakeSlots = new ArrayList<SpmodSlot>();
	private ArrayList<TankSlot> tanks = new ArrayList<TankSlot>();
	private AdvTile tile = null;
	private EntityPlayer player;
	private int offsetX = 0;
	private int offsetY = 0;
	
	public AdvContainer(EntityPlayer par1)
	{
		player = par1;
	}
	
	public AdvContainer(InventoryPlayer par1)
	{
		this(par1.player);
	}
	
	public AdvContainer(InventoryPlayer par1, AdvTile par2)
	{
		this(par1);
		this.setTile(par2);
		par2.addContainerSlots(this);
		setInventory(par1);
	}
	
	public AdvContainer setTile(AdvTile par1)
	{
		tile = par1;
		tile.onPlayerOpenContainer(player);
		return this;
	}
	
	public AdvTile getTile()
	{
		return tile;
	}
	
	public AdvContainer setOffset(int x, int y)
	{
		offsetX = x;
		offsetY = y;
		return this;
	}
	
	public void setInventory(InventoryPlayer par1)
	{
		int var3;
		
		for(var3 = 0;var3 < 3;++var3)
		{
			for(int var4 = 0;var4 < 9;++var4)
			{
				addSlotToContainer(new Slot(par1, var4 + var3 * 9 + 9, 8 + offsetX + var4 * 18, 84 + offsetY + var3 * 18));
			}
		}
		
		for(var3 = 0;var3 < 9;++var3)
		{
			addSlotToContainer(new Slot(par1, var3, 8 + offsetX + var3 * 18, 142 + offsetY));
		}
	}
	
	public void addSpmodSlotToContainer(SpmodSlot par1)
	{
		slots.add(par1);
		addSlotToContainer(par1);
	}
	
	public void addSpmodSlot(IInventory par1, int id, int x, int y)
	{
		addSpmodSlotToContainer(new SpmodSlot(par1, id, x, y));
	}
	
	public void addTankSlot(TankSlot par1)
	{
		tanks.add(par1);
	}
	
	public ArrayList<TankSlot> getTanks()
	{
		return tanks;
	}
	
	public ArrayList<SpmodSlot> getAllSlots()
	{
		return slots;
	}
	
	public void addFakeSlot(SpmodSlot par1)
	{
		fakeSlots.add(par1);
	}
	
	public ArrayList<SpmodSlot> getFakeSlots()
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
			for(int i = 0;i < this.crafters.size();i++)
			{
				ICrafting craft = (ICrafting)crafters.get(i);
				tile.onSendingGuiInfo(this, craft);
			}
		}
	}
	
	@Override
	public boolean mergeItemStack(ItemStack par1ItemStack, int par2, int par3, boolean par4)
	{
		if(tile != null)
		{
			while(!tile.canMergeItem(par1ItemStack, par2) && par2 < par3)
			{
				par2++;
			}
		}
		else
		{
			while(!((Slot)this.inventorySlots.get(par2)).isItemValid(par1ItemStack) && par2 < par3)
			{
				par2++;
			}
		}
		
		if(par2 == par3)
		{
			return false;
		}
		return super.mergeItemStack(par1ItemStack, par2, par3, par4);
	}
	
	@Override
	public ItemStack slotClick(int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
	{
		if(tile != null)
		{
			if(tile.onSlotClicked(this, par1, par2, par3, par4EntityPlayer))
			{
				return null;
			}
		}
		return super.slotClick(par1, par2, par3, par4EntityPlayer);
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1)
	{
		super.onCraftMatrixChanged(par1);
		if(tile != null)
		{
			tile.onMatrixChanged(par1);
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		if(tile != null)
		{
			int size = tile.getSizeInventory();
			ItemStack itemstack = null;
			Slot slot = (Slot)inventorySlots.get(par2);
			
			if(slot != null && slot.getHasStack())
			{
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				
				if(par2 >= size)
				{
					if(!mergeItemStack(itemstack1, 0, size, false))
					{
						return null;
					}
				}
				else if(par2 >= 0 && par2 < size)
				{
					if(!mergeItemStack(itemstack1, size, 36+size, true))
					{
						return null;
					}
				}
				else if(par2 > size + 27 && par2 <= size && !this.mergeItemStack(itemstack1, size, size+27, false))
				{
					return null;
				}
				
				if(itemstack1.stackSize == 0)
				{
					slot.putStack((ItemStack)null);
				}
				else
				{
					slot.onSlotChanged();
				}
				
				if(itemstack1.stackSize == itemstack.stackSize)
				{
					return null;
				}
				
				slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
			}
			return itemstack;	
		}
		return super.transferStackInSlot(par1EntityPlayer, par2);
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
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		if(tile != null)
		{
			return tile.isUseableByPlayer(entityplayer);
		}
		return true;
	}
}
