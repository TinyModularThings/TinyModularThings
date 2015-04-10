package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.items.plates.PlateManager;
import speiger.src.spmodapi.common.tile.TileFacing;
import speiger.src.spmodapi.common.util.TextureEngine;

public class MultiPlate extends TileFacing
{
	public String identity = "";
	
	
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}

	@Override
	public boolean isSixSidedFacing()
	{
		return true;
	}
	
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		try
		{
			return PlateManager.plates.getInfoFromIdentity(getIdentity()).getItem().getIconFromDamage(PlateManager.plates.getAllIdentifiers().indexOf(getIdentity()));
		}
		catch(Exception e)
		{
		}
		return TextureEngine.getTextures().getIconSafe();
	}
	
	
	
	@Override
	public float getBlockHardness()
	{
		try
		{
			return PlateManager.plates.getInfoFromIdentity(getIdentity()).getHardness();
		}
		catch(Exception e)
		{
		}
		return 1F;
	}

	@Override
	public AxisAlignedBB getColidingBox()
	{
		return null;
	}

	@Override
	public boolean SolidOnSide(ForgeDirection side)
	{
		return false;
	}

	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		ItemStack stack = PlateManager.plates.getInfoFromIdentity(getIdentity()).getItemStack();
		return stack;
	}

	public void setIdentity(String identity)
	{
		this.identity = identity;
	}

	@Override
	public AxisAlignedBB getSelectedBoxes()
	{
		double i = 0.055D;
		switch(getFacing())
		{
			case 0: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord + (1D - i), zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
			case 1: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + i, zCoord + 1);
			case 2: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord + (1D - i), xCoord + 1, yCoord + 1, zCoord + 1);
			case 3: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + i);
			case 4: return AxisAlignedBB.getAABBPool().getAABB(xCoord + (1D - i), yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
			case 5: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + i, yCoord + 1, zCoord + 1);
			default: return AxisAlignedBB.getAABBPool().getAABB(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
		}
	}

	@Override
	public void onBlockChange(Block par1, int par2)
	{
		int facing = getFacing();
		boolean drop = false;
		if (facing == ForgeDirection.DOWN.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord, yCoord + 1, zCoord, ForgeDirection.UP))
			{
				drop = true;
			}
		}
		if (facing == ForgeDirection.UP.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord, yCoord - 1, zCoord, ForgeDirection.DOWN))
			{
				drop = true;
			}
		}
		if (facing == ForgeDirection.EAST.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord - 1, yCoord, zCoord, ForgeDirection.WEST))
			{
				drop = true;
			}
		}
		if (facing == ForgeDirection.WEST.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord + 1, yCoord, zCoord, ForgeDirection.EAST))
			{
				drop = true;
			}
		}
		if (facing == ForgeDirection.NORTH.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord, yCoord, zCoord + 1, ForgeDirection.SOUTH))
			{
				drop = true;
			}
		}
		if (facing == ForgeDirection.SOUTH.ordinal())
		{
			if (!getWorldObj().isBlockSolidOnSide(xCoord, yCoord, zCoord - 1, ForgeDirection.NORTH))
			{
				drop = true;
			}
		}
		
		if (drop)
		{
			getWorldObj().setBlock(xCoord, yCoord, zCoord, 0);
			if (!getWorldObj().isRemote)
			{
				try
				{
					ItemStack stack = PlateManager.plates.getInfoFromIdentity(getIdentity()).getItemStack();
					EntityItem item = new EntityItem(worldObj, xCoord, yCoord, zCoord, stack);
					if(item != null)
					{
						item.delayBeforeCanPickup = 10;
						worldObj.spawnEntityInWorld(item);
					}
				}
				catch(Exception e)
				{
				}
			}
			getWorldObj().markTileEntityForDespawn(this);
		}
	}
	
	

	@Override
	public boolean onClick(boolean sneak, EntityPlayer par1, Block par2, int side)
	{
		if(sneak)
		{
			this.setRotation((short)((short)this.getRotation()+1));
			if(getRotation() > 3)
			{
				this.setRotation((short)0);
			}
			this.updateNeighbors(true);
			this.updateBlock();
			return true;
		}
		return false;
	}

	@Override
	public void setBoundsOnState(Block par1)
	{
		float i = 0.055F;
		switch(getFacing())
		{
			case 0:	
				par1.setBlockBounds(0.0F, (1F - i), 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 1:
				par1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F + i, 1.0F);
				break;
			case 2:
				par1.setBlockBounds(0.0F, 0.0F, (1F - i), 1.0F, 1.0F, 1.0F);
				break;
			case 3:
				par1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F + i);
				break;
			case 4:
				par1.setBlockBounds((1F - i), 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case 5:
				par1.setBlockBounds(0.0F, 0.0F, 0.0F, 0.0F + i, 1.0F, 1.0F);
				break;
			default:
				par1.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public boolean canPlacedOnSide(ForgeDirection side)
	{
		Block block = Block.blocksList[WorldReading.getBlockId(getWorldObj(), xCoord, yCoord, zCoord, side.ordinal())];
		if(block == null)
		{
			return false;
		}
		return block.isBlockSolidOnSide(getWorldObj(), xCoord, yCoord, zCoord, side);
	}

	@Override
	public boolean isNormalCube()
	{
		return false;
	}

	public String getIdentity()
	{
		return identity;
	}
	
	@Override
	public boolean isSixSidedRotation()
	{
		return false;
	}
	
	public ResourceLocation getTexture()
	{
		ResourceLocation texture = PlateManager.plates.getTexture(identity);
		if (texture == null)
		{
			texture = new ResourceLocation("Error");
		}
		return texture;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.setIdentity(nbt.getString("ID"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("ID", getIdentity());
	}

	@Override
	public ItemStack getItemDrop()
	{
		return PlateManager.plates.getInfoFromIdentity(getIdentity()).getItemStack();
	}
	
	
	
}
