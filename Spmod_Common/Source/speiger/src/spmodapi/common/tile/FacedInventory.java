package speiger.src.spmodapi.common.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class FacedInventory extends TileFacing implements IInventory
{
	
	public ItemStack[] inv;
	int maxStack = 64;
	
	public FacedInventory(int size)
	{
		inv = new ItemStack[size];
	}
	
	public FacedInventory changeInventory(int size)
	{
		inv = new ItemStack[size];
		return this;
	}
	
	public FacedInventory setMaxStackSize(int size)
	{
		maxStack = size;
		return this;
	}
	
	@Override
    public int getSizeInventory()
    {
        return this.inv.length;
    }
	
	@Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.inv[par1];
    }
    
	@Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack;

            if (this.inv[par1].stackSize <= par2)
            {
                itemstack = this.inv[par1];
                this.inv[par1] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inv[par1].splitStack(par2);

                if (this.inv[par1].stackSize == 0)
                {
                    this.inv[par1] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }
	
	@Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
        if (this.inv[par1] != null)
        {
            ItemStack itemstack = this.inv[par1];
            this.inv[par1] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

	@Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.inv[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return maxStack;
	}
	
	@Override
	public void openChest()
	{
		
	}
	
	@Override
	public void closeChest()
	{
		
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(this instanceof ISidedInventory)
		{
			return ((ISidedInventory)this).canInsertItem(slotID, par1, 0);
		}
		return super.canMergeItem(par1, slotID);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		inv = new ItemStack[getSizeInventory()];
		NBTTagList list = par1.getTagList("Inventory");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)list.tagAt(i);
			byte slotID = data.getByte("SlotID");
			if(slotID >= 0 && slotID < getSizeInventory())
			{
				inv[slotID] = ItemStack.loadItemStackFromNBT(data);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<getSizeInventory();i++)
		{
			ItemStack stack = inv[i];
			if(stack != null)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("SlotID", (byte)i);
				stack.writeToNBT(data);
				list.appendTag(data);
			}
		}
		par1.setTag("Inventory", list);
	}
	
}
