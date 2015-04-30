package speiger.src.spmodapi.common.blocks.gas;

import forestry.core.utils.StackUtils;
import ic2.api.item.Items;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import speiger.src.api.common.utils.MathUtils;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockBase;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;

public class BlockAnimalGas extends SpmodBlockBase implements IFluidBlock
{

	public BlockAnimalGas(int par1)
	{
		super(par1, Material.air);
		this.dissableDrops();
		this.setIsAbnormal();
		APIUtils.animalGas.setBlockID(par1);
		this.setHardness(100F);
		this.setLightOpacity(3);
		this.setResistance(10000.0F);
	}

	@Override
	public Fluid getFluid()
	{
		return APIUtils.animalGas;
	}
	
	

	@Override
	public boolean isBlockSolid(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		return false;
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(doDrain)
		{
			world.setBlockToAir(x, y, z);
		}
		return new FluidStack(getFluid(), (meta*100));
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z)
	{
		return true;
	}
	
	//EntityColide
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		if(!par1World.isRemote && par5Entity != null && par5Entity instanceof EntityLivingBase)
		{
			EntityLivingBase base = (EntityLivingBase)par5Entity;
			int oldTime = 0;
			if(par5Entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)par5Entity;
				if(!isAirSave(player, player.getCurrentArmor(3)))
				{
					PotionEffect ef = player.getActivePotionEffect(Potion.confusion);
					if(ef != null)
					{
						oldTime = ef.getDuration();
					}
				}
				else
				{
					return;
				}
			}
			else
			{
				PotionEffect ef = base.getActivePotionEffect(Potion.poison);
				if(ef != null)
				{
					oldTime += ef.getDuration();
				}
			}
			if(par1World.rand.nextBoolean())
			{
				base.addPotionEffect(new PotionEffect(Potion.confusion.getId(), oldTime+10, 3));
			}
		}
	}
	
	public boolean isAirSave(EntityPlayer player, ItemStack par1)
	{
		if(par1 != null)
		{
			try
			{
				ItemStack stack = Items.getItem("hazmatHelmet");
				ItemStack airCell = Items.getItem("airCell");
				ItemStack emptyCell = Items.getItem("cell");
				if(stack != null && airCell != null && emptyCell != null && par1.itemID == stack.itemID)
				{
					if(player.inventory.hasItemStack(airCell))
					{
						if(player.worldObj.rand.nextInt(150) == 0)
						{
							StackUtils.consumeItem(airCell);
							player.inventory.addItemStackToInventory(airCell);
							player.inventoryContainer.detectAndSendChanges();
						}
						return true;
					}
				}
			}
			catch(Exception e)
			{
			}
		}
		return false;
	}
	
	//Bounding Boxes
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
	    return null;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		setBlockBounds(0F, (1F - (0.1F*meta)), 0F, 1F, 1F, 1F);
	}

	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		super.updateTick(par1World, par2, par3, par4, par5Random);
		this.notifyNeighbors(par1World, par2, par3, par4);
		if(!par1World.isRemote)
		{
			onFlow(par1World, par2, par3, par4);
		}
		par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World));
	}

	public void onBlockAdded(World world, int i, int j, int k)
    {        
        world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
    }
	
	//This thing is Simply add TickDelay so it happen not instantly
	@Override
    public boolean func_82506_l()
    {
        return false;
    }
	
	@Override
	public int tickRate(World par0)
	{
		return 5;
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
	public void onNeighborBlockChange(World world, int x, int y, int z, int par5)
	{
		world.scheduleBlockUpdate(x, y, z, blockID, tickRate(world));
		super.onNeighborBlockChange(world, x, y, z, par5);
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean canCollideCheck(int par1, boolean par2)
	{
		return false;
	}

	
	//Animal Gas Functions

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(APIItems.gasBucket);
	}

	public void onFlow(World world, int x, int y, int z)
	{
		Random rand = world.rand;
		int meta = world.getBlockMetadata(x, y, z);
		FlowingState up = canFlowUp(world, x, y, z);
		if(meta == 1 && up == FlowingState.Nothing)
		{
			if(rand.nextInt(5) == 0)
			{
				for(int i = 2;i<6;i++)
				{
					if(rand.nextInt(5)+2 == i)
					{
						ForgeDirection dir = ForgeDirection.getOrientation(i);
						if(world.isAirBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))
						{
							WorldReading.setBlockToSide(world, x, y, z, i, this.blockID, world.getBlockMetadata(x, y, z), 3);
							world.setBlockToAir(x, y, z);
							return;
						}
					}
				}
			}
		}
		if(rand.nextInt(5) == 0)
		{
			Type[] types = BiomeDictionary.getTypesForBiome(world.getBiomeGenForCoords(x, z));
			for(Type type : types)
			{
				if(type == Type.END)
				{
					world.setBlockToAir(x, y, z);
					return;
				}
				else if(type == Type.DESERT)
				{
					if(y >= 150)
					{
						world.setBlockToAir(x, y, z);
						return;
					}
				}
				else if(type == Type.NETHER)
				{
					world.setBlockToAir(x, y, z);
					world.newExplosion(null, x, y, z, 5-(4/meta), true, true);
					return;
				}
			}
		}
		if(y >= 150)
		{
			if(world.getRainStrength(1.0F) > 0.2D)
			{
				world.setBlockToAir(x, y, z);
				return;
			}
			else if(rand.nextInt(150) == 0)
			{
				world.setBlockToAir(x, y, z);
				return;
			}

			if(canFlowToSide(world, x, y, z))
			{
				flowToSide(world, x, y, z);
			}
			return;
		}
		
		if(up == FlowingState.Full || up == FlowingState.Partly)
		{
			flowUp(world, x, y, z, up);
		}
		else
		{
			boolean flag = canFlowToSide(world, x, y, z);
			if(flag)
			{
				flowToSide(world, x, y, z);
			}
		}
	}
	
	private void flowUp(World world, int x, int y, int z, FlowingState state)
	{
		if(state == FlowingState.Full)
		{
			world.setBlock(x, y+1, z, this.blockID, world.getBlockMetadata(x,y,z), 3);
			world.setBlock(x, y, z, 0, 0, 3);
		}
		else
		{
			int metaUp = world.getBlockMetadata(x, y+1, z);
			int meta = world.getBlockMetadata(x, y, z);
			int[] result = MathUtils.getAdjustedMeta(metaUp, meta, 10);
			if(result[1] == 0)world.setBlockToAir(x, y, z);
			else world.setBlockMetadataWithNotify(x, y, z, result[1], 3);
			if(result[0] == 0)world.setBlockToAir(x, y, z);
			else world.setBlockMetadataWithNotify(x, y+1, z, result[0], 3);
			if(result[0] > 0)
			{
				flowToSide(world, x, y, z);
			}
		}
	}
	
	private void flowToSide(World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		SideInfo[] sides = getValidSides(world, x, y, z);
		for(int side = 0;side<sides.length;side++)
		{
			SideInfo info = sides[side];
			int i = info.getSide();
			if(world.rand.nextInt(5)+2 == i)
			{
				if(info.getBlockType() == GasBlockInfo.GasBlock)
				{
					int sideMeta = WorldReading.getBlockMeta(world, x, y, z, i);
					int[] result = MathUtils.getEqualMeta(sideMeta, meta);
					if(result[1] == 0) world.setBlockToAir(x, y, z);
					else world.setBlockMetadataWithNotify(x, y, z, result[1], 3);
					if(result[0] == 0) ;
					else WorldReading.setBlockMetaData(world, x, y, z, i, result[0], 3);
				}
				else
				{
					int sideMeta = WorldReading.getBlockMeta(world, x, y, z, i);
					int[] result = MathUtils.getEqualMeta(sideMeta, meta);
					if(result[1] == 0)world.setBlockToAir(x, y, z);
					else world.setBlockMetadataWithNotify(x, y, z, result[1], 3);
					if(result[0] == 0) ;
					else WorldReading.setBlockToSide(world, x, y, z, i, this.blockID, result[0], 3);
				}
				break;
			}
		}
	}
	
	private SideInfo[] getValidSides(World world, int x, int y, int z)
	{
		ArrayList<SideInfo> ints = new ArrayList<SideInfo>();
		int meta = world.getBlockMetadata(x, y, z);
		for(int i = 2;i<6;i++)
		{
			int id = WorldReading.getBlockId(world, x, y, z, i);
			
			if(id > 0 && id != this.blockID && (Block.blocksList[id].isAirBlock(world, x, y, z) || Block.blocksList[id].isBlockReplaceable(world, x, y, z)))
			{
				id = 0;
			}
			
			if(id == 0)
			{
				ints.add(new SideInfo(GasBlockInfo.EmptyBlock, i));
			}
			else if(id == this.blockID)
			{
				int sideMeta = WorldReading.getBlockMeta(world, x, y, z, i);
				if(sideMeta+1 < meta)
				{
					ints.add(new SideInfo(GasBlockInfo.GasBlock, i));
				}
			}
		}
		if(ints.isEmpty() || ints.size() == 0)
		{
			return new SideInfo[0];
		}
		return ints.toArray(new SideInfo[0]);
	}
	
	private boolean canFlowToSide(World world, int x, int y, int z)
	{
		boolean flag = true;
		int meta = world.getBlockMetadata(x, y, z);
		for(int i = 2;i<6;i++)
		{
			int id = WorldReading.getBlockId(world, x, y, z, i);
			if(id == 0)
			{
				flag = false;
				break;
			}
			else if(id == this.blockID)
			{
				int sideMeta = WorldReading.getBlockMeta(world, x, y, z, i);
				if(sideMeta < 10 && sideMeta+1 < meta)
				{
					flag = false;
					break;
				}
			}
		}
		if(flag)
		{
			return false;
		}
		return true;
	}
	
	private FlowingState canFlowUp(World world, int x, int y, int z)
	{
		if(y >= 150)
		{
			return FlowingState.Nothing;
		}
		int blockID = world.getBlockId(x, y+1, z);
		int meta = world.getBlockMetadata(x, y+1, z);
		if(blockID <= 0)
		{
			return FlowingState.Full;
		}
		else
		{
			if(blockID == this.blockID && meta < 10)
			{
				return FlowingState.Partly;
			}
		}
		return FlowingState.Nothing;
	}
	
	
	public static enum FlowingState
	{
		Full,
		Partly,
		Nothing;
	}
	
	public static enum GasBlockInfo
	{
		EmptyBlock,
		GasBlock;
	}
	
	public static class SideInfo
	{
		int side;
		GasBlockInfo info;
		
		public SideInfo(GasBlockInfo par1, int par2)
		{
			info = par1;
			side = par2;
		}
		
		public GasBlockInfo getBlockType()
		{
			return info;
		}
		
		public int getSide()
		{
			return side;
		}
	}
}
