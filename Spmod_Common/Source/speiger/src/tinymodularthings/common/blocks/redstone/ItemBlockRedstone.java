package speiger.src.tinymodularthings.common.blocks.redstone;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockRedstone extends ItemBlockTinyChest
{
	
	public ItemBlockRedstone(int par1)
	{
		super(par1);
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}
	
	@Override
	public String getName(ItemStack par1)
	{
		switch(par1.getItemDamage())
		{
			case 0: return "Detector";
		}
		return null;
	}
	
}
