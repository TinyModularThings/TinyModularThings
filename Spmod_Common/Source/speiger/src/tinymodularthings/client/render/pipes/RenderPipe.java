package speiger.src.tinymodularthings.client.render.pipes;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.energy.IEnergyProvider;
import speiger.src.api.pipes.IAdvancedPipeProvider;
import speiger.src.api.pipes.IBasicPipeProvider;
import speiger.src.tinymodularthings.common.blocks.pipes.basic.BlockPipe;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.interfaces.IBasicPipe;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderPipe implements ISimpleBlockRenderingHandler
{
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		RenderBlocks.renderItemIn3d(getRenderId());
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		
		if (block instanceof BlockPipe)
		{
			renderBlock(renderer, (BlockPipe) block, world, x, y, z);
		}
		
		return true;
	}
	
	double min = 0.25;
	double max = 0.75;
	
	double[][] facings = new double[][] { { min, 0.0D, min, max, min, max }, { min, max, min, max, 1.0D, max }, { min, min, 0.0D, max, max, min }, { min, min, max, max, max, 1.0D }, { 0.0D, min, min, min, max, max }, { max, min, min, 1.0D, max, max } };
	
	public void renderBlock(RenderBlocks render, BlockPipe pipe, IBlockAccess world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z) != pipe.blockID)
		{
			return;
		}
		
		int meta = world.getBlockMetadata(x, y, z) % 6;
		
		ForgeDirection front = ForgeDirection.getOrientation(meta);
		
		render.setRenderBounds(min, min, min, max, max, max);
		render.renderStandardBlock(pipe, x, y, z);
		
		if (isConnected(ForgeDirection.UP, world, x, y, z))
		{
			render.setRenderBounds(min, max, min, max, 1.0D, max);
			render.renderStandardBlock(pipe, x, y, z);
		}
		if (isConnected(ForgeDirection.DOWN, world, x, y, z))
		{
			render.setRenderBounds(min, 0.0D, min, max, min, max);
			render.renderStandardBlock(pipe, x, y, z);
		}
		if (isConnected(ForgeDirection.SOUTH, world, x, y, z))
		{
			render.setRenderBounds(min, min, max, max, max, 1.0D);
			render.renderStandardBlock(pipe, x, y, z);
		}
		if (isConnected(ForgeDirection.NORTH, world, x, y, z))
		{
			render.setRenderBounds(min, min, 0.0D, max, max, min);
			render.renderStandardBlock(pipe, x, y, z);
		}
		if (isConnected(ForgeDirection.EAST, world, x, y, z))
		{
			render.setRenderBounds(max, min, min, 1.0D, max, max);
			render.renderStandardBlock(pipe, x, y, z);
		}
		if (isConnected(ForgeDirection.WEST, world, x, y, z))
		{
			render.setRenderBounds(0.0D, min, min, min, max, max);
			render.renderStandardBlock(pipe, x, y, z);
			
		}
		
		if (isPossibleReciver(front, world, x, y, z))
		{
			render.setOverrideBlockTexture(pipe.info.getDirectionIcon());
			double[] array = facings[front.ordinal()];
			render.setRenderBounds(array[0], array[1], array[2], array[3], array[4], array[5]);
			render.renderStandardBlock(pipe, x, y, z);
			render.clearOverrideBlockTexture();
		}
		
	}
	
	private boolean isPossibleReciver(ForgeDirection direction, IBlockAccess world, int xCoord, int yCoord, int zCoord)
	{
		int x = xCoord + direction.offsetX;
		int y = yCoord + direction.offsetY;
		int z = zCoord + direction.offsetZ;
		
		int id = world.getBlockId(x, y, z);
		int meta = world.getBlockMetadata(x, y, z) % 6;
		
		if (id == 0 || Block.blocksList[id] == null)
		{
			return false;
		}
		if (Block.blocksList[id].hasTileEntity(meta))
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if (tile instanceof IInventory || tile instanceof IFluidHandler || tile instanceof IPowerReceptor || tile instanceof IEnergyProvider || tile instanceof IPipeTile)
			{
				return true;
			}
		}
		if (Block.blocksList[id] instanceof IBasicPipe)
		{
			return true;
		}
		return isConnected(direction, world, xCoord, yCoord, zCoord);
	}
	
	private boolean isConnected(ForgeDirection direction, IBlockAccess world, int x, int y, int z)
	{
		int xCoord = x + direction.offsetX;
		int yCoord = y + direction.offsetY;
		int zCoord = z + direction.offsetZ;
		
		int id = world.getBlockId(xCoord, yCoord, zCoord);
		int meta = world.getBlockMetadata(xCoord, yCoord, zCoord) % 6;
		
		if (id == 0 || Block.blocksList[id] == null)
		{
			return false;
		}
		if (Block.blocksList[id].hasTileEntity(meta))
		{
			TileEntity tile = world.getBlockTileEntity(xCoord, yCoord, zCoord);
			if (tile != null)
			{
				
				if (tile instanceof IAdvancedPipeProvider)
				{
					IAdvancedPipeProvider pipe = (IAdvancedPipeProvider) tile;
					return pipe.canConnect(direction.getOpposite());
				}
				
				if (tile instanceof IBasicPipeProvider)
				{
					ForgeDirection provider = ((IBasicPipeProvider) tile).getConnectionSide(world, xCoord, yCoord, zCoord);
					if (x == xCoord + provider.offsetX && y == yCoord + provider.offsetY && z == zCoord + provider.offsetZ)
					{
						return true;
					}
				}
			}
		}
		
		if (Block.blocksList[id] instanceof IAdvancedPipeProvider)
		{
			return ((IAdvancedPipeProvider) Block.blocksList[id]).canConnect(direction.getOpposite());
		}
		
		if (Block.blocksList[id] instanceof IBasicPipeProvider)
		{
			ForgeDirection provider = ((IBasicPipeProvider) Block.blocksList[id]).getConnectionSide(world, xCoord, yCoord, zCoord);
			if (x == xCoord + provider.offsetX && y == yCoord + provider.offsetY && z == zCoord + provider.offsetZ)
			{
				return true;
			}
		}
		if (Block.blocksList[id] instanceof IBasicPipe)
		{
			ForgeDirection provider = ((IBasicPipe) Block.blocksList[id]).getNextDirection(world, xCoord, yCoord, zCoord);
			if (x == xCoord + provider.offsetX && y == yCoord + provider.offsetY && z == zCoord + provider.offsetZ)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldRender3DInInventory()
	{
		return false;
	}
	
	@Override
	public int getRenderId()
	{
		return EnumIDs.Pipe.getId();
	}
	
}
