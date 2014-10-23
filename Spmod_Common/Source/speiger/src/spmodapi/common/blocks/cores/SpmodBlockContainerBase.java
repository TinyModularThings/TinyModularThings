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
import net.minecraft.world.World;
import speiger.src.api.blocks.BlockPosition;
import speiger.src.api.util.WorldReading;
import speiger.src.spmodapi.common.tile.AdvTile;

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
    	
    	TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
    	if(tile != null && tile instanceof AdvTile)
    	{
    		AdvTile adv = (AdvTile)tile;
    		adv.onPlaced(facing);
    		if(par5 != null && par5 instanceof EntityPlayer)
    		{
    			adv.setupUser((EntityPlayer)par5);
    		}
    	}
	}
    
    
    
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		// TODO Auto-generated method stub
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		if(tile != null && tile instanceof AdvTile)
		{
			ItemStack stack = ((AdvTile)tile).pickBlock(target);
			if(stack != null)
			{
				return stack;
			}
		}
		return super.getPickBlock(target, world, x, y, z);
	}

	public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
    }
    
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
    	TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
    	int meta = par1World.getBlockMetadata(par2, par3, par4);
    	if(tile != null && tile instanceof AdvTile)
    	{
    		AdvTile adv = (AdvTile)tile;
    		adv.onBreaking();
    		if(hasTileDrops(meta))
    		{
    			tileDrops.put(adv.getPosition(), adv.onDrop(0));
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
    
}
