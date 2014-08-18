package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.common.FMLCommonHandler;

public class EnderChestReader extends AdvTile implements IInventory
{
	
	String owner = "None";
	
	@Override
	public IIcon getIconFromSideAndMetadata(int side, int renderPass)
	{
		return Blocks.end_portal.getIcon(0, 0);
	}
	
	@Override
	public void setupUser(EntityPlayer player)
	{
		owner = player.getCommandSenderName();
	}
	
	public InventoryEnderChest findEnderChest()
	{
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			int x = xCoord + ForgeDirection.getOrientation(i).offsetX;
			int y = yCoord + ForgeDirection.getOrientation(i).offsetY;
			int z = zCoord + ForgeDirection.getOrientation(i).offsetZ;
			
			TileEntity tile = worldObj.getTileEntity(x, y, z);
			if (tile != null && tile instanceof TileEntityEnderChest)
			{
				EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152612_a(owner);
				if (player != null)
				{
					return player.getInventoryEnderChest();
				}
			}
		}
		return null;
	}
	
	@Override
	public int getSizeInventory()
	{
		return findEnderChest() != null ? findEnderChest().getSizeInventory() : 0;
	}
	
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return findEnderChest() != null ? findEnderChest().getStackInSlot(i) : null;
	}
	
	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return findEnderChest() != null ? findEnderChest().decrStackSize(i, j) : null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return findEnderChest() != null ? findEnderChest().getStackInSlotOnClosing(i) : null;
	}
	
	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		if (findEnderChest() != null)
		{
			findEnderChest().setInventorySlotContents(i, itemstack);
		}
		
	}
	

	@Override
	public int getInventoryStackLimit()
	{
		return findEnderChest() != null ? findEnderChest().getInventoryStackLimit() : 0;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}
	
	
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		owner = nbt.getString("Owner");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("Owner", owner);
	}

	@Override
	public String getInventoryName()
	{
		return "EnderChest Reader";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}
	
}
