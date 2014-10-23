package speiger.src.tinymodularthings.common.blocks.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.enums.EnumIDs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStorage extends SpmodBlockContainerBase
{
	
	public BlockStorage(int par1)
	{
		super(par1, Material.iron);
		setHardness(4.0F);
		setCreativeTab(CreativeTabs.tabFood);
		MinecraftForge.setBlockHarvestLevel(this, 0, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 0, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 1, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 2, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 2, "axe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 3, "pickaxe", 0);
		MinecraftForge.setBlockHarvestLevel(this, 3, "axe", 0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		
	}
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}
	
	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return 0;
	}
	
	@Override
	public int damageDropped(int par1)
	{
		return 0;
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		ItemStack stack = new ItemStack(world.getBlockId(x, y, z), 1, world.getBlockMetadata(x, y, z));
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			stack = ((AdvTile) tile).pickBlock(target);
		}
		return stack;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6)
	{
		int direction = 2;
		int facing = MathHelper.floor_double(par5.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		
		if (facing == 0)
		{
			direction = ForgeDirection.NORTH.ordinal();
		}
		else if (facing == 1)
		{
			direction = ForgeDirection.EAST.ordinal();
		}
		else if (facing == 2)
		{
			direction = ForgeDirection.SOUTH.ordinal();
		}
		else if (facing == 3)
		{
			direction = ForgeDirection.WEST.ordinal();
		}
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onPlaced(direction);
		}
		
	}
	
	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		try
		{
			switch (metadata)
			{
				case 0:
					return new TinyChest();
				case 1:
					return new TinyTank();
				case 2:
					return new AdvTinyChest();
				case 3:
					return new AdvTinyTank();
				default:
					return null;
			}
		}
		catch (Exception e)
		{
			TinyModularThings.log.print("Could not Load TileEntity for meta: " + metadata + " Please call the ModAuthor to let it fix");
			return null;
		}
	}
	
	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		if (!par1.isRemote)
		{
			
			TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
			if (par5.isSneaking())
			{
				if (tile != null && tile instanceof AdvTile)
				{
					if (((AdvTile) tile).onClick(true, par5, this, par6))
					{
						return true;
					}
					return false;
				}
			}
			
			if (tile != null && tile instanceof AdvTile)
			{
				if (((AdvTile) tile).onClick(false, par5, this, par6))
				{
					return true;
				}
				
				return ((AdvTile) tile).onActivated(par5);
			}
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).getIconFromSideAndMetadata(par5, 0);
		}
		return null;
	}
	
	@Override
	public int getRenderType()
	{
		return EnumIDs.StorageBlock.getId();
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
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile && ((AdvTile) tile).getBlockLightLevel() > 0)
		{
			return ((AdvTile) tile).getBlockLightLevel();
		}
		return 0;
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onBreaking();
			for (ItemStack cu : getBlockDropped(par1World, par2, par3, par4, par6, 0))
			{
				dropBlockAsItem_do(par1World, par2, par3, par4, cu);
			}
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			drops.addAll(((AdvTile) tile).onDrop(fortune));
		}
		return drops;
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).isIndirectlyPowering(par5);
		}
		
		return 0;
	}
	
	@Override
	public boolean canProvidePower()
	{
		return true;
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).isPowering(par5);
		}
		return 0;
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).canConnectToWire();
		}
		return false;
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		if (side == side.DOWN)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).shouldCheckWeakPower();
		}
		return false;
	}
	
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		notifyNeighbors(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
	}
	
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		notifyNeighbors(world, i, j, k);
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
	
	public void onBlockAdded(World world, int i, int j, int k)
	{
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
	}
	
	@Override
	public int tickRate(World par1World)
	{
		return 5;
	}
	
}
