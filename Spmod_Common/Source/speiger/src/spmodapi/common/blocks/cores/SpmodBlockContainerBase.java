package speiger.src.spmodapi.common.blocks.cores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import speiger.src.api.common.world.blocks.BlockPosition;
import speiger.src.spmodapi.common.interfaces.IAdvTile;
import speiger.src.spmodapi.common.util.TextureEngine;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SpmodBlockContainerBase extends SpmodBlockBase implements ITileEntityProvider
{
	public static WeakHashMap<List<Integer>, DropEntry> tileDrops = new WeakHashMap<List<Integer>, DropEntry>();
	public ThreadLocal<EntityPlayer> breaker = new ThreadLocal<EntityPlayer>();
	
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
	public boolean isBlockNormalCube(World world, int x, int y, int z)
	{
		IAdvTile tile = this.getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isNormalCube();
		}
		return super.isBlockNormalCube(world, x, y, z);
	}
    
    

	@Override
	public boolean canBeReplacedByLeaves(World world, int x, int y, int z)
	{
		IAdvTile tile = this.getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canBeReplaced();
		}
		return super.canBeReplacedByLeaves(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = this.getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			return tile.shouldSideBeRendered(par5);
		}
		return super.shouldSideBeRendered(par1iBlockAccess, par2, par3, par4, par5);
	}
	
	

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onBlockChange(this, par5);
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess par1iBlockAccess, int par2, int par3, int par4)
	{
		IAdvTile tile = this.getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			tile.setBoundsOnState(this);
		}
	}

	@Override
	public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion)
	{
		IAdvTile tile = this.getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onDistroyedByExplosion(par5Explosion);
		}
		super.onBlockDestroyedByExplosion(par1World, par2, par3, par4, par5Explosion);
	}

	@Override
	public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		IAdvTile tile = this.getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onEntityWalk(par5Entity);
		}
		super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = this.getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			return tile.isIndirectlyPowering(par5);
		}
		return super.isProvidingWeakPower(par1iBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		IAdvTile tile = this.getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onColide(par5Entity);
		}
		super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = this.getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			return tile.isPowering(par5);
		}
		return super.isProvidingStrongPower(par1iBlockAccess, par2, par3, par4, par5);
	}

	@Override
	public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6)
	{
		IAdvTile tile = this.getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onFallen(par5Entity);
		}
		super.onFallenUpon(par1World, par2, par3, par4, par5Entity, par6);
	}

	@Override
	public boolean isBlockReplaceable(World world, int x, int y, int z)
	{
		IAdvTile tile = this.getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canBeReplaced();
		}
		return super.isBlockReplaceable(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1iBlockAccess, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = this.getAdvTile(par1iBlockAccess, par2, par3, par4);
		int meta = par1iBlockAccess.getBlockMetadata(par2, par3, par4);
		if(tile != null)
		{
			return TextureEngine.getTextures().getIconSafe(tile.getIconFromSideAndMetadata(par5, tile.getRenderPass()));
		}
		return getIcon(par5, meta);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta)
	{
		if(!this.hasTileEntity(meta))
		{
			return getTexture(TextureEngine.getTextures(), meta, side);
		}
		return TileIconMaker.getIconMaker().getIconFromTile(this, meta, side);
	}

	@Override
	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity)
	{
		IAdvTile tile = this.getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canEntityDistroyMe(entity);
		}
		return super.canEntityDestroy(world, x, y, z, entity);
	}
    
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
 		if(tile != null)
		{
 			AxisAlignedBB bb = tile.getColidingBox();
			if(bb != null)
			{
				return tile.getColidingBox();
			}
		}
		return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
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
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getBlockHardness();
		}
		return super.getBlockHardness(world, x, y, z);
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getExplosionResistance(par1Entity);
		}
		return super.getExplosionResistance(par1Entity, world, x, y, z, explosionX, explosionY, explosionZ);
	}

	@Override
	public float getPlayerRelativeBlockHardness(EntityPlayer par1EntityPlayer, World par2World, int par3, int par4, int par5)
	{
		IAdvTile tile = getAdvTile(par2World, par3, par4, par5);
		if(tile != null)
		{
			if(tile.removeAbleByPlayer(par1EntityPlayer))
			{
				float hardness = tile.getHardnessForPlayer(par1EntityPlayer);
				int meta = par2World.getBlockMetadata(par3, par4, par5);
				boolean canHarvest = ForgeHooks.canHarvestBlock(this, par1EntityPlayer, meta);
				if(!canHarvest)
				{
		            float speed = ForgeEventFactory.getBreakSpeed(par1EntityPlayer, this, meta, 1.0f);
		            return (speed < 0 ? 0 : speed) / hardness / 100F;
				}
				else
				{
					 return par1EntityPlayer.getCurrentPlayerStrVsBlock(this, false, meta) / hardness / 30F;
				}
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
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getBlockLightLevel();
		}
		return super.getLightValue(world, x, y, z);
	}
	
	@Override
	public int getLightOpacity(World world, int x, int y, int z)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getLightOpactiy();
		}
		return super.getLightOpacity(world, x, y, z);
	}
	
	@Override
	public boolean shouldCheckWeakPower(World world, int x, int y, int z, int side)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.shouldCheckWeakPower(side);
		}
		return super.shouldCheckWeakPower(world, x, y, z, side);
	}
	
	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.SolidOnSide(side);
		}
		return super.isBlockSolidOnSide(world, x, y, z, side);
	}
	
	
	
	@Override
	public void onBlockClicked(World par1, int par2, int par3, int par4, EntityPlayer par5)
	{
		super.onBlockClicked(par1, par2, par3, par4, par5);
		if(!par1.isRemote)
		{
			IAdvTile tile = getAdvTile(par1, par2, par3, par4);
			if(tile != null)
			{
				tile.onLeftClick(par5);
			}
		}
	}

	@Override
	public boolean onBlockActivated(World par1, int par2, int par3, int par4, EntityPlayer par5, int par6, float par7, float par8, float par9)
	{
		int meta = par1.getBlockMetadata(par2, par3, par4);
		if(par5.isSneaking() && !ignoreRightClick[meta])
		{
			return false;
		}
		
		IAdvTile tile = getAdvTile(par1, par2, par3, par4);
		if(tile != null)
		{
			if(tile.onClick(par5.isSneaking(), par5, this, par6))
			{
				return true;
			}
			if(tile.hasContainer())
			{
				return tile.onOpened(par5, par6);
			}
		}
		
		return super.onBlockActivated(par1, par2, par3, par4, par5, par6, par7, par8, par9);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
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
		IAdvTile tile = getAdvTile(par1iBlockAccess, par2, par3, par4);
		if(tile != null)
		{
			return tile.getColor().getHex();
		}
		return super.getBlockColor();
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canConnectToWire(side);
		}
		return super.canConnectRedstone(world, x, y, z, side);
	}
	
	@Override
	public int getFireSpreadSpeed(World world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getFireSpreadSpeed(face);
		}
		return super.getFireSpreadSpeed(world, x, y, z, metadata, face);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, int x, int y, int z, int metadata, ForgeDirection face)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.getFireBurnLenght(face);
		}
		return super.getFlammability(world, x, y, z, metadata, face);
	}

	@Override
	public boolean isFireSource(World world, int x, int y, int z, int metadata, ForgeDirection side)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isFireSource(side);
		}
		return super.isFireSource(world, x, y, z, metadata, side);
	}
	
	@Override
	public boolean isAirBlock(World world, int x, int y, int z)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isAir();
		}
		return super.isAirBlock(world, x, y, z);
	}

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			return tile.canPlacedOnSide(ForgeDirection.getOrientation(par5));
		}
		return super.canPlaceBlockOnSide(par1World, par2, par3, par4, par5);
	}
	
	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.canMonsterSpawn(type);
		}
		return super.canCreatureSpawn(type, world, x, y, z);
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata)
	{
		IAdvTile tile = getAdvTile(world, x, y, z);
		if(tile != null)
		{
			return tile.isSilkHarvestable(player);
		}
		return super.canSilkHarvest(world, player, x, y, z, metadata);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
	{
		IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
		if(tile != null)
		{
			tile.onClientTick();
		}
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }
    
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	IAdvTile tile = getAdvTile(par1World, par2, par3, par4);
    	int meta = par1World.getBlockMetadata(par2, par3, par4);
    	if(tile != null)
    	{
    		tile.onBreaking();
    		if(hasTileDrops(meta))
    		{
    			int fortune = 0;
    			EntityPlayer player = breaker.get();
    			if(player != null)
    			{
    				fortune = EnchantmentHelper.getFortuneModifier(player);
    			}
    			tileDrops.put(tile.getPosition().getAsList(), new DropEntry(tile.getItemDrops(fortune), par1World.getTotalWorldTime()));
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
		
		if(tileDrops.containsKey(pos.getAsList()))
		{
			drops.addAll(tileDrops.get(pos.getAsList()).getDrops());
		}
		else
		{
			IAdvTile tile = getAdvTile(pos.getWorld(), pos.getXCoord(), pos.getYCoord(), pos.getZCoord());
			if(tile != null)
			{
				drops.addAll(tile.getItemDrops(fortune));
			}
		}
		return drops;
	}
    
    public boolean hasTileDrops(int meta)
    {
    	return false;
    }
    
    public IAdvTile getAdvTile(IBlockAccess par1, int x, int y, int z)
    {
    	TileEntity tile = par1.getBlockTileEntity(x, y, z);
    	if(tile != null && tile instanceof IAdvTile)
    	{
    		return (IAdvTile)tile;
    	}
    	return null;
    }
    
    public Icon getTexture(TextureEngine par1, int meta, int side)
    {
    	return par1.getIconSafe();
    }
    
    public static class DropEntry
    {
    	final List<ItemStack> drops;
    	final long timeAdded;
    	public DropEntry(List<ItemStack> par1, long par2)
		{
    		drops = par1;
    		timeAdded = par2;
		}
    	
    	public List<ItemStack> getDrops()
		{
			return drops;
		}
    	
    	public long getTimeAdded()
		{
			return timeAdded;
		}
    }
    
}
