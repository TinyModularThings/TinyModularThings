package speiger.src.spmodapi.common.blocks.hemp;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.lib.SpmodAPILib;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockHempStraw extends Block
{
	public float[] size = new float[] { 1.0F, 0.9F, 0.8F, 0.7F, 0.6F, 0.5F, 0.4F, 0.3F, 0.2F, 0.1F };
	
	public BlockHempStraw()
	{
		super(Material.plants);
		setHardness(4.0F);
		this.setCreativeTab(APIUtils.tabHemp);
		setTickRandomly(true);
		setHarvestLevel("axe", 0);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		par1World.notifyBlockChange(par2, par3, par4, this);
	}
	
	@Override
	public int tickRate(World world)
	{
		return 5;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		blockIcon = par1IconRegister.registerIcon(SpmodAPILib.ModID.toLowerCase() + ":hemp/HempStraw");
	}
	
	@Override
	public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		if (!par1World.isRemote)
		{
			List<EntityAnimal> list = par1World.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(par2, par3, par4, par2 + 1, par3 + 1, par4 + 1).expand(10, 5, 10));
			for (int i = 0; i < list.size(); i++)
			{
				EntityAnimal current = list.get(i);
				
				if (!current.isInLove() && current.getAge() == 0 && !current.isChild())
				{
					current.getNavigator().tryMoveToXYZ(par2, par3, par4, 1D);
				}
			}
			
		}
		
		breedAnimal(par1World, par2, par3, par4, par5Random);
		par1World.notifyBlockOfNeighborChange(par2, par3, par4, this);
		par1World.scheduleBlockUpdate(par2, par3, par4, this, tickRate(par1World));
	}
	
	@Override
	public void onBlockAdded(World world, int i, int j, int k)
	{
		world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		int i = par1World.getBlockMetadata(par2, par3, par4);
		return AxisAlignedBB.getBoundingBox(par2, par3, par4, par2 + 1.0F, par3 + size[i], par4 + 1.0F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		int i = par1World.getBlockMetadata(par2, par3, par4);
		return AxisAlignedBB.getBoundingBox(par2, par3, par4, par2 + 1.0F, par3 + size[i], par4 + 1.0F);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		int i = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, size[i], 1.0F);
	}
	
	@Override
	public void setBlockBoundsForItemRender()
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public void breedAnimal(World par1, int par2, int par3, int par4, Random par5)
	{
		List<EntityAnimal> list = par1.getEntitiesWithinAABB(EntityAnimal.class, AxisAlignedBB.getBoundingBox(par2, par3, par4, par2 + 1, par3 + 1, par4 + 1).expand(1, 1, 1));
		if (!list.isEmpty())
		{
			EntityAnimal current = list.get(par5.nextInt(list.size()));
			if (feedAnimal(current))
			{
				damageBlock(par1, par2, par3, par4);
				par1.markBlockForUpdate(par2, par3, par4);
				par1.markBlockRangeForRenderUpdate(par2 - 1, par3 - 1, par4 - 1, par2 + 1, par3 + 1, par4 + 1);
			}
		}
	}
	
	public boolean feedAnimal(EntityAnimal var1)
	{
		Random rand = new Random();
		if (var1 == null)
		{
			return false;
		}
		else
		{
			if (var1.getGrowingAge() == 0 && !var1.isInLove())
			{
				var1.func_146082_f(null);
				var1.setTarget((Entity) null);
				
				for (int var3 = 0; var3 < 7; ++var3)
				{
					double var4 = rand.nextGaussian() * 0.02D;
					double var6 = rand.nextGaussian() * 0.02D;
					double var8 = rand.nextGaussian() * 0.02D;
					var1.worldObj.spawnParticle("heart", var1.posX + rand.nextFloat() * var1.width * 2.0F - var1.width, var1.posY + 0.5D + rand.nextFloat() * var1.height, var1.posZ + rand.nextFloat() * var1.width * 2.0F - var1.width, var4, var6, var8);
				}
				return true;
				
			}
			else
			{
				return false;
			}
		}
	}
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		if (meta == 0)
		{
			dropBlockAsItem(par1World, par2, par3, par4, new ItemStack(this));
		}
		
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}
	
	@Override
	public int quantityDropped(Random par1Random)
	{
		return 0;
	}
	
	public void damageBlock(World par1, int x, int y, int z)
	{
		Block block = par1.getBlock(x, y, z);
		int meta = par1.getBlockMetadata(x, y, z);
		if (block == this)
		{
			if (meta >= 9)
			{
				par1.setBlock(x, y, z, Blocks.air);
			}
			else
			{
				par1.setBlockMetadataWithNotify(x, y, z, meta + 1, 3);
			}
		}
	}
	
}
