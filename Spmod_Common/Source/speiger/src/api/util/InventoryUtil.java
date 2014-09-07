package speiger.src.api.util;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class InventoryUtil
{
	
	// My Version of a ItemStack Check. Not the best but it is working
	public static boolean isItemEqual(ItemStack par1, ItemStack par2)
	{
		if (par1.itemID == par2.itemID)
		{
			if ((par1.getItemDamage() == par2.getItemDamage()) || (par1.getItemDamage() == OreDictionary.WILDCARD_VALUE || par2.getItemDamage() == OreDictionary.WILDCARD_VALUE) || (par1.getItemDamage() == -1 || par2.getItemDamage() == -1))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isItemEqualSave(ItemStack par1, ItemStack par2)
	{
		if(par1 != null && par2 != null)
		{
			if(par1.itemID == par2.itemID)
			{
				if(par1.getItemDamage() == par2.getItemDamage() && par1.stackSize <= par2.stackSize)
				{
					return true;
				}
			}
		}
		else if(par1 == null && par2 == null)
		{
			return true;
		}
		return false;
	}
	
	public static ItemStack getItemFromInventory(IInventory inv, int itemID)
	{
		ItemStack stack = null;
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack cu = inv.getStackInSlot(i);
			if (cu != null && cu.itemID == itemID)
			{
				stack = cu;
				break;
			}
		}
		return stack;
	}
	
	public static int getPipeRotation(IBlockAccess par0, int x, int y, int z)
	{
		return getPipeRotationFromMeta(getMetadata(par0, x, y, z));
	}
	
	public static int getFirstSlot(IInventory par1, int side)
	{
		if(par1 != null)
		{
			if(par1 instanceof ISidedInventory)
			{
				ISidedInventory inv = (ISidedInventory) par1;
				int[] slots = inv.getAccessibleSlotsFromSide(side);
				for(int i = 0;i<slots.length;i++)
				{
					int slot = slots[i];
					if(par1.getStackInSlot(slot) != null)
					{
						return slot;
					}
				}
			}
			else
			{
				for(int i = 0;i<par1.getSizeInventory();i++)
				{
					if(par1.getStackInSlot(i) != null)
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
	
	public static ItemStack splitStack(ItemStack par1, int size)
	{
		if (par1 == null)
		{
			return par1;
		}
		
		if (par1.stackSize < size)
		{
			return par1.splitStack(par1.stackSize);
		}
		return par1.splitStack(size);
	}
	
	public static int getPipeRotationFromMeta(int meta)
	{
		int end = meta;
		
		if (end > 5)
		{
			end -= 6;
		}
		return end;
		
	}
	
	public static int getMetadata(IBlockAccess par0, int x, int y, int z)
	{
		return par0.getBlockMetadata(x, y, z);
	}
	
	public static ItemStack insertItemIntoBasicInventory(IInventory inv, ItemStack stack)
	{
		ItemStack copy = stack.copy();
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack cu = inv.getStackInSlot(i);
			if (cu == null)
			{
				inv.setInventorySlotContents(i, copy);
				copy = null;
				break;
			}
			else if (cu != null && cu.stackSize < 64 && cu.isItemEqual(copy) && ItemStack.areItemStackTagsEqual(copy, cu))
			{
				int stackSize = cu.stackSize + copy.stackSize;
				if (stackSize > 64)
				{
					int leftover = stackSize - 64;
					copy.stackSize = leftover;
					cu.stackSize = 64;
					inv.setInventorySlotContents(i, cu);
					continue;
				}
				else
				{
					cu.stackSize = stackSize;
					copy = null;
					inv.setInventorySlotContents(i, cu);
					break;
				}
				
			}
		}
		
		return copy;
	}
	
	public static ItemStack addToInventory(IInventory inv, int slot, ItemStack stack)
	{
		ItemStack copy = stack.copy();
		
		ItemStack intInv = inv.getStackInSlot(slot);
		
		if (intInv == null)
		{
			intInv = stack;
			return stack;
		}
		else
		{
			if (intInv.stackSize < intInv.getMaxStackSize() && intInv.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, intInv))
			{
				int size = Math.min(intInv.stackSize + copy.stackSize, intInv.getMaxStackSize());
				
				int end = intInv.getMaxStackSize() - size;
				
				intInv.stackSize = size;
				copy.stackSize = end;
				inv.setInventorySlotContents(slot, intInv);
				return copy;
			}
			copy.stackSize = 0;
			return copy;
		}
		
	}
	
	public static void dropInventory(World world, int x, int y, int z)
	{
		Random rand = new Random();
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if (!(tileEntity instanceof IInventory))
		{
			return;
		}
		
		IInventory inventory = (IInventory) tileEntity;
		
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			
			ItemStack itemStack = inventory.getStackInSlot(i);
			
			if (itemStack != null && itemStack.stackSize > 0)
			{
				float dX = rand.nextFloat() * 0.8F + 0.1F;
				float dY = rand.nextFloat() * 0.8F + 0.1F;
				float dZ = rand.nextFloat() * 0.8F + 0.1F;
				
				EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.itemID, itemStack.stackSize, itemStack.getItemDamage()));
				
				if (itemStack.hasTagCompound())
				{
					entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
				}
				
				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				itemStack.stackSize = 0;
			}
		}
	}
	
	public static void dropInventory(World world, int x, int y, int z, IInventory inv)
	{
		for (int i = 0; i < inv.getSizeInventory(); ++i)
		{
			ItemStack itemstack = inv.getStackInSlot(i);
			
			if (itemstack != null)
			{
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
				
				while (itemstack.stackSize > 0)
				{
					int j = world.rand.nextInt(21) + 10;
					
					if (j > itemstack.stackSize)
					{
						j = itemstack.stackSize;
					}
					
					itemstack.stackSize -= j;
					ItemStack stack = itemstack.copy();
					stack.stackSize = j;
					
					EntityItem entityitem = new EntityItem(world, x + (double) f, y + (double) f1, z + (double) f2, stack);
					float f3 = 0.05F;
					entityitem.motionX = (float) world.rand.nextGaussian() * f3;
					entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
	}
	
	public static void dropInventory(World world, int x, int y, int z, ArrayList<ItemStack> inv)
	{
		for (int i = 0; i < inv.size(); i++)
		{
			ItemStack itemstack = inv.get(i);
			if (itemstack != null)
			{
				float f = world.rand.nextFloat() * 0.8F + 0.1F;
				float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
				float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
				
				while (itemstack.stackSize > 0)
				{
					int j = world.rand.nextInt(21) + 10;
					
					if (j > itemstack.stackSize)
					{
						j = itemstack.stackSize;
					}
					
					itemstack.stackSize -= j;
					ItemStack stack = new ItemStack(itemstack.itemID, j, itemstack.getItemDamage());
					if (itemstack.hasTagCompound())
					{
						stack.setTagCompound(stack.getTagCompound());
					}
					
					EntityItem entityitem = new EntityItem(world, x + (double) f, y + (double) f1, z + (double) f2, stack);
					float f3 = 0.05F;
					entityitem.motionX = (float) world.rand.nextGaussian() * f3;
					entityitem.motionY = (float) world.rand.nextGaussian() * f3 + 0.2F;
					entityitem.motionZ = (float) world.rand.nextGaussian() * f3;
					world.spawnEntityInWorld(entityitem);
				}
			}
			inv.remove(i);
		}
	}

	public static void dropItem(EntityPlayer player, ItemStack item)
	{
		EntityItem drop = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, item);
		player.capturedDrops.add(drop);
		player.worldObj.spawnEntityInWorld(drop);
	}
	
	public static void dropItem(TileEntity tile, ItemStack item)
	{
		EntityItem drop = new EntityItem(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord, item);
		tile.worldObj.spawnEntityInWorld(drop);
	}

	public static boolean isInventoryFull(IInventory inv)
	{
		for(int i = 0;i<inv.getSizeInventory();i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null)
			{
				return false;
			}
			if(stack.stackSize < stack.getMaxStackSize())
			{
				return false;
			}
		}
		return true;
	}
	
}
