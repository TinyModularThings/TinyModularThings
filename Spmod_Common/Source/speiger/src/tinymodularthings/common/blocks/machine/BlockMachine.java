package speiger.src.tinymodularthings.common.blocks.machine;

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
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import speiger.src.api.blocks.BlockStack;
import speiger.src.spmodapi.common.blocks.cores.SpmodBlockContainerBase;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMachine extends SpmodBlockContainerBase
{
	
	public BlockMachine(int par1)
	{
		super(par1, Material.iron);
		setHardness(4.0F);
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 1);
		setCreativeTab(CreativeTabs.tabFood);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3)
	{
		for (int i = 0; i < 5; i++)
		{
			par3.add(new ItemStack(par1, 1, i));
		}
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile) tile).pickBlock(target);
		}
		return new ItemStack(world.getBlockId(x, y, z), 1, world.getBlockMetadata(x, y, z));
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return null;
	}
	
	@Override
	public TileEntity createTileEntity(World par1, int metadata)
	{
		try
		{
			switch (metadata)
			{
				case 0:
					return new PressureFurnace();
				case 1:
					return new BucketFillerBasic();
				case 2:
					return new SelfPoweredBucketFiller();
				case 3:
					return new WaterGenerator();
				case 4:
					return new OilGenerator();
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
	public void registerTextures(TextureEngine par1)
	{
		par1.removePath();
		par1.finishMod();
		par1.registerTexture(this, "cobblestone", "furnace_front_off", "furnace_front_on");
		par1.setCurrentMod(TinyModularThingsLib.ModID.toLowerCase());
		par1.setCurrentPath("machine");
		par1.registerTexture(new BlockStack(this, 1), "basicBucketFiller_top", "basicBucketFiller_bottom", "basicBucketFiller_side");
		par1.registerTexture(new BlockStack(this, 2), "SelfPoweredBucketFiller_top", "SelfPoweredBucketFiller_bottom", "SelfPoweredBucketFiller_side");
		par1.registerTexture(new BlockStack(this, 3), "waterGenerator_top", "waterGenerator_bottom", "waterGenerator_side");
	
	}
	
	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
	{
		if (!par1.isRemote)
		{
			TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
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
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onBreaking();
		}
	}
	
	@Override
	public float getBlockHardness(World par1, int par2, int par3, int par4)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
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
		
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			((AdvTile) tile).onPlaced(direction);
		}
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1, int par2, int par3, int par4, int par5)
	{
		TileEntity tile = par1.getBlockTileEntity(par2, par3, par4);
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if (tile != null && tile instanceof AdvTile)
		{
			return ((AdvTile)tile).getIconFromSideAndMetadata(par5, 0);
		}
		return TextureEngine.getTextures().getIconSafe();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		return TileIconMaker.getIconMaker().getIconFromTile(this, par2, par1);
	}
	
	
	
	

	@Override
	public void updateTick(World world, int i, int j, int k, Random random)
	{
		notifyNeighbors(world, i, j, k);
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
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
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
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
		world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
	}
	
	public int tickRate()
	{
		return 5;
	}
	
}
