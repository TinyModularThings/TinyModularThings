package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import cpw.mods.fml.common.FMLCommonHandler;

public class EnderChestReader extends AdvTile implements IInventory
{
	
	String owner = "None";
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		TextureEngine engine = TextureEngine.getTextures();
		Icon result = engine.getTexture(new BlockStack(TinyBlocks.transportBlock, 0), 0);
		if(engine.isMissingTexture(result))
		{
			result = Block.portal.getIcon(0, 0);
		}
		return result;
	}
	
	@Override
	public void setupUser(EntityPlayer player)
	{
		owner = player.username;
	}

	public InventoryEnderChest findEnderChest()
	{
		for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++)
		{
			int x = xCoord + ForgeDirection.getOrientation(i).offsetX;
			int y = yCoord + ForgeDirection.getOrientation(i).offsetY;
			int z = zCoord + ForgeDirection.getOrientation(i).offsetZ;
			
			TileEntity tile = worldObj.getBlockTileEntity(x, y, z);
			if (tile != null && tile instanceof TileEntityEnderChest)
			{
				EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(owner);
				if (player != null)
				{
					return player.getInventoryEnderChest();
				}
			}
		}
		return null;
	}
	
	@Override
	public void registerIcon(TextureEngine par1)
	{
		par1.registerTexture(TinyBlocks.transportBlock, "enderChestReader");
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
	public String getInvName()
	{
		return "EnderChest Reader";
	}
	
	@Override
	public boolean isInvNameLocalized()
	{
		return false;
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
	
}
