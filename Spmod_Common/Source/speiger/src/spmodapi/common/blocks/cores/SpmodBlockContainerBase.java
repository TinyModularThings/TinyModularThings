package speiger.src.spmodapi.common.blocks.cores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.tile.AdvTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodBlockContainerBase extends SpmodBlockBase implements ITileEntityProvider
{
	public static HashMap<BlockPosition, ArrayList<ItemStack>> tileDrops = new HashMap<BlockPosition, ArrayList<ItemStack>>();
	
	
	public SpmodBlockContainerBase(int par1, Material par2Material)
	{
		super(par1, par2Material);
		this.isBlockContainer = true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return null;
	}
	
    @Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5, ItemStack par6ItemStack)
	{
    	int facing = WorldReading.getLookingDirectionFromEnitty(par5);
    	AdvTile tile = getAdvTile(par1World, par2, par3, par4);
    	if(tile != null)
    	{
    		tile.onPlaced(facing);
    		if(par5 != null && par5 instanceof EntityPlayer)
    		{
    			tile.setupUser((EntityPlayer)par5);
    		}
    	}
	}
    
    
    
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		AdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			ArrayList<AxisAlignedBB> boxes = tile.getColidingBoxes(par7Entity);
			if(boxes != null && boxes.size() > 0)
			{
				for(AxisAlignedBB aabb : boxes)
				{
					if(par5AxisAlignedBB.intersectsWith(aabb))
					{
						par6List.add(aabb);
					}
				}
				return;
			}
		}
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		AdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			AxisAlignedBB aabb = tile.getSelectedBoxes();
			if(aabb != null)
			{
				return aabb;
			}
		}
		return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
	}
	
	

	@Override
	public float getBlockHardness(World world, int x, int y, int z)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getBlockHardness();
		}
		return super.getBlockHardness(world, x, y, z);
	}
	
	

	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getExplosionResistance(par1Entity);
		}
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5)
	{
		AdvTile tile = getAdvTile(par2World, par3, par4, par5);
		if(tile != null)
		{
			if(tile.removeAbleByPlayer(par1EntityPlayer))
			{
				return tile.getHardnessForPlayer(par1EntityPlayer);
			}
			else
			{
				return -1F;
			}
		}
		return super.getPlayerRelativeBlockHardness(par1EntityPlayer, par2World, par3, par4, par5);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getBlockLightLevel();
		}
		return super.getLightValue(world, x, y, z);
	}
	
	
	
	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getLightOpactiy();
		}
		return super.getLightOpacity(world, x, y, z);
	}
	
	@Override
	public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.shouldCheckWeakPower(side);
		}
		return super.shouldCheckWeakPower(world, x, y, z, side);
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.SolidOnSide(side);
		}
		return super.isBlockSolidOnSide(world, x, y, z, side);
	}
	
	
	
	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if(par5.isSneaking() && !ignoreRightClick[meta])
		{
			return false;
		}
		
		if(!par1.isRemote)
		{
			AdvTile tile = getAdvTile(par1, par2, par3, par4);
			if(tile != null)
			{
				if(tile.onClick(par5.isSneaking(), par5, this, par6))
				{
					return true;
				}
				
				if(tile.hasContainer())
				{
					return tile.onActivated(par5);
				}
			}
		}
		return true;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			ItemStack stack = tile.pickBlock(target);
			if(stack != null)
			{
				return stack;
			}
		}
		return super.getPickBlock(target, world, x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		AdvTile tile = getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			return tile.getColor().getHex();
		}
		return super.getBlockColor();
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canConnectToWire(side);
		}
		return super.canConnectRedstone(world, x, y, z, side);
	}
	
	

	@Override
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getFireSpreadSpeed(face);
		}
		return super.getFireSpreadSpeed(world, x, y, z, metadata, face);
	}
	
	
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getFireBurnLenght(face);
		}
		return super.getFlammability(world, x, y, z, metadata, face);
	}

	@Override
	public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isFireSource(side);
		}
		return super.isFireSource(world, x, y, z, metadata, side);
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z)
	{
		AdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isAir();
		}
		return super.isAirBlock(world, x, y, z);
	}
	
	

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
	{
		AdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			return tile.canPlacedOnSide(ForgeDirection.getOrientation(par5));
		}
		return super.canPlaceBlockOnSide(par1World, par2, par3, par4, par5);
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }
    
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	AdvTile tile = getAdvTile(par1World, par2, par3, par4);
    	int meta = par1World.getBlockMetadata(par2, par3, par4);
    	if(tile != null)
    	{
    		tile.onBreaking();
    		if(hasTileDrops(meta))
    		{
    			tileDrops.put(tile.getPosition(), tile.onDrop(0));
    		}
    	}
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
        par1World.removeBlockTileEntity(par2, par3, par4);
    }

    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        super.onBlockEventReceived(par1World, par2, par3, par4, par5, par6);
        TileEntity tileentity = par1World.getBlockTileEntity(par2, par3, par4);
        return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
    }

	@Override
	public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	{
		ArrayList<ItemStack> drops = super.getBlockDropped(world, x, y, z, metadata, fortune);
		BlockPosition pos = new BlockPosition(world, x, y, z);
		
		if(tileDrops.containsKey(pos))
		{
			drops.addAll(tileDrops.get(pos));
		}
		
		return drops;
	}
    
    public boolean hasTileDrops(int meta)
    {
    	return false;
    }
    
    public AdvTile getAdvTile(IBlockAccess par1, int x, int y, int z)
    {
    	TileEntity tile = par1.getBlockTileEntity(x, y, z);
    	if(tile != null && tile instanceof AdvTile)
    	{
    		return (AdvTile)tile;
    	}
    	return null;
    }
    
}
