package speiger.src.tinymodularthings.common.blocks.ores;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockSpmodOre extends ItemBlockTinyChest
{	
	public ItemBlockSpmodOre(int par1)
	{
		super(par1);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
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
			case 0: return "Copper Ore";
			case 1: return "Tin Ore";
			case 2: return "Silver Ore";
			case 3: return "Lead Ore";
			case 4: return "Bauxit Ore";
			case 5: return "Iridium Ore";
		}
		return null;
	}
}
