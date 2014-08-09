package speiger.src.tinymodularthings.common.blocks.transport;

import java.io.DataInput;
import java.io.IOException;

import javax.swing.Icon;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import speiger.src.api.blocks.BlockStack;
import speiger.src.api.inventory.IAcceptor;
import speiger.src.api.inventory.InterfaceAcceptor;
import speiger.src.api.packets.IPacketReciver;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.BlockPosition;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.client.gui.transport.ItemInterfaceGui;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiStructureItemInterface extends AdvTile implements IInventory,
		IAcceptor, IPacketReciver, IActionReceptor
{
	
	public int blockID = -1;
	public int metadata = -1;
	
	// Structure Coords
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	public IInventory target = null;
	
	public int choosenSlot = 0;
	public boolean textureUpdate = false;
	public boolean changed = false;
	public boolean active = false;
	
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata).getTexture(side);
		}
		return null;
	}
	
	
	


	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 0);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		return stack;
	}

	public boolean hasTarget()
	{
		return target != null;
	}
	
	@Override
	public void onTick()
	{
		
		if ((!worldObj.isRemote && textureUpdate) || (!worldObj.isRemote && worldObj.getWorldTime() % 200 == 0))
		{
			textureUpdate = false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
		}
		
		if (worldObj.isRemote || worldObj.getWorldTime() % 20 != 0)
		{
			return;
		}
		
		if (!active)
		{
			changed = false;
		}
		active = false;
		
		if (!worldObj.isRemote)
		{
			updateInventory();
		}
		
	}
	
	
	
	@Override
	public void onBreaking()
	{
		this.removeInventory();
		ItemStack stack = ItemInterfaceBlock.addBlockToInterface(new ItemStack(TinyItems.interfaceBlock, 1, 0), this.getBlock());
		EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
		worldObj.spawnEntityInWorld(item);
	}





	public void updateInventory()
	{
		if ((x == 0 && y == 0 && z == 0) || !doesExsist() || (doesExsist() && target == null))
		{
			
			if (!doesExsist())
			{
				if (target == null)
				{
					
					return;
				}
				removeInventory();
				return;
			}
			findInventory();
		}
	}
	
	private void removeInventory()
	{
		if(target != null)
		{
			((InterfaceAcceptor)target).removeAcceptor(this);
		}
		target = null;
		x = 0;
		y = 0;
		z = 0;
		choosenSlot = 0;
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	public void findInventory()
	{
		if (x == 0 && y == 0 && z == 0)
		{
			BlockPosition pos = StructureStorage.instance.getCorePosition(getPosition());
			if (pos != null)
			{
				TileEntity tile = pos.getTileEntity();
				if (tile != null && tile instanceof InterfaceAcceptor)
				{
					InterfaceAcceptor inter = (InterfaceAcceptor) tile;
					if (inter.acceptItems(this) && inter.addAcceptor(this))
					{
						target = (IInventory) tile;
						x = tile.xCoord;
						y = tile.yCoord;
						z = tile.zCoord;
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					}
				}
			}
		}
		else
		{
			
			BlockPosition pos = new BlockPosition(worldObj, x, y, z);
			if (pos != null && pos.doesBlockExsist() && pos.hasTileEntity())
			{
				TileEntity tile = pos.getTileEntity();
				
				if (tile instanceof InterfaceAcceptor)
				{
					
					InterfaceAcceptor inter = (InterfaceAcceptor) tile;
					if (inter.acceptItems(this) && inter.addAcceptor(this))
					{
						
						target = (IInventory) tile;
						worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType().blockID);
					}
				}
				
			}
		}
	}
	
	public boolean doesExsist()
	{
		if (x == 0 && y == 0 && z == 0)
		{
			return StructureStorage.instance.isRegistered(getPosition());
		}
		else
		{
			BlockPosition pos = new BlockPosition(worldObj, x, y, z);
			return pos.doesBlockExsist() && pos.hasTileEntity() && pos.getTileEntity() instanceof InterfaceAcceptor;
		}
	}
	
	@Override
	public AcceptorType getType()
	{
		return AcceptorType.Items;
	}
	
	@Override
	public boolean isBlock(BlockStack par1)
	{
		return blockID == par1.getBlockID() && metadata == par1.getMeta();
	}
	
	@Override
	public void setBlock(BlockStack par1)
	{
		blockID = par1.getBlockID();
		metadata = par1.getMeta();
		textureUpdate = true;
	}
	
	@Override
	public BlockStack getBlock()
	{
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata);
		}
		return null;
	}
	
	@Override
	public void targetLeave(TileEntity tile)
	{
		if (tile != null)
		{
			if (tile.worldObj.provider.dimensionId == worldObj.provider.dimensionId && tile.xCoord == x && tile.yCoord == y && tile.zCoord == z)
			{
				target = null;
				x = 0;
				y = 0;
				z = 0;
			}
		}
		
	}
	
	@Override
	public int getSizeInventory()
	{
		return target != null ? 1 : 0;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return target != null ? target.getStackInSlot(choosenSlot) : null;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return target != null ? target.decrStackSize(choosenSlot, j) : null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return target != null ? target.getStackInSlotOnClosing(choosenSlot) : null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if (target != null)
		{
			target.setInventorySlotContents(choosenSlot, itemstack);
		}
	}
	
	@Override
	public String getInvName()
	{
		return "Item Interface";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return target != null ? target.getInventoryStackLimit() : 0;
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
	public boolean onActivated(EntityPlayer par1)
	{
		
		if (hasContainer())
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
	@SideOnly(Side.CLIENT)
	public GuiContainer getGui(InventoryPlayer par1)
	{
		return new ItemInterfaceGui(this, par1);
	}
	
	@Override
	public Container getInventory(InventoryPlayer par1)
	{
		return new AdvContainer(par1)
		{
			
			@Override
			public boolean canInteractWith(EntityPlayer entityplayer)
			{
				return true;
			}
			
		};
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1)
	{
		super.readFromNBT(par1);
		int[] array = par1.getIntArray("BlockStack");
		if (array != null && array.length == 2)
		{
			blockID = array[0];
			metadata = array[1];
		}
		array = par1.getIntArray("Coords");
		if (array != null && array.length == 3)
		{
			x = array[0];
			y = array[1];
			z = array[2];
		}
		choosenSlot = par1.getInteger("Slot");
		active = par1.getBoolean("Active");
		changed = par1.getBoolean("Changed");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setIntArray("BlockStack", new int[] { blockID, metadata });
		par1.setIntArray("Coords", new int[] { x, y, z });
		par1.setInteger("Slot", choosenSlot);
		par1.setBoolean("Active", active);
		par1.setBoolean("Changed", changed);
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, nbt);
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
	{
		readFromNBT(pkt.data);
	}
	
	@Override
	public void recivePacket(DataInput par1)
	{
		if (!worldObj.isRemote)
		{
			int nextID;
			try
			{
				nextID = par1.readInt();
				
			}
			catch (IOException e)
			{
				return;
			}
			if (nextID != choosenSlot)
			{
				choosenSlot = nextID;
			}
		}
		
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		if (action != null)
		{
			if (action instanceof GateChangeToSlot)
			{
				GateChangeToSlot change = (GateChangeToSlot) action;
				if (target != null && change.getSlotID() != choosenSlot && target.getSizeInventory() > change.getSlotID())
				{
					if (!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0)
					{
						if (choosenSlot > change.getSlotID())
						{
							choosenSlot--;
						}
						else if (choosenSlot < change.getSlotID())
						{
							choosenSlot++;
						}
						textureUpdate = true;
					}
				}
			}
			else if (action instanceof ActionOneSlotChange)
			{
				if (!changed && target != null)
				{
					int swt = ((ActionOneSlotChange) action).plus ? 1 : 0;
					switch (swt)
					{
						case 0:
							if (choosenSlot + 1 == target.getSizeInventory())
							{
								choosenSlot = 0;
							}
							else
							{
								choosenSlot++;
							}
							break;
						case 1:
							if (choosenSlot - 1 < 0)
							{
								choosenSlot = target.getSizeInventory() - 1;
							}
							else
							{
								choosenSlot--;
							}
							break;
					}
					changed = true;
					textureUpdate = true;
				}
				active = true;
			}
		}
	}





	@Override
	public String identifier()
	{
		return null;
	}
	
}
