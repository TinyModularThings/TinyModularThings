package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.block.Block;
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

public class TinyChest extends FacedInventory
{
	public int mode = -1;
	
	public TinyChest()
	{
		super(0);
	}
	
	@Override
	public boolean canMergeItem(ItemStack par1, int slotID)
	{
		return true;
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return false;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return null;
	}
	
	@Override
	public boolean SolidOnSide(ForgeDirection side)
	{
		if(side == ForgeDirection.DOWN)
		{
			return true;
		}
		return super.SolidOnSide(side);
	}
	
	public void setMode(int mode)
	{
		this.mode = mode;
	}
	
	public int getMode()
	{
		return mode;
	}
	
	public boolean isValidMode()
	{
		return mode != -1;
	}
	
	@Override
	public void updateBlock()
	{
		PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 30, worldObj.provider.dimensionId, getDescriptionPacket());
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
	public ItemStack getItemDrop()
	{
		return new ItemStack(TinyItems.tinyChest, 1, mode - 1);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		mode = nbt.getInteger("Mode");
		super.readFromNBT(nbt);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("Mode", mode);
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
	
	@Override
	public float getBlockHardness()
	{
		return 2F;
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
	public int getSizeInventory()
	{
		return mode;
	}
	
	@Override
	public String getInvName()
	{
		return "Tiny Chest";
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		return new ItemStack(TinyItems.tinyChest, 1, mode - 1);
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
	public void addContainerSlots(AdvContainer par1)
	{
		int[][] SlotX = new int[][] { {}, { 80 }, { 70, 88 }, { 62, 80, 98 }, { 52, 70, 88, 106 }, { 44, 62, 80, 98, 116 }, { 34, 52, 70, 88, 106, 124 }, { 26, 44, 62, 80, 98, 116, 134 }, { 16, 34, 52, 70, 88, 106, 124, 142 }, { 8, 26, 44, 62, 80, 98, 116, 134, 152 } };
		for(int i = 0;i<getSizeInventory();i++)
		{
			par1.addSpmodSlot(this, i, SlotX[getSizeInventory()][i], 30);
		}
	}
	
	
	
}
