package speiger.src.spmodapi.common.modHelper.forestry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import speiger.src.spmodapi.common.blocks.hemp.BlockHempCrop;
import speiger.src.spmodapi.common.config.ModObjects.APIBlocks;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;

public class Hanfaddon implements IFarmable
{
	
	@Override
	public boolean isGermling(ItemStack germling)
	{
		if (germling != null && germling.itemID == APIItems.hempSeed.itemID)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isSaplingAt(World world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z) == APIBlocks.hempCrop.blockID && world.getBlockMetadata(x, y, z) >= 7)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public ICrop getCropAt(World world, int x, int y, int z)
	{
		if (world.getBlockId(x, y, z) == APIBlocks.hempCrop.blockID && world.getBlockMetadata(x, y, z) >= 7)
		{
			Block block = Block.blocksList[world.getBlockId(x, y, z)];
			if (block != null && block instanceof BlockHempCrop)
			{
				BlockHempCrop end = (BlockHempCrop) block;
				world.setBlock(x, y, z, 0);
				return end;
			}
		}
		return null;
	}
	
	@Override
	public boolean isWindfall(ItemStack itemstack)
	{
		if (itemstack != null)
		{
			if (itemstack.itemID == APIItems.hempSeed.itemID)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean plantSaplingAt(ItemStack germling, World world, int x, int y, int z)
	{
		if (germling.itemID == APIItems.hempSeed.itemID)
		{
			if (world.setBlock(x, y, z, APIBlocks.hempCrop.blockID))
			{
				return true;
			}
		}
		return false;
	}
	
}
