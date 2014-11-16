package speiger.src.api.common.data.utils;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;

public class BlockData implements IStackInfo
{
	Block block;
	int meta;
	
	public BlockData(BlockStack par1)
	{
		this(par1.getBlock(), par1.getMeta());
	}
	public BlockData(Block par1, int par2)
	{
		block = par1;
		meta = par2;
	}
	
	public BlockData(Block par1)
	{
		this(par1, 0);
	}
	
	@Override
	public boolean equals(Object arg0)
	{
		if(arg0 == null)
		{
			return false;
		}
		if(!(arg0 instanceof BlockData))
		{
			return false;
		}
		BlockData par1 = (BlockData)arg0;
		if(par1.block != block)
		{
			return false;
		}
		if(par1.meta != meta)
		{
			return false;
		}
		return true;
	}
	@Override
	public int hashCode()
	{
		return block.blockID + meta;
	}
	@Override
	public ItemStack getResult()
	{
		return new ItemStack(block, 1, meta);
	}
	
}
