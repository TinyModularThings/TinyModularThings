package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.tiles.interfaces.IAcceptor;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiStructureFluidInterface extends AdvTile implements
		IFluidHandler, IAcceptor
{
	
	public int blockID = -1;
	public int metadata = -1;
	
	IFluidHandler target = null;
	
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 1);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		return stack;
	}
	
	
	
	@Override
	public ItemStack getItemDrop()
	{
		return ItemInterfaceBlock.addBlockToInterface(new ItemStack(TinyItems.interfaceBlock, 1, 1), new BlockStack(blockID, metadata));
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (target != null)
		{
			return target.fill(from, resource, doFill);
		}
		return 0;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (target != null)
		{
			return target.drain(from, resource, doDrain);
		}
		return null;
	}
	
	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		if (target != null)
		{
			return target.drain(from, maxDrain, doDrain);
		}
		return null;
	}
	
	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if (target != null)
		{
			return target.canFill(from, fluid);
		}
		return false;
	}
	
	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		if (target != null)
		{
			return target.canDrain(from, fluid);
		}
		return false;
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		if (target != null)
		{
			return target.getTankInfo(from);
		}
		return new FluidTankInfo[0];
	}
	
	@Override
	public AcceptorType getType()
	{
		return AcceptorType.Fluids;
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
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata);
		}
		return null;
	}
		
	@Override
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 1)
		{
			return TextureEngine.getTextures().getTexture(TinyBlocks.transportBlock, 2, 0);
		}
		
		if (blockID != -1 && metadata != -1)
		{
			return new BlockStack(blockID, metadata).getTexture(side);
		}
		return null;
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
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setIntArray("BlockStack", new int[] { blockID, metadata });
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



	@Override
	public boolean hasMaster()
	{
		return target != null;
	}



	@Override
	public void setMaster(TileEntity par1)
	{
		target = (IFluidHandler)par1;
	}



	@Override
	public void removeMaster()
	{
		target = null;
	}
}
