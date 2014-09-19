package speiger.src.tinymodularthings.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.spmodapi.common.util.slot.BasicItemInventory;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class PotionInventory extends BasicItemInventory
{
	EntityPlayer player;
	String id;
	
	public PotionInventory(InventoryPlayer par1, ItemStack par2)
	{
		super(36);
		player = par1.player;
		id = NBTHelper.getTag(par2, "Bag").getString("ID");
		readFromNBT(NBTHelper.getTag(par2, "Bag").getCompoundTag("Inventory"));
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("Items");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagCompound tag = (NBTTagCompound)list.tagAt(i);
			int slot = tag.getInteger("Slot");
			if(slot >= 0 && slot < 36)
			{
				this.inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void onInventoryChanged()
	{
		writeToNBT();
	}
	
	
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if(itemstack != null)
		{
			if(itemstack.itemID == Item.glassBottle.itemID)
			{
				return true;
			}
			if(itemstack.getItem() instanceof ItemPotion)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public void closeChest()
	{
		writeToNBT();
	}

	public void writeToNBT()
	{
		InventoryPlayer inv = player.inventory;
		ItemStack stack = null;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack cu = inv.getStackInSlot(i);
			if(cu != null && cu.itemID == TinyItems.potionBag.itemID)
			{
				String id = NBTHelper.getTag(cu, "Bag").getString("ID");
				if(id.equalsIgnoreCase(this.id))
				{
					stack = cu;
					break;
				}
			}
		}
		
		if(stack != null)
		{
			NBTTagCompound nbt = NBTHelper.getTag(stack, "Bag").getCompoundTag("Inventory");
			NBTTagList list = new NBTTagList();
			for(int i = 0;i<this.inv.length;i++)
			{
				if(this.getStackInSlot(i) != null)
				{
					NBTTagCompound data = new NBTTagCompound();
					data.setInteger("Slot", i);
					this.getStackInSlot(i).writeToNBT(data);
					list.appendTag(data);
				}
			}
			nbt.setTag("Items", list);
		}
		
		
	}
	
	
	
	
}
