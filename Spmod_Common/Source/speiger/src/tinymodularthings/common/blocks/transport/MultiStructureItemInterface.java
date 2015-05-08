package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.data.packets.SpmodPacketHelper.SpmodPacket;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.spmodapi.SpmodAPI;
import speiger.src.spmodapi.client.gui.GuiInventoryCore;
import speiger.src.spmodapi.common.network.packets.base.TileNBTPacket;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import speiger.src.tinymodularthings.common.plugins.BC.actions.ActionOneSlotChange;
import speiger.src.tinymodularthings.common.plugins.BC.actions.GateChangeToSlot;
import buildcraft.api.gates.IAction;
import buildcraft.api.gates.IActionReceptor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiStructureItemInterface extends AdvTile implements IInventory,
		IAcceptor, IActionReceptor
{
	
	public int blockID = -1;
	public int metadata = -1;
	
	public IInventory target = null;
	
	public int choosenSlot = 0;
	public boolean changed = false;
	public boolean active = false;
	public boolean exsist = true;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void drawFrontExtras(GuiInventoryCore par1, int guiX, int guiY, int mouseX, int mouseY)
	{
		
		if(target != null)
		{
			BlockPosition pos = new BlockPosition((TileEntity)target);
			if(pos.doesBlockExsist() && pos.hasTileEntity())
			{
				String target = pos.getAsBlockStack().getBlockDisplayName();
				if(pos != null)
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
		else
		{
			par1.getFontRenderer().drawString("No Target", 60, 35, 4210752);
		}
	}
	
	@Override
	public void onBreaking()
	{
		super.onBreaking();
		exsist = false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onButtonClick(GuiInventoryCore par1, GuiButton par2)
	{
		if(target != null)
		{
			int id = par2.id;
			if(id != 0 && id != 1)
			{
				return;
			}
			
			TileNBTPacket packet = new TileNBTPacket(this);
			NBTTagCompound nbt = packet.getData();
			//1 = Up & 0 = Down
			nbt.setBoolean("SlotChange", id == 1);
			this.sendPacketToServer(SpmodAPI.handler.createFinishPacket(packet));
		}
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 1)
		{
			return TextureEngine.getTextures().getTexture(TinyBlocks.transportBlock, 1, 0);
		}
		
		if(blockID != -1 && metadata != -1)
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
	
	@Override
	public boolean hasMaster()
	{
		return target != null;
	}

	@Override
	public void setMaster(TileEntity par1)
	{
		target = (IInventory)par1;
		updateInfo();
	}

	@Override
	public void removeMaster()
	{
		target = null;
		if(this.exsist)
		{
			updateInfo();
		}
	}

	public void updateInfo()
	{
		boolean exsist = hasMaster();
		TileNBTPacket packet = new TileNBTPacket(this);
		NBTTagCompound nbt = packet.getData();
		nbt.setBoolean("Master", exsist);
		if(exsist)
		{
			TileEntity master = (TileEntity)target;
			int[] coords = new int[]{master.xCoord, master.yCoord, master.zCoord};
			nbt.setIntArray("Pos", coords);
		}
		this.sendPacketToClient(SpmodAPI.handler.createFinishPacket(packet), 20);
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}
	
	@Override
	public ItemStack getItemDrop()
	{
		return ItemInterfaceBlock.addBlockToInterface(new ItemStack(TinyItems.interfaceBlock, 1, 0), this.getBlock());
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
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	@Override
	public BlockStack getBlock()
	{
		if(blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata);
		}
		return null;
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
		if(target != null)
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
		if(array != null && array.length == 2)
		{
			blockID = array[0];
			metadata = array[1];
		}
		choosenSlot = par1.getInteger("Slot");
		active = par1.getBoolean("Active");
		changed = par1.getBoolean("Changed");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setIntArray("BlockStack", new int[] {blockID, metadata });
		par1.setInteger("Slot", choosenSlot);
		par1.setBoolean("Active", active);
		par1.setBoolean("Changed", changed);
	}
	
	@Override
	public void onSpmodPacket(SpmodPacket par1)
	{
		if(par1.getPacket() instanceof TileNBTPacket)
		{
			TileNBTPacket packet = (TileNBTPacket)par1.getPacket();
			NBTTagCompound nbt = packet.getData();
			if(nbt.hasKey("Master"))
			{
				if(nbt.getBoolean("Master"))
				{
					int[] pos = nbt.getIntArray("Pos");
					if(pos.length != 3)
					{
						return;
					}
					target = (IInventory)worldObj.getBlockTileEntity(pos[0], pos[1], pos[2]);
				}
				else
				{
					target = null;
				}
			}
			if(nbt.hasKey("SlotChange"))
			{
				boolean plus = nbt.getBoolean("SlotChange");
				choosenSlot += plus ? 1 : -1;
			}
		}
	}
	
	@Override
	public void actionActivated(IAction action)
	{
		if(action != null)
		{
			if(action instanceof GateChangeToSlot)
			{
				GateChangeToSlot change = (GateChangeToSlot)action;
				if(target != null && change.getSlotID() != choosenSlot && target.getSizeInventory() > change.getSlotID())
				{
					if(!worldObj.isRemote && worldObj.getWorldTime() % 20 == 0)
					{
						if(choosenSlot > change.getSlotID())
						{
							choosenSlot--;
						}
						else if(choosenSlot < change.getSlotID())
						{
							choosenSlot++;
						}
					}
				}
			}
			else if(action instanceof ActionOneSlotChange)
			{
				if(!changed && target != null)
				{
					int swt = ((ActionOneSlotChange)action).plus ? 1 : 0;
					switch(swt)
					{
						case 0:
							if(choosenSlot + 1 == target.getSizeInventory())
							{
								choosenSlot = 0;
							}
							else
							{
								choosenSlot++;
							}
							break;
						case 1:
							if(choosenSlot - 1 < 0)
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
				}
				active = true;
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderInv(BlockStack stack, RenderBlocks render)
	{
	}
	
	@Override
	public void onRenderWorld(Block block, RenderBlocks renderer)
	{
		for(int i = 0;i < 2;i++)
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

	@Override
	public void onReciveGuiInfo(int key, int val)
	{
		super.onReciveGuiInfo(key, val);
		if(key == 200)
		{
			choosenSlot = val;
		}
	}

	@Override
	public void onSendingGuiInfo(Container par1, ICrafting par2)
	{
		super.onSendingGuiInfo(par1, par2);
		par2.sendProgressBarUpdate(par1, 200, choosenSlot);
	}
	
	@Override
	public boolean ImMaster(TileEntity par1)
	{
		TileEntity tile = (TileEntity)target;
		if(tile == null && par1 == null)
		{
			return true;
		}
		if(tile != null && par1 != null)
		{
			return tile.worldObj.provider.dimensionId == par1.worldObj.provider.dimensionId && tile.xCoord == par1.xCoord && tile.yCoord == par1.yCoord && tile.zCoord == par1.zCoord;
		}
		return false;
	}
}