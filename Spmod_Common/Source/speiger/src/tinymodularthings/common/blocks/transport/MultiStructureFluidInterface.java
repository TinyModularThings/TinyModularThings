package speiger.src.tinymodularthings.common.blocks.transport;

import java.util.ArrayList;

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
import speiger.src.api.common.world.tiles.interfaces.InterfaceAcceptor;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.data.StructureStorage;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyItems;
import speiger.src.tinymodularthings.common.items.itemblocks.transport.ItemInterfaceBlock;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MultiStructureFluidInterface extends AdvTile implements
		IFluidHandler, IAcceptor
{
	
	public int blockID = -1;
	public int metadata = -1;
	
	public int x = 0;
	public int y = 0;
	public int z = 0;
	
	IFluidHandler target = null;
	
	private boolean textureUpdate = false;
	
	public void removeTarget()
	{
		target = null;
		x = 0;
		y = 0;
		z = 0;
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
					if (inter.acceptFluids(this) && inter.addAcceptor(this))
					{
						target = (IFluidHandler) tile;
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
					if (inter.acceptFluids(this) && inter.addAcceptor(this))
					{
						target = (IFluidHandler) tile;
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
	
	public void updateData()
	{
		if ((x == 0 && y == 0 && z == 0) || !doesExsist() || (doesExsist() && target == null))
		{
			if (!doesExsist())
			{
				if (target == null)
				{
					return;
				}
				removeTarget();
				return;
			}
			findInventory();
		}
	}
	
	@Override
	public void onTick()
	{
		if (worldObj.isRemote || worldObj.getWorldTime() % 20 == 0)
		{
			return;
		}
		
		if (!worldObj.isRemote)
		{
			updateData();
		}
		if ((!worldObj.isRemote && textureUpdate) || (!worldObj.isRemote && worldObj.getWorldTime() % 200 == 0))
		{
			textureUpdate = false;
			PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 20, worldObj.provider.dimensionId, getDescriptionPacket());
		}
		
	}
	
	@Override
	public ItemStack pickBlock(MovingObjectPosition target)
	{
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 1);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		return stack;
	}
	
	@Override
	public ArrayList<ItemStack> onDrop(int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack stack = new ItemStack(TinyItems.interfaceBlock, 1, 1);
		ItemInterfaceBlock.addBlockToInterface(stack, new BlockStack(blockID, metadata));
		drops.add(stack);
		return drops;
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
	public Icon getIconFromSideAndMetadata(int side, int renderPass)
	{
		if(renderPass == 1)
		{
			return TextureEngine.getTextures().getTexture(TinyBlocks.transportBlock, 2, 0);
		}
		
		if (blockID != -1 && metadata != -1 && renderPass == 1)
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
		array = par1.getIntArray("Coords");
		if (array != null && array.length == 3)
		{
			x = array[0];
			y = array[1];
			z = array[2];
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1)
	{
		super.writeToNBT(par1);
		par1.setIntArray("BlockStack", new int[] { blockID, metadata });
		par1.setIntArray("Coords", new int[] { x, y, z });
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
}
