package speiger.src.spmodapi.common.modHelper.forestry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
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
		if (germling != null && germling.getItem() == APIItems.hempSeed)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isSaplingAt(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == APIBlocks.hempCrop && world.getBlockMetadata(x, y, z) >= 7)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public ICrop getCropAt(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == APIBlocks.hempCrop && world.getBlockMetadata(x, y, z) >= 7)
		{
			Block block = world.getBlock(x, y, z);
			if (block != null && block instanceof BlockHempCrop)
			{
				BlockHempCrop end = (BlockHempCrop) block;
				world.setBlock(x, y, z, Blocks.air);
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
			if (itemstack.getItem() == APIItems.hempSeed)
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean plantSaplingAt(ItemStack germling, World world, int x, int y, int z)
	{
		if (germling.getItem() == APIItems.hempSeed)
		{
			if (world.setBlock(x, y, z, APIBlocks.hempCrop))
			{
				return true;
			}
		}
		return false;
	}
	
}
