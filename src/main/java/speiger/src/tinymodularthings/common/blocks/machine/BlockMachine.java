package speiger.src.tinymodularthings.common.blocks.machine;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import speiger.src.tinymodularthings.common.plugins.BC.BCRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMachine extends BlockContainer
{
	
	public BlockMachine()
	{
		super(Material.iron);
		setHardness(4.0F);
		setHarvestLevel("pickaxe", 1, 0);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for(int i = 0;i<4;i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}

	
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile)tile).pickBlock(target);
		}
		return new ItemStack(world.getBlock(x, y, z), 1, world.getBlockMetadata(x, y, z));
	}


	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return this.createTileEntity(p_149915_1_, p_149915_2_);
	}
	
	@Override
	public TileEntity createTileEntity(World par1, int metadata)
	{
		try
		{
			switch (metadata)
			{
				case 0: return new PressureFurnace();
				case 1: return new BucketFillerBasic();
				case 2: return new SelfPoweredBucketFiller();
				case 3: return new WaterGenerator();
				default:
					return null;
			}
		}
		catch (Exception e)
		{
			TinyModularThings.log.print("Could not Load TileEntityMeta: " + metadata + " Reason: " + e.getLocalizedMessage());
			return null;
		}
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onBreaking();
		}
		
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (!par1.isRemote)
		{
			TileEntity tile = par1.getTileEntity(par2, par3, par4);
			if (tile instanceof AdvTile)
			{
				return ((AdvTile) tile).onActivated(par5EntityPlayer);
			}
		}
		return true;
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion)
	{
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onBreaking();
		}
	}
	
	@Override
	public float getBlockHardness(World par1, int par2, int par3, int par4)
	{
		TileEntity tile = par1.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).getBlockHardness();
		}
		
		return 4.0F;
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par1)
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
		
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onPlaced(direction);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1.getTileEntity(par2, par3, par4);
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile &&  ((AdvTile) tile).getIconFromSideAndMetadata(par5, 0) != null)
		{
			return ((AdvTile) tile).getIconFromSideAndMetadata(par5, 0);
		}
		return getIcon(par5, meta);
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		switch (par2)
		{
			case 0:
			{
				if (par1 == 3)
				{
					if (BCRegistry.overrideVanilla)
					{
						return Blocks.furnace.getIcon(3, 2);
					}
					else
					{
						return Blocks.furnace.getIcon(2, 2);
					}
				}
				return Blocks.cobblestone.getBlockTextureFromSide(0);
			}
			case 1:
			{
				if(par1 < 2)
				{
					return textures[0][par1];
				}
				return textures[0][2];
			}
			case 2:
			{
				if(par1 < 2)
				{
					return textures[1][par1];
				}
				return textures[1][2];
			}
			case 3:
			{
				if(par1 < 2)
				{
					if(par1 == 1)
					{
						par1 = 0;
					}
					else if(par1 == 0)
					{
						par1 = 1;
					}
					return textures[2][par1];
				}
				return textures[2][2];
			}
			default:
				return null;
		}
	}
	
	IIcon[][] textures = new IIcon[3][3];
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		String[] names = new String[]{"top", "bottom", "side"};
		for(int i = 0;i<names.length;i++)
		{
			textures[0][i] = par1IconRegister.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":machine/basicBucketFiller_"+names[i]);
			textures[1][i] = par1IconRegister.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":machine/SelfPoweredBucketFiller_"+names[i]);
			textures[2][i] = par1IconRegister.registerIcon(TinyModularThingsLib.ModID.toLowerCase()+":machine/waterGenerator_"+names[i]);
			
		}
	}
	
	

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		notifyNeighbors(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, this, tickRate());
	}
	
	public void notifyNeighbors(World world, int i, int j, int k)
	{
		world.notifyBlocksOfNeighborChange(i, j, k, this);
		world.notifyBlocksOfNeighborChange(i, j - 1, k, this);
		world.notifyBlocksOfNeighborChange(i, j + 1, k, this);
		world.notifyBlocksOfNeighborChange(i - 1, j, k, this);
		world.notifyBlocksOfNeighborChange(i + 1, j, k, this);
		world.notifyBlocksOfNeighborChange(i, j, k - 1, this);
		world.notifyBlocksOfNeighborChange(i, j, k + 1, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onClientTick();
		}
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int i, int j, int k, int l)
	{
		notifyNeighbors(world, i, j, k);
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		world.scheduleBlockUpdate(i, j, k, this, tickRate());
	}
	
	public int tickRate()
	{
		return 5;
	}
	
}
