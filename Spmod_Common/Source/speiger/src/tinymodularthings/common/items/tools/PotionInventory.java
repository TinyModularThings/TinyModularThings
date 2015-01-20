package speiger.src.tinymodularthings.common.items.tools;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.AdvInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;

public class PotionInventory extends AdvInventory
{
	EntityPlayer player;
	String id;
	
	public PotionInventory(EntityPlayer par1, ItemStack par2)
	{
		super(45);
		player = par1;
		id = NBTHelper.getTag(par2, "Bag").getString("ID");
		readFromNBT(NBTHelper.getTag(par2, "Bag").getCompoundTag("Inventory"));
	}

	@Override
	public void onInventoryChanged()
	{
		ItemStack bag = this.getBagFromInventory();
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		NBTHelper.getTag(bag, "Bag").setCompoundTag("Inventory", nbt);
	}
	
	
	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		par1.setOffset(12, 56);
		int slot = 0;
		for(int y = 0;y<4;y++)
		{
			for(int x = 0;x<9;x++)
			{
				par1.addSpmodSlot(this, slot++, 20 + x * 18, 18 + y * 18).addUsage("Potion Storage", "And Glass Bottle Storage");
			}
		}
		for(int i = 0;i<9;i++)
		{
			par1.addSpmodSlot(this, slot++, 20 + i * 18, 100).addUsage("Potion Filter", "Put a Potion in", "Then it search in the Potion storage", "for the matching ones (only Potion ID counts)");
		}
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getTexture()
	{
		return getEngine().getTexture("BigFrame");
	}

	@Override
	public void onPlayerCloseContainer(EntityPlayer par1)
	{
		super.onPlayerCloseContainer(par1);
		this.closeChest();
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
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		if(par1 != null)
		{
			boolean flag = false;
			if(slotID < 36)
			{
				flag = par1.itemID == Item.glassBottle.itemID;
			}
			return flag || par1.getItem() instanceof ItemPotion;
		}
		return super.canMergeItem(par1, slotID);
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

	public void onTick(ItemStack stack)
	{
		if(!player.worldObj.isRemote)
		{
			if(player.openContainer != null && player.openContainer instanceof AdvContainer && ((AdvContainer)player.openContainer).getInvName().equals("Potion Bag"))
			{
				return;
			}
			
			NBTTagCompound nbt = NBTHelper.getTag(this.getBagFromInventory(), "Bag").getCompoundTag("BagData");

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
									NBTHelper.getTag(this.getBagFromInventory(), "Bag").setCompoundTag("BagData", nbt);
									return;
								}
							}
						}
					}
				}
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
				return player.getHealth() <= player.getMaxHealth() - (4<<par1.getAmplifier());
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
	
	public boolean hasPotions()
	{
		boolean flag = false;
		for(int i = 0;i<9;i++)
		{
			ItemStack stack = this.getPotionStackFromFilter(i);
			if(stack != null)
			{
				flag = true;
			}
		}
		
		if(!flag)
		{
			return false;
		}
		
		for(int i = 0;i<36;i++)
		{
			ItemStack stack = this.getStackInSlot(i);
			if(stack != null && stack.getItem() instanceof ItemPotion)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void onGuiConstructed(GuiInventoryCore par1)
	{
		par1.setY(225);
		par1.setX(202);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getNameYOffset()
	{
		return super.getNameYOffset();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getInvNameYOffset()
	{
		return -6;
	}

	@Override
	public String getInvName()
	{
		return "Potion Bag";
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
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
