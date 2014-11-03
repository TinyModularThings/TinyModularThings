package speiger.src.spmodapi.common.blocks.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

import java.io.DataInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.energy.IBCBattery;
import speiger.src.api.nbt.INBTReciver;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.api.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.packets.SpmodPacketHelper.PacketType;
import speiger.src.api.util.MathUtils;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.utils.GuiInventoryAccesser;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.enums.EnumGuiIDs;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class InventoryAccesser extends TileFacing implements ISidedInventory, INBTReciver, IPacketReciver
{
	public static ArrayList<String> allowedClasses = new ArrayList<String>();
	public HashMap<List<Integer>, String> customNames = new HashMap<List<Integer>, String>();
	public ArrayList<List<Integer>> canBeOpened = new ArrayList<List<Integer>>();
	public ItemStack[] redstoneCables = new ItemStack[5];
	public double power = 0;
	
	
	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	
	
	@Override
	public void registerIcon(TextureEngine par1)
	{
		super.registerIcon(par1);
		par1.registerTexture(new BlockStack(APIBlocks.blockUtils, 4), "InventoryAccesser_Top", "InventoryAccesser_Side", "InventoryAccesser_Front");
	}

	


	@Override
	public float getBlockHardness()
	{
		return 4F;
	}
	@Override
	public float getExplosionResistance(Entity par1)
	{
		return 5F;
	}

	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		BlockStack block = new BlockStack(APIBlocks.blockUtils, 4);
		if(side == getFacing())
		{
			return engine.getTexture(block, 2);
		}
		else if(side == 0 || side == 1)
		{
			return engine.getTexture(block, 0);
		}
		else
		{
			return engine.getTexture(block, 1);
		}
	}
	
	public String getCustomName(List<Integer> par1)
	{
		return customNames.get(par1);
	}
	
	public boolean hasCustomName(List<Integer> par1)
	{
		
		return customNames.containsKey(par1);
	}
	
	public BlockPosition getTarget(int i)
	{
		return new BlockPosition(canBeOpened.get(i));
	}
	
	public boolean isInRange(int i)
	{
		return this.canBeOpened.size() > i;
	}
	
	public boolean needPower()
	{
		return power <= 0;
	}
	
	@Override
	public boolean onActivated(EntityPlayer par1, int side)
	{
		
		if(!worldObj.isRemote)
		{
			if(getUserSize() == 0)
			{
				reloadData();
			}
			
			par1.openGui(SpmodAPI.instance, EnumGuiIDs.Tiles.getID(), getWorldObj(), xCoord, yCoord, zCoord);
		}
		return true;
	}
	
	

	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drop = super.onDrop(fortune);
		for(ItemStack stack : redstoneCables)
		{
			drop.add(stack);
		}
		return drop;
	}

	@Override
	public void onTick()
	{
		if(!worldObj.isRemote)
		{
			if(power < 2000)
			{
				getPower();
			}
			if(worldObj.getWorldTime() % 20 == 0)
			{
				if(hasUsers() && power > 0)
				{
					power-=1;
				}
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
			}
		}
	}
	
	public void getPower()
	{
		ItemStack stack = getStackInSlot(4);
		if(stack != null)
		{
			if(stack.getItem() instanceof IBCBattery)
			{
				power += (2.5*((IBCBattery)stack.getItem()).discharge(stack, 1, false, false));
			}
			else if(stack.getItem() instanceof IElectricItem)
			{
				power +=ElectricItem.manager.discharge(stack, 1, ((IElectricItem)stack.getItem()).getTier(stack), false, false);
			}
		}
	}

	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new ContainerInventoryAccesser(par1, this);
	}

	@Override
	public boolean hasContainer()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiInventoryAccesser(par1, this);
	}
	



	public void reloadData()
	{
		canBeOpened.clear();
		int size = 0;
		for(int i = 0;i<4;i++)
		{
			ItemStack stack = redstoneCables[i];
			if(stack != null)
			{
				size += stack.stackSize;
			}
		}
		LinkedList<List<Integer>> possebilities = new LinkedList<List<Integer>>();
		LinkedList<List<Integer>> added = new LinkedList<List<Integer>>();
		BlockPosition pos = this.getPosition();
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{		
			BlockPosition newPos = pos.add(dir);
			if(newPos.doesBlockExsist())
			{
				boolean par1 = false;
				if(newPos.isThisBlock(new BlockStack(Block.workbench), false))
				{
					par1 = true;
				}
				else if(newPos.isThisBlock(new BlockStack(Block.anvil), false))
				{
					par1 = true;
				}
				else if(newPos.hasTileEntity())
				{
					TileEntity tile = newPos.getTileEntity();
					if(allowedClasses.contains(tile.getClass().getSimpleName()))
					{
						par1 = true;
					}
				}
				
				if(par1)
				{
					possebilities.add(newPos.getAsList());
					added.add(newPos.getAsList());
				}
			}
		}
		for(;!possebilities.isEmpty();)
		{
			BlockPosition lastTarget = new BlockPosition(possebilities.removeFirst());
			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
			{
				BlockPosition newPos = lastTarget.add(dir);
				if(newPos.isThisPosition(pos))
				{
					continue;
				}
				if(added.contains(newPos.getAsList()))
				{
					continue;
				}
				
				if(newPos.doesBlockExsist())
				{
					boolean par1 = false;
					if(newPos.isThisBlock(new BlockStack(Block.workbench), false))
					{
						par1 = true;
					}
					else if(newPos.isThisBlock(new BlockStack(Block.anvil), false))
					{
						par1 = true;
					}
					else if(newPos.hasTileEntity())
					{
						TileEntity tile = newPos.getTileEntity();
						if(allowedClasses.contains(tile.getClass().getSimpleName()))
						{
							par1 = true;
						}
					}
					
					if(par1)
					{
						possebilities.add(newPos.getAsList());
						added.add(newPos.getAsList());
					}
				}
			}
		}
		if(added.size() > size)
		{
			for(;added.size() > size;)
			{
				added.removeLast();
			}
		}
		canBeOpened.addAll(added);
		
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	

	@Override
	public void onInventoryChanged()
	{
		super.onInventoryChanged();
		if(!worldObj.isRemote)
		{
			reloadData();
		}
	}
	


	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		NBTTagList items = nbt.getTagList("Items");
		for(int i = 0;i<items.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)items.tagAt(i);
			int slot = data.getInteger("Slot");
			if(slot >= 0 && slot < redstoneCables.length)
			{
				redstoneCables[slot] = ItemStack.loadItemStackFromNBT(data);
			}
		}
		NBTTagList names = nbt.getTagList("CustomNames");
		for(int i = 0;i<names.tagCount();i++)
		{
			NBTTagCompound data = (NBTTagCompound)names.tagAt(i);
			String key = data.getString("Value");
			int[] value = data.getIntArray("Key");
			customNames.put(MathUtils.getListFromArray(value), key);
		}
		
		NBTTagList can = nbt.getTagList("Can");
		canBeOpened.clear();
		for(int i = 0;i<can.tagCount();i++)
		{
			NBTTagIntArray data2 = (NBTTagIntArray)can.tagAt(i);
			int[] array = data2.intArray;
			canBeOpened.add(MathUtils.getListFromArray(array));
		}
		
		power = nbt.getDouble("Power");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		for(int i = 0;i<redstoneCables.length;i++)
		{
			ItemStack stack = redstoneCables[i];
			if(stack != null)
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setInteger("Slot", i);
				stack.writeToNBT(data);
				list.appendTag(data);
			}
		}
		
		nbt.setTag("Items", list);
		NBTTagList names = new NBTTagList();
		Iterator<Entry<List<Integer>, String>> iter = customNames.entrySet().iterator();
		for(;iter.hasNext();)
		{
			Entry<List<Integer>, String> entry = iter.next();
			NBTTagCompound data = new NBTTagCompound();
			data.setIntArray("Key", MathUtils.getArrayFromList(entry.getKey()));
			data.setString("Value", entry.getValue());
			names.appendTag(data);
		}
		nbt.setTag("CustomNames", names);
		
		NBTTagList can = new NBTTagList();
		for(List<Integer> array : canBeOpened)
		{
			NBTTagIntArray data = new NBTTagIntArray("data", new int[]{array.get(0), array.get(1), array.get(2), array.get(3)});
			can.appendTag(data);
		}
		nbt.setTag("Can", can);
		
		nbt.setDouble("Power", power);
	}

	
	
	public ItemStack getStackInSlot(int par1)
	{
		return this.redstoneCables[par1];
	}
	
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (this.redstoneCables[par1] != null)
		{
			ItemStack itemstack;
			
			if (this.redstoneCables[par1].stackSize <= par2)
			{
				itemstack = this.redstoneCables[par1];
				this.redstoneCables[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.redstoneCables[par1].splitStack(par2);
				
				if (this.redstoneCables[par1].stackSize == 0)
				{
					this.redstoneCables[par1] = null;
				}
				
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}
	
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (this.redstoneCables[par1] != null)
		{
			ItemStack itemstack = this.redstoneCables[par1];
			this.redstoneCables[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}
	
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		this.redstoneCables[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return "Inventory Accesser";
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
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
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
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j)
	{
		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return 5;
	}

	public static void addTileEntity(String cu)
	{
		if(!allowedClasses.contains(cu))
		{
			allowedClasses.add(cu);
		}
	}

	public static void removeTileEntity(String tile)
	{
		if(allowedClasses.contains(tile))
		{
			allowedClasses.remove(tile);
		}
	}

	@Override
	public void recivePacket(DataInput par1)
	{
		if(!worldObj.isRemote)
		{
			try
			{
				int task = par1.readInt();
				if(task == 0)
				{
					int number = par1.readInt();
					String name = par1.readUTF();
					if(this.isInRange(number))
					{
						BlockPosition pos = this.getTarget(number);
						if(pos != null)
						{
							this.customNames.put(pos.getAsList(), name);
							PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, this.getDescriptionPacket());
						}
					}
				}
				else if(task == 1)
				{
					int number = par1.readInt();
					String name = par1.readUTF();
					int side = this.getSideFromPlayer(name);
					if(side != -1 && this.isInRange(number))
					{
						BlockPosition pos = this.getTarget(number);
						if(pos != null)
						{
							pos.getAsBlockStack().getBlock().onBlockActivated(pos.getWorld(), pos.getXCoord(), pos.getYCoord(), pos.getZCoord(), worldObj.getPlayerEntityByName(name), side, 0.5F, 0.5F, 0.5F);
						}
					}
				}
				else if(task == 2)
				{
					allowedClasses.add(par1.readUTF());
				}
				else if(task == 3)
				{
					allowedClasses.remove(par1.readUTF());
				}


			}
			catch(Exception e)
			{
			}
		}
		
	}

	@Override
	public String identifier()
	{
		return "Accesser";
	}

	@Override
	public void loadFromNBT(NBTTagCompound par1)
	{
		NBTTagList list = par1.getTagList("Data");
		for(int i = 0;i<list.tagCount();i++)
		{
			NBTTagString string = (NBTTagString)list.tagAt(i);
			if(!allowedClasses.contains(string.data))
			{
				allowedClasses.add(string.data);
			}
		}
	}

	@Override
	public void saveToNBT(NBTTagCompound par1)
	{
		NBTTagList list = new NBTTagList();
		for(String data : allowedClasses)
		{
			list.appendTag(new NBTTagString("Data", data));
		}
	}

	@Override
	public void finishLoading()
	{
		
	}

	@Override
	public SpmodMod getOwner()
	{
		return SpmodAPI.instance;
	}

	@Override
	public String getID()
	{
		return "AccessTile";
	}
}
