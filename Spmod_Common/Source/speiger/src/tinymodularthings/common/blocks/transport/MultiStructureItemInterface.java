package speiger.src.tinymodularthings.common.blocks.transport;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.packets.IPacketReciver;
import speiger.src.api.common.data.packets.SpmodPacketHelper.ModularPacket;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.api.common.world.tiles.interfaces.InterfaceAcceptor;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
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
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		BlockPosition pos = new BlockPosition(worldObj, x, y, z);
		if(pos != null && pos.doesBlockExsist() && pos.hasTileEntity())
		{
			String target = pos.getAsBlockStack().getBlockDisplayName();
			if (pos != null)
			{
				par1.getFontRenderer().drawString("Target: " + target, guiX - 40 - par1.getFontRenderer().getStringWidth("Target: " + target) / 2, 20, 4210752);
				par1.getFontRenderer().drawString("Choosen Slot", 50, 35, 4210752);
				par1.getFontRenderer().drawString("" + choosenSlot, 80, 56, 4210752);
			}
			
			par1.getButtonsList().clear();
			par1.getButtonsList().add(new GuiButton(0, guiX + 40, guiY + 50, 20, 20, "-"));
			par1.getButtonsList().add(new GuiButton(1, guiX + 105, guiY + 50, 20, 20, "+"));
		}
		else
		{
			par1.getFontRenderer().drawString("No Target", 60, 35, 4210752);
		}
	}
	
	

	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		if(target != null)
		{
			int id = par2.id;
			if(id == 0)
			{
				sendPacketToServer(createBasicPacket(TinyModularThings.instance).InjectNumber(0).injectBoolean(false).finishPacket());
			}
			else if(id == 1)
			{
				sendPacketToServer(createBasicPacket(TinyModularThings.instance).InjectNumber(0).injectBoolean(true).finishPacket());
			}
		}
	}



	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 1)
		{
			return TextureEngine.getTextures().getTexture(TinyBlocks.transportBlock, 1, 0);
		}
		
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
		if ((!worldObj.isRemote && textureUpdate))
		{
			textureUpdate = false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
		}
		
		if (worldObj.getWorldTime() % 20 != 0)
		{
			return;
		}
		if (!active)
		{
			changed = false;
		}
		active = false;
		updateInventory();
		
	}
	
	@Override
	public void onBreaking()
	{
		this.removeInventory();
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drop = new ArrayList<ItemStack>();
		drop.add(ItemInterfaceBlock.addBlockToInterface(new ItemStack(TinyItems.interfaceBlock, 1, 0), this.getBlock()));
		return drop;
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
		if (target != null)
		{
			((InterfaceAcceptor) target).removeAcceptor(this);
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
						textureUpdate = true;
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
						textureUpdate = true;
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
	public boolean hasContainer()
	{
		return true;
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
	public void recivePacket(DataInput par1)
	{
		if (!worldObj.isRemote)
		{
			try
			{
				int eventID = par1.readInt();
				if(eventID == 0 && target != null)
				{
					boolean plus = par1.readBoolean();
					if(plus)
					{
						if(choosenSlot + 1 < target.getSizeInventory())
						{
							choosenSlot++;
						}
					}
					else
					{
						if(choosenSlot > 0)
						{
							choosenSlot--;
						}
					}
					textureUpdate = true;
				}
			}
			catch (IOException e)
			{
				return;
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
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
	}

	@Override
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		for(int i = 0;i<2;i++)
		{
			this.setRenderPass(i);
			renderer.renderStandardBlock(block, xCoord, yCoord, zCoord);
		}
	}

	@Override
	public boolean isBlockPresent(BlockStack par1)
	{
		boolean flag = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition pos = getPosition().add(dir);
			if(par1.match(pos.getAsBlockStack()))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public int getSideFromBlock(BlockStack par1)
	{
		int side = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition pos = getPosition().add(dir);
			if(par1.match(pos.getAsBlockStack()))
			{
				side = dir.ordinal();
				break;
			}
		}
		return side;
	}

	@Override
	public boolean isTilePressent(Class par1)
	{
		boolean flag = false;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition pos = getPosition().add(dir);
			if(pos.hasTileEntity() && par1.isAssignableFrom(pos.getTileEntity().getClass()))
			{
				flag = true;
				break;
			}
		}
		
		return flag;
	}

	@Override
	public <T> T getTileEntity(Class<T> par1)
	{
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition pos = getPosition().add(dir);
			if(pos.hasTileEntity() && par1.isAssignableFrom(pos.getTileEntity().getClass()))
			{
				return (T)pos.getBlockTileEntity();
			}
		}
		return null;
	}

	@Override
	public int getSideFromTile(Class par1)
	{
		int side = 0;
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			BlockPosition pos = getPosition().add(dir);
			if(pos.hasTileEntity() && par1.isAssignableFrom(pos.getTileEntity().getClass()))
			{
				side = dir.ordinal();
				break;
			}
		}
		return side;
	}
}