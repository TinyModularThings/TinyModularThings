package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.api.IDeepStorageUnit;
import speiger.src.api.common.data.nbt.NBTHelper;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TinyBarrel extends TileFacing implements IDeepStorageUnit,
		ISidedInventory
{
	
	public ItemStack templateItem;
	
	public int storedAmount;
	public int maxStorage = 0;
	public int metadata;
	
	public TinyBarrel()
	{
		maxStorage = 16;
	}
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}
	
	@Override
	public int getSizeInventory()
	{
		return 1;
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		if(par3 == null || !NBTHelper.nbtCheck(par3, "BarrelMeta"))
		{
			return;
		}
		int meta = par3.getTagCompound().getCompoundTag("BarrelMeta").getInteger("Metadata");
		par2.add("Stores: "+((1 + meta) * 16)+" Stacks");
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(side == getFacing())
		{
			return getEngine().getTexture(TinyBlocks.storageBlock, 4, 0);
		}
		if(side == 0 || side == 1)
		{
			return getEngine().getTexture(TinyBlocks.storageBlock, 4, 1);
		}
		return getEngine().getTexture(TinyBlocks.storageBlock, 4, 2);
	}
	
	@Override
	public ItemStack getStoredItemType()
	{
		return templateItem;
	}
	
	@Override
	public void setStoredItemCount(int amount)
	{
		storedAmount = amount;
		if(amount <= 0 && hasItem())
		{
			setItem(null);
		}
	}
	
	public void setMax(int i)
	{
		metadata = i;
		maxStorage = ((1 + i) * 16);
	}
	
	@Override
	public float getBlockHardness()
	{
		return 2F;
	}
	
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 4F;
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if(!worldObj.isRemote)
		{
			if(this.worldObj.getTotalWorldTime() - this.getClockTime() < 10L && hasItem())
			{
				for(Integer slot : allowedSlots(par1))
				{
					ItemStack slotStack = par1.inventory.getStackInSlot(slot).copy();;
					int added = addItem(slotStack);
					slotStack.stackSize -= added;
					if(slotStack.stackSize > 0)
					{
						par1.inventory.setInventorySlotContents(slot, slotStack);
					}
					else
					{
						par1.inventory.setInventorySlotContents(slot, null);
					}
				}
			}
			else
			{
				ItemStack heldStack = par1.inventory.getCurrentItem();
				if(heldStack != null)
				{
					int added = addItem(heldStack);
					heldStack.stackSize -= added;
				}
			}
			setClockTime((int)worldObj.getTotalWorldTime());
			par1.inventoryContainer.detectAndSendChanges();
			onInventoryChanged();
		}
		return true;
	}
	
	public List<Integer> allowedSlots(EntityPlayer par1)
	{
		ArrayList<Integer> ints = new ArrayList<Integer>();
		InventoryPlayer player = par1.inventory;
		for(int i = 0;i<player.getSizeInventory();i++)
		{
			ItemStack stack = player.getStackInSlot(i);
			if(stack != null && sameItem(stack))
			{
				ints.add(i);
			}
		}
		return ints;
	}
	
	@Override
	public void onLeftClick(EntityPlayer par1)
	{
		if(!worldObj.isRemote)
		{
			if(par1.getCurrentEquippedItem() != null && par1.getCurrentEquippedItem().getItem() instanceof ItemAxe)
			{
				return;
			}
			
			boolean sneak = par1.isSneaking();
			
			if(sneak)
			{
				ItemStack item = getItem();
				
				if(item != null)
				{
					item.stackSize = 1;
					if(par1.inventory.addItemStackToInventory(item))
					{
						decrStackSize(1, 1);
					}
				}
			}
			else
			{
				ItemStack original = getStackInSlot(1);
				if(original != null)
				{
					ItemStack copy = original.copy();
					par1.inventory.addItemStackToInventory(copy);
					original.stackSize -= copy.stackSize;
					decrStackSize(1, original.stackSize);
				}
			}
			onInventoryChanged();
			par1.inventoryContainer.detectAndSendChanges();
		}
	}
	
	public int addItem(ItemStack par1)
	{
		boolean skip = (par1 == null) || (!sameItem(par1));
		if(!hasItem() && par1 != null)
		{
			skip = false;
		}
		if(skip)
		{
			return 0;
		}
		int deposite;
		if(!hasItem())
		{
			setItem(par1);
			this.storedAmount = par1.stackSize;
			deposite = par1.stackSize;
		}
		else
		{
			int totalCapacity = getItem().getMaxStackSize() * this.maxStorage;
			int freeSpace = totalCapacity - this.storedAmount;
			deposite = Math.min(par1.stackSize, freeSpace);
			this.storedAmount += deposite;
		}
		return deposite;
	}
	
	public boolean sameItem(ItemStack par1)
	{
		if(!hasItem())
		{
			return true;
		}
		if(par1 == null)
		{
			return false;
		}
		if((getItem().isItemEqual(par1)) && (ItemStack.areItemStackTagsEqual(getItem(), par1)))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public void setStoredItemType(ItemStack type, int amount)
	{
		setItem(type);
		storedAmount = amount;
	}
	
	@Override
	public int getMaxStoredCount()
	{
		if(hasItem())
		{
			return maxStorage * getItem().getMaxStackSize();
		}
		else
		{
			return maxStorage * 64;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
		super.onRenderInv(stack, render);
	}
	
	@SideOnly(Side.CLIENT)
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		super.onRenderWorld(block, renderer);
	}
	
	public String getBarrelString()
	{
		String outString = "";
		int maxStack = getItem().getMaxStackSize();
		int amount = storedAmount;
		if(maxStack != 1)
		{
			int nstacks = amount / maxStack;
			int remains = amount % maxStack;
			if((nstacks > 0) && (remains > 0))
			{
				outString = String.format("%s*%s + %s", new Object[] {Integer.valueOf(nstacks), Integer.valueOf(maxStack), Integer.valueOf(remains) });
			}
			
			else if((nstacks == 0) && (remains > 0))
			{
				outString = String.format("%s", new Object[] {Integer.valueOf(remains) });
			}
			else if((nstacks > 0) && (remains == 0))
			{
				outString = String.format("%s*%s", new Object[] {Integer.valueOf(nstacks), Integer.valueOf(maxStack) });
			}
			else if(amount == 0)
			{
				outString = "0";
			}
		}
		else if(maxStack == 1)
		{
			outString = String.format("%s", new Object[] {Integer.valueOf(amount) });
		}
		return outString;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return getStackFromSlot(i);
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if(hasItem())
		{
			ItemStack copy = getItem().copy();
			int amount = Math.min(j, Math.min(templateItem.getMaxStackSize(), storedAmount));
			this.storedAmount -= amount;
			copy.stackSize = amount;
			return copy;
		}
		return null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return getStackFromSlot(i);
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if(i == 0)
		{
			if(itemstack == null)
			{
				return;
			}
			if(!hasItem())
			{
				setItem(itemstack);
			}
			if((maxStorage * getItem().getMaxStackSize()) - storedAmount < itemstack.stackSize)
			{
				storedAmount = (maxStorage * getItem().getMaxStackSize());
			}
			else
			{
				storedAmount += itemstack.stackSize;
			}
		}
		else if(i == 1)
		{
			if(itemstack == null)
			{
				storedAmount -= templateItem.getMaxStackSize();
			}
			else
			{
				storedAmount -= (templateItem.getMaxStackSize() - itemstack.stackSize);
			}
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
		return 64;
	}
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[] {0, 1 };
	}
	
	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		if(i == 1)
		{
			return false;
		}
		if(itemstack == null)
		{
			return false;
		}
		if(!hasItem())
		{
			return true;
		}
		if(!equal(itemstack))
		{
			return false;
		}
		if(!enoughSpace())
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		if(i == 0)
		{
			return false;
		}
		if(!hasItem())
		{
			return false;
		}
		if(itemstack == null)
		{
			return true;
		}
		if(!equal(itemstack))
		{
			return false;
		}
		return true;
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
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		if(storedAmount <= 0 && templateItem != null)
		{
			templateItem = null;
			storedAmount = 0;
		}
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	private ItemStack getStackFromSlot(int slotID)
	{
		if(slotID == 0)
		{
			if(hasItem())
			{
				int amount = (maxStorage * getItem().getMaxStackSize()) - storedAmount;
				if(amount < 64)
				{
					ItemStack result = getItem().copy();
					result.stackSize = amount;
					return result;
				}
			}
		}
		else if(slotID == 1)
		{
			if(hasItem())
			{
				ItemStack result = getItem().copy();
				result.stackSize = Math.min(result.getMaxStackSize(), storedAmount);
				return result;
			}
		}
		return null;
	}
	
	public boolean hasItem()
	{
		return templateItem != null;
	}
	
	public ItemStack getItem()
	{
		return templateItem;
	}
	
	private boolean enoughSpace()
	{
		if(!hasItem())
		{
			return true;
		}
		return (maxStorage * getItem().getMaxStackSize()) - storedAmount > 0;
	}
	
	private boolean equal(ItemStack par1)
	{
		return par1.isItemEqual(getItem()) && ItemStack.areItemStackTagsEqual(par1, getItem());
	}
	
	public void setItem(ItemStack par1)
	{
		if(par1 != null)
		{
			templateItem = par1.copy();
			templateItem.stackSize = 0;
		}
		else
		{
			templateItem = null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		maxStorage = nbt.getInteger("Max");
		storedAmount = nbt.getInteger("Stored");
		if(nbt.hasKey("ItemData"))
		{
			templateItem = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("ItemData"));
		}
		else
		{
			templateItem = null;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Max", maxStorage);
		nbt.setInteger("Stored", storedAmount);
		NBTTagCompound item = new NBTTagCompound();
		if(templateItem != null)
		{
			templateItem.writeToNBT(item);
		}
		nbt.setCompoundTag("ItemData", item);
	}
	
	@SideOnly(Side.CLIENT)
	public void applySize(float size, double posX, double posY, double posz)
	{
		GL11.glTranslated(0.0D, 0.0D, posz);
		GL11.glScalef(0.0039063F, 0.0039063F, -1.0E-004F);
		GL11.glTranslated(posX, posY, 0.0D);
		GL11.glScalef(size, size, 1.0F);
	}
	
	@SideOnly(Side.CLIENT)
	public void applyRotation(float x, float y, float z)
	{
		GL11.glTranslatef((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);
		switch(getFacing())
		{
			case 2:
				GL11.glRotatef(0, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180F, 0F, 0F, 1F);
				break;
			case 3:
				GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180F, 0F, 0F, 1F);
				break;
			case 4:
				GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180, 0.0F, 0F, 1.0F);
				break;
			case 5:
				GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
				GL11.glRotatef(180, 0.0F, 0F, 1.0F);
				break;
		}
		
		GL11.glTranslated(-0.5D, -0.5D, -0.5D);
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return null;
	}

	@Override
	public ArrayList<ItemStack> getItemDrops(int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		NBTTagCompound data = new NBTTagCompound();
		data.setInteger("Metadata", metadata);
		ItemStack stack = new ItemStack(TinyBlocks.storageBlock, 1, 4);
		stack.setTagInfo("BarrelMeta", data);
		drop.add(stack);
		if(hasItem())
		{
			for(;storedAmount > 0;)
			{
				ItemStack toAdd = decrStackSize(1, 64);
				drop.add(toAdd);
			}
		}
		return drop;
	}
	
}
