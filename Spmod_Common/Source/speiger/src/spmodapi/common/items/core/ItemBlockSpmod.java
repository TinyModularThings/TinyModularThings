package speiger.src.spmodapi.common.items.core;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.api.common.utils.WorldReading;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.tile.AdvTile;
import speiger.src.spmodapi.common.util.TileIconMaker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ItemBlockSpmod extends ItemBlock
{

	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		BlockStack stack = getBlockToPlace(par1ItemStack.getItemDamage());
		if(stack != null)
		{
			AdvTile tile = TileIconMaker.getIconMaker().getTileEntityFormBlockAndMetadata(stack.getBlock(), stack.getMeta());
			if(tile != null)
			{
				tile.onItemInformation(par2EntityPlayer, par3List);
			}
		}
	}

	public ItemBlockSpmod(int par1)
	{
		super(par1);
	}
	
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
        int blockID = world.getBlockId(x, y, z);
        
        if (blockID == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            side = 1;
        }
        else if (blockID != Block.vine.blockID && blockID != Block.tallGrass.blockID && blockID != Block.deadBush.blockID && (Block.blocksList[blockID] == null || !Block.blocksList[blockID].isBlockReplaceable(world, x, y, z)))
        {
            if (side == 0)
            {
                --y;
            }

            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }
        }
        
        if (item.stackSize == 0)
        {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, item))
        {
            return false;
        }
        else if (y == 255 && Block.blocksList[this.getBlockID()].blockMaterial.isSolid())
        {
            return false;
        }
        else if(getBlockToPlace(item.getItemDamage()) != null)
        {
        	BlockStack stack = getBlockToPlace(item.getItemDamage());
        	if(world.setBlock(x, y, z, stack.getBlockID(), stack.getMeta(), 3))
        	{
        		WorldReading.setupUser(world, x, y, z, player);
        		WorldReading.setUpFacing(world, x, y, z, player, side);
        		boolean flag = onAfterPlaced(world, x, y, z, side, player, item);
        		stack.getBlock().onBlockPlacedBy(world, x, y, z, player, item);
        		if(flag)
        		{
        			removeItem(player, item);
        		}
        		return true;
        	}
        }
        
        return false;
    }
    
    public abstract BlockStack getBlockToPlace(int meta);
	
    public boolean onAfterPlaced(World world, int x, int y, int z, int side, EntityPlayer player, ItemStack item)
    {
    	return false;
    }
    
    public void removeItem(EntityPlayer par1, ItemStack par2)
    {
    	if(!par1.capabilities.isCreativeMode)
    	{
    		par2.stackSize--;
    	}
    }

	@Override
	public String getItemDisplayName(ItemStack par1)
	{
		return getName(par1);
	}
    
	public abstract String getName(ItemStack par1);
    
}
