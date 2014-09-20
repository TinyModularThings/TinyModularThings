package speiger.src.tinymodularthings.common.items.tools;

import java.util.List;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import speiger.src.api.nbt.NBTHelper;
import speiger.src.spmodapi.common.util.slot.BasicItemInventory;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class PotionInventory extends BasicItemInventory
{
	EntityPlayer player;
	String id;
	
	public PotionInventory(InventoryPlayer par1, ItemStack par2)
	{
		super(45);
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
			if(slot >= 0 && slot < this.getSizeInventory())
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
		this.onInventoryChanged();
	}
	
	public ItemStack getBagFromInventory()
	{
		InventoryPlayer inv = player.inventory;
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack cu = inv.getStackInSlot(i);
			if(cu != null && cu.itemID == TinyItems.potionBag.itemID)
			{
				String id = NBTHelper.getTag(cu, "Bag").getString("ID");
				if(id.equalsIgnoreCase(this.id))
				{
					return cu;
				}
			}
		}
		if(inv.getCurrentItem() != null && inv.getCurrentItem().itemID == TinyItems.potionBag.itemID)
		{
			String may = NBTHelper.getTag(inv.getCurrentItem(), "Bag").getString("ID");
			if(may.equalsIgnoreCase(id))
			{
				return inv.getCurrentItem();
			}
		}
		return null;
	}

	public void writeToNBT()
	{
		ItemStack stack = getBagFromInventory();
		
		if(stack != null)
		{
			NBTTagCompound bag = NBTHelper.getTag(stack, "Bag");
			NBTTagCompound nbt = bag.getCompoundTag("Inventory");
			NBTTagList list = new NBTTagList();
			for(int i = 0;i<this.getSizeInventory();i++)
			{
				ItemStack item = this.getStackInSlot(i);
				if(item != null)
				{
					NBTTagCompound data = new NBTTagCompound();
					data.setInteger("Slot", i);
					this.getStackInSlot(i).writeToNBT(data);
					list.appendTag(data);
				}
			}
			nbt.setTag("Items", list);
			bag.setCompoundTag("Inventory", nbt);
			stack.setTagInfo("Bag", bag);
		}
	}

	public void onTick(ItemStack stack)
	{
		if(!player.worldObj.isRemote)
		{
			if(player.openContainer != null && player.openContainer instanceof ContainerPotionBag)
			{
				return;
			}
			
			NBTTagCompound nbt = NBTHelper.getTag(this.getBagFromInventory(), "Bag").getCompoundTag("BagData");
			int delay = nbt.getInteger("Delay");
			if(delay > 0)
			{
				nbt.setInteger("Delay", delay-1);
			}
			else
			{
				for(int x = 0;x<9;x++)
				{
					ItemPotion potion = getPotionFromFilter(x);
					if(potion != null)
					{
						List<PotionEffect> effects = potion.getEffects(getPotionStackFromFilter(x));
						if(effects != null)
						{
							for(PotionEffect ef : effects)
							{
								PotionEffect effect = player.getActivePotionEffect(Potion.potionTypes[ef.getPotionID()]);
								if(effect == null || effect.getDuration() < 200)
								{
									if(applyEffect(ef))
									{
										nbt.setInteger("Delay", 20);
										NBTHelper.getTag(this.getBagFromInventory(), "Bag").setCompoundTag("BagData", nbt);
										return;
									}
								}
							}
						}
					}
				}
				nbt.setInteger("Delay", 40);
			}
			NBTHelper.getTag(this.getBagFromInventory(), "Bag").setCompoundTag("BagData", nbt);
		}
	}
	
	public boolean applyEffect(PotionEffect par1)
	{
		for(int i = 0;i<36;i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null && stack.itemID != Item.glassBottle.itemID)
			{
				ItemPotion potion = (ItemPotion)stack.getItem();
				if(!potion.isSplash(stack.getItemDamage()))
				{
					List<PotionEffect> effect = potion.getEffects(stack);
					if(effect != null && effect.size() > 0)
					{
						for(PotionEffect ef : effect)
						{
							if(potionEquals(ef, par1) && canBeUsed(ef))
							{
								this.setInventorySlotContents(i, stack.onFoodEaten(player.worldObj, player));
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean canBeUsed(PotionEffect par1)
	{
		Potion potion = Potion.potionTypes[par1.getPotionID()];
		if(potion != null)
		{
			if(potion.isBadEffect())
			{
				return false;
			}
			else if(potion == potion.heal)
			{
				return player.getHealth() < player.getMaxHealth() - (4<<par1.getAmplifier());
			}
			return true;
		}
		return false;
	}
	
	public boolean potionEquals(PotionEffect par1, PotionEffect par2)
	{
		if(par1 == null && par2 == null)
		{
			return true;
		}
		if(par1 == null || par2 == null)
		{
			return false;
		}
		
		if(par1.getPotionID() == par2.getPotionID())
		{
			return true;
		}
		return false;
	}
	
	public ItemStack getPotionStackFromFilter(int slot)
	{
		return this.getStackInSlot(36+slot);
	}
	
	public ItemPotion getPotionFromFilter(int slot)
	{
		ItemStack stack = this.getStackInSlot(36+slot);
		if(stack != null)
		{
			return (ItemPotion)stack.getItem();
		}
		return null;
	}
	
	
}
