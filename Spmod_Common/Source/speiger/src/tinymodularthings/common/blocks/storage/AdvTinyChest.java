package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.tile.FacedInventory;
import speiger.src.spmodapi.common.util.slot.AdvContainer;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AdvTinyChest extends FacedInventory
{
	public boolean isEmpty = false;
	public boolean isFull = false;
	public boolean update = true;
	public int mode = -1;

	public AdvTinyChest()
	{
		super(0);
	}
	
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
		this.changeInventory(getSizeInventory());
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, worldObj.provider.dimensionId, getDescriptionPacket());
	}
	
	@Override
	public boolean hasContainer()
	{
		return isValidMode();
	}
	
	@Override
	public void addContainerSlots(AdvContainer par1)
	{
		int[][] SlotX = new int[][] { {}, { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
		for(int i = 0;i<getSizeInventory();i++)
		{
			par1.addSpmodSlot(this, i, SlotX[getSizeInventory()][i], 30);
		}
	}

	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		mode = nbt.getInteger("Mode");
		isFull = nbt.getBoolean("isFull");
		isEmpty = nbt.getBoolean("isEmpty");
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
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
	public String getInvName()
	{
		return "Advanced TinyChest";
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
	public void onTick()
	{
		super.onTick();
		if (!worldObj.isRemote)
		{	
			if (worldObj.getWorldTime() % 20 == 0)
			{
				if(updateRedstone())
				{
					this.updateNeighbors(true);
				}
			}
		}
	}
	
	public boolean updateRedstone()
	{
		int fullSlot = 0;
		boolean totalEmpty = true;
		boolean saveFull = isFull;
		boolean saveEmpty = isEmpty;
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
		
		if(saveFull != isFull || saveEmpty != isEmpty)
		{
			return true;
		}
		
		return false;
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

	@Override
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyItems.advTinyChest, 1, mode - 1);
	}
	
}
