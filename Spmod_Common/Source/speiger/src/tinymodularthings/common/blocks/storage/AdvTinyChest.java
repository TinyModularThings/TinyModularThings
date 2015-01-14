package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.interfaces.ISharedInventory;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.storage.GuiAdvTinyChest;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AdvTinyChest extends TileFacing implements IInventory,
		ISharedInventory
{
	
	public boolean isEmpty = false;
	public boolean isFull = false;
	public boolean update = true;
	public int mode = -1;
	ItemStack[] inventory = new ItemStack[0];
	
	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	
	public int getMode()
	{
		return mode;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onItemInformation(EntityPlayer par1, List par2, ItemStack par3)
	{
		super.onItemInformation(par1, par2, par3);
		if(GuiScreen.isCtrlKeyDown())
		{
			par2.add("A small chest that outputs a Redstone Signal");
			par2.add("Empty means redstone Signal down");
			par2.add("Full means redstone Signal Up");
			par2.add("Between mean none Signal");
		}
		else
		{
			par2.add("Press Ctrl to get Extra Infos");
		}
	}

	@Override
	public boolean dropNormalBlock()
	{
		return false;
	}

	public boolean isValidMode()
	{
		return mode != -1;
	}
	
	@Override
	public void onPlaced(int facing)
	{
		super.onPlaced(facing);
		if (isValidMode())
		{
			updateInventory();
		}
	}
	
	public void updateInventory()
	{
		inventory = new ItemStack[getSizeInventory()];
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	@Override
	public boolean onActivated(EntityPlayer par1)
	{
		if (isValidMode())
		{
			par1.openGui(TinyModularThings.instance, EnumIDs.ADVTiles.getId(), worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasContainer()
	{
		return true;
	}
	
	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new AdvTinyChestInventory(par1, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new GuiAdvTinyChest(par1, this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		mode = nbt.getInteger("Mode");
		isFull = nbt.getBoolean("isFull");
		isEmpty = nbt.getBoolean("isEmpty");
		NBTTagList nbttaglist = nbt.getTagList("Items");
		inventory = new ItemStack[mode < 0 ? 0 : mode];
		
		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");
			
			if (b0 >= 0 && b0 < inventory.length)
			{
				inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();
		
		for (int i = 0; i < inventory.length; ++i)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		
		nbt.setTag("Items", nbttaglist);
		nbt.setInteger("Mode", mode);
		nbt.setBoolean("isFull", isFull);
		nbt.setBoolean("isEmpty", isEmpty);
	}
	
	@Override
	public int getSizeInventory()
	{
		return mode;
	}
	
	@Override
	public ItemStack getStackInSlot(int par1)
	{
		return inventory[par1];
	}
	
	@Override
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (inventory[par1] != null)
		{
			ItemStack itemstack;
			
			if (inventory[par1].stackSize <= par2)
			{
				itemstack = inventory[par1];
				inventory[par1] = null;
				return itemstack;
			}
			else
			{
				itemstack = inventory[par1].splitStack(par2);
				
				if (inventory[par1].stackSize == 0)
				{
					inventory[par1] = null;
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
		if (inventory[par1] != null)
		{
			ItemStack itemstack = inventory[par1];
			inventory[par1] = null;
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
		inventory[par1] = par2ItemStack;
		
		if (par2ItemStack != null && par2ItemStack.stackSize > getInventoryStackLimit())
		{
			par2ItemStack.stackSize = getInventoryStackLimit();
		}
	}
	
	@Override
	public String getInvName()
	{
		return "AdvTinyChest";
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
		return true;
	}
	
	@Override
	public IInventory getIInventory()
	{
		return this;
	}
	
	@Override
	public boolean isEntity()
	{
		return false;
	}
	
	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		if (key == 0)
		{
			isFull = val == 1;
		}
		if (key == 1)
		{
			isEmpty = val == 1;
		}
		if (key == 2)
		{
			mode = val;
		}
		
	}
	
	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		par2.sendProgressBarUpdate(par1, 0, isFull ? 1 : 0);
		par2.sendProgressBarUpdate(par1, 1, isEmpty ? 1 : 0);
		par2.sendProgressBarUpdate(par1, 2, mode);
	}
	
	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if (sneak)
		{
			if (par1.getCurrentEquippedItem() == null)
			{
				setFacing(setNextFacing());
				par1.getFoodStats().addExhaustion(1.0F);
				updateBlock();
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		
		ItemStack tinyChest = new ItemStack(TinyItems.advTinyChest, 1, mode - 1);
		drops.add(tinyChest);
		for (int i = 0; i < getSizeInventory(); i++)
		{
			ItemStack cu = inventory[i];
			if (cu != null)
			{
				drops.add(cu);
			}
		}
		
		return drops;
		
	}
	
	@Override
	public void onTick()
	{
		super.onTick();
		if (!worldObj.isRemote)
		{
			if (update)
			{
				onInventoryChanged();
				updateBlock();
				PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, worldObj.provider.dimensionId, getDescriptionPacket());
				update = false;
			}
			
			if (worldObj.getWorldTime() % 20 == 0)
			{
				updateRedstone();
				this.updateNeighbors(true);
			}
		}
	}
	
	public void updateRedstone()
	{
		int fullSlot = 0;
		boolean totalEmpty = true;
		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			ItemStack stack = getStackInSlot(i);
			if (stack != null)
			{
				if (stack.stackSize >= stack.getMaxStackSize())
				{
					fullSlot++;
				}
				totalEmpty = false;
			}
		}
		if (fullSlot == this.getSizeInventory())
		{
			isFull = true;
		}
		else
		{
			isFull = false;
		}
		
		if (totalEmpty)
		{
			isEmpty = true;
		}
		else
		{
			isEmpty = false;
		}
		update = true;
	}
	
	@Override
	public int isPowering(int side)
	{
		if (side == ForgeDirection.UP.ordinal())
		{
			if (isEmpty)
			{
				return 15;
			}
		}
		else if (side == ForgeDirection.DOWN.ordinal())
		{
			if (isFull)
			{
				return 15;
			}
		}
		return 0;
	}
	
	@Override
	public boolean canConnectToWire(int side)
	{
		return true;
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(TinyItems.advTinyChest, 1, mode - 1);
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return Block.bedrock.getIcon(0, 0);
	}
	
	@Override
	public boolean shouldCheckWeakPower(int side)
	{
		return true;
	}

	@Override
	public boolean SolidOnSide(ForgeDirection side)
	{
		if(side == ForgeDirection.DOWN)
		{
			return false;
		}
		return super.SolidOnSide(side);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
	}

	@Override
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
	}
	
}
