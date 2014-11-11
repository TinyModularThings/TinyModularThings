package speiger.src.tinymodularthings.common.blocks.pipes.basic;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.api.common.world.blocks.IAdvancedPipeProvider;
import speiger.src.api.common.world.blocks.IBasicPipeProvider;
import speiger.src.api.common.world.tiles.energy.IEnergyProvider;
import speiger.src.spmodapi.client.render.core.BlockRendererSpmodCore.BlockRendererHelper;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.tinymodularthings.common.config.ModObjects.TinyBlocks;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import speiger.src.tinymodularthings.common.interfaces.IBasicPipe;
import speiger.src.tinymodularthings.common.utils.PipeInformation;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.transport.IPipeTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Everything in this Class Will Change. So do not access it.
 */

@Deprecated
public class BlockPipe extends SpmodBlockBase implements IBasicPipe
{
	
	public PipeInformation info;
	
	public BlockPipe(int par1, PipeInformation par2, TextureEngine par3)
	{
		super(par1, Material.iron);
		setCreativeTab(CreativeTabs.tabFood);
		info = par2;
		info.injectBlock(this);
		par2.updateIcon(par3);
		setLightOpacity(0);
	}
	
	@Override
	public ForgeDirection getNextPipeDirection(World world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public ForgeDirection getNextDirection(IBlockAccess world, int x, int y, int z)
	{
		return ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public int getEnergyTransferlimit(World world, int x, int y, int z)
	{
		return this.getItemInforamtion(world, x, y, z).getEnergyTransferlimit();
	}
	
	@Override
	public int getLiquidTransferlimit(World world, int x, int y, int z)
	{
		return this.getItemInforamtion(world, x, y, z).getFluidTransferlimit();
	}
	
	@Override
	public int getItemTransferlimit(World world, int x, int y, int z)
	{
		return this.getItemInforamtion(world, x, y, z).getItemTransferlimit();
	}
	
	@Override
	public PipeInformation getItemInforamtion(World world, int x, int y, int z)
	{
		return info;
	}
	
	@Override
	public PipeInformation getItemInforamtion(IBlockAccess world, int x, int y, int z)
	{
		return info;
	}
	
	@Override
	public PipeInformation getItemInformation(ItemStack par1)
	{
		if (blockID != par1.itemID)
		{
			return null;
		}
		return info;
	}
	
	@Override
	public World getNextWorld(World world, int x, int y, int z)
	{
		return world;
	}
	
	public static Block[] getBasicPipes()
	{
		Block[] array = new Block[] { TinyBlocks.smallPipe, TinyBlocks.normalPipe, TinyBlocks.mediumPipe, TinyBlocks.bigPipe, TinyBlocks.biggerPipe, TinyBlocks.largePipe, TinyBlocks.hugePipe };
		return array;
	}
	
	public static int getRecipesLenght()
	{
		return getBasicPipes().length;
	}
	
	public static BlockStack[] getSlabs()
	{
		BlockStack[] array = new BlockStack[] { new BlockStack(Block.stoneSingleSlab, 2), new BlockStack(Block.stoneSingleSlab, 3), new BlockStack(Block.stoneSingleSlab, 1), new BlockStack(Block.stoneSingleSlab, 5), new BlockStack(Block.stoneSingleSlab, 4), new BlockStack(Block.stoneSingleSlab, 6), new BlockStack(Block.stoneSingleSlab, 7) };
		return array;
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		return getPower(par1iBlockAccess, par2, par3, par4);
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		return getPower(par1iBlockAccess, par2, par3, par4);
	}
	
	public int getPower(IBlockAccess par0, int x, int y, int z)
	{
		return 0;
	}
	
	

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z)
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return info.getPipeIcon();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		double[] box = getBoxes(par1World, par2, par3, par4);
		return AxisAlignedBB.getAABBPool().getAABB(par2 + box[0], par3 + box[1], par4 + box[2], par2 + box[3], par3 + box[4], par4 + box[5]);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		double[] box = getBoxes(par1World, par2, par3, par4);
		return AxisAlignedBB.getAABBPool().getAABB(par2 + box[0], par3 + box[1], par4 + box[2], par2 + box[3], par3 + box[4], par4 + box[5]);
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		double[] box = getBoxes(par1iBlockAccess, par2, par3, par4);
		
		setBlockBounds((float) box[0], (float) box[1], (float) box[2], (float) box[3], (float) box[4], (float) box[5]);
	}
	
	public double[] getBoxes(IBlockAccess world, int x, int y, int z)
	{
		double[] array = new double[6];
		
		double pipeMin = 0.25D;
		double pipeMax = 0.75D;
		
		ArrayList<ForgeDirection> valids = getPossibleSides(world, x, y, z);
		
		double minX = pipeMin;
		double minY = pipeMin;
		double minZ = pipeMin;
		
		double maxX = pipeMax;
		double maxY = pipeMax;
		double maxZ = pipeMax;
		
		if (valids.contains(ForgeDirection.UP))
		{
			maxY = 1.0D;
		}
		if (valids.contains(ForgeDirection.DOWN))
		{
			minY = 0.0D;
		}
		if (valids.contains(ForgeDirection.EAST))
		{
			maxX = 1.0F;
		}
		if (valids.contains(ForgeDirection.WEST))
		{
			minX = 0.0F;
		}
		if (valids.contains(ForgeDirection.NORTH))
		{
			minZ = 0.0F;
		}
		if (valids.contains(ForgeDirection.SOUTH))
		{
			maxZ = 1.0F;
		}
		
		array[0] = minX;
		array[1] = minY;
		array[2] = minZ;
		array[3] = maxX;
		array[4] = maxY;
		array[5] = maxZ;
		
		return array;
	}
	
	public ArrayList<ForgeDirection> getPossibleSides(IBlockAccess world, int x, int y, int z)
	{
		ArrayList<ForgeDirection> directions = new ArrayList<ForgeDirection>();
		for (int i = 0; i < 6; i++)
		{
			ForgeDirection cuDirection = ForgeDirection.getOrientation(i);
			if (isConnected(cuDirection, world, x, y, z))
			{
				directions.add(cuDirection);
			}
		}
		ForgeDirection direction = ForgeDirection.getOrientation(world.getBlockMetadata(x, y, z) % 6);
		if (isPossibleReciver(direction, world, x, y, z))
		{
			directions.add(direction);
		}
		return directions;
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
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World));
		notifyNeighbors(par1World, par2, par3, par4);
		
	}
	
	public boolean isRedstonePipe()
	{
		return false;
	}
	
	@Override
	public int tickRate(World par1World)
	{
		if (isRedstonePipe())
		{
			return 4;
		}
		return 1000;
	}
	
	@Override
	public void onBlockAdded(World par1World, int par2, int par3, int par4)
	{
		par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World));
	}
	
	public void notifyNeighbors(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, blockID);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, blockID);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, blockID);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		notifyNeighbors(world, i, j, k);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresRenderer(int meta)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultibleRenderPasses(int meta)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int meta)
	{
		return 0;
	}
	double min = 0.25;
	double max = 0.75;
	double[][] facings = new double[][] { { min, 0.0D, min, max, min, max }, { min, max, min, max, 1.0D, max }, { min, min, 0.0D, max, max, min }, { min, min, max, max, max, 1.0D }, { 0.0D, min, min, min, max, max }, { max, min, min, 1.0D, max, max } };

	
	@Override
	@SideOnly(Side.CLIENT)
	public void onRender(IBlockAccess world, int x, int y, int z, RenderBlocks render, BlockStack block, int renderPass)
	{
		ForgeDirection front = ForgeDirection.getOrientation(block.getMeta());
		
		render.setRenderBounds(min, min, min, max, max, max);
		render.renderStandardBlock(this, x, y, z);
		
		if (isConnected(ForgeDirection.UP, world, x, y, z))
		{
			render.setRenderBounds(min, max, min, max, 1.0D, max);
			render.renderStandardBlock(this, x, y, z);
		}
		if (isConnected(ForgeDirection.DOWN, world, x, y, z))
		{
			render.setRenderBounds(min, 0.0D, min, max, min, max);
			render.renderStandardBlock(this, x, y, z);
		}
		if (isConnected(ForgeDirection.SOUTH, world, x, y, z))
		{
			render.setRenderBounds(min, min, max, max, max, 1.0D);
			render.renderStandardBlock(this, x, y, z);
		}
		if (isConnected(ForgeDirection.NORTH, world, x, y, z))
		{
			render.setRenderBounds(min, min, 0.0D, max, max, min);
			render.renderStandardBlock(this, x, y, z);
		}
		if (isConnected(ForgeDirection.EAST, world, x, y, z))
		{
			render.setRenderBounds(max, min, min, 1.0D, max, max);
			render.renderStandardBlock(this, x, y, z);
		}
		if (isConnected(ForgeDirection.WEST, world, x, y, z))
		{
			render.setRenderBounds(0.0D, min, min, min, max, max);
			render.renderStandardBlock(this, x, y, z);
			
		}
		
		if (isPossibleReciver(front, world, x, y, z))
		{
			render.setOverrideBlockTexture(info.getDirectionIcon());
			double[] array = facings[front.ordinal()];
			render.setRenderBounds(array[0], array[1], array[2], array[3], array[4], array[5]);
			render.renderStandardBlock(this, x, y, z);
			render.clearOverrideBlockTexture();
		}
	}

	@Override
	public boolean dissableRendering(int meta)
	{
		return false;
	}

	@Override
	public boolean requiresRender()
	{
		return true;
	}

	@Override
	public boolean renderItemBlock(int meta)
	{
		return true;
	}

	@Override
	public boolean renderItemBlockBasic(int meta)
	{
		return false;
	}

	@Override
	public float[] getBoundingBoxes(int meta)
	{
		return new float[]{0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F};
	}

	@Override
	public float[] getXYZForItemRenderer(ItemRenderType type, int meta)
	{
		switch(type)
		{
			case ENTITY: return new float[]{-0.5F, -0.5F, -0.5F};
			case EQUIPPED: return new float[]{-0.4F, 0.50F, 0.35F};
			case EQUIPPED_FIRST_PERSON: return new float[]{-0.4F, 0.50F, 0.35F};
			case INVENTORY: return new float[]{-0.5F, -0.5F, -0.5F};
			default: return null;
			
		}
	}

	@Override
	public int getItemRenderPasses(int meta)
	{
		return 0;
	}

	@Override
	public void onItemRendering(BlockRendererHelper render, ItemRenderType type, BlockStack stack, int renderPass, float x, float y, float z, Object... data)
	{
		render.renderBlockStandart(this, stack, (RenderBlocks)data[1], type);
	}
	
}
