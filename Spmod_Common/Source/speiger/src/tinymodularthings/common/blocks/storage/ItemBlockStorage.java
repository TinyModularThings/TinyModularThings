package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockStorage extends ItemBlockTinyChest
{
	public ItemBlockStorage(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1 > 3 ? 3 : par1;
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
			case 0: return "TinyChest";
			case 1: return "TinyTank";
			case 2: return "Advanced TinyChest";
			case 3: return "Advanced TinyTank";
		}
		return null;
	}
	
}
