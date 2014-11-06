package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockMachine extends ItemBlockTinyChest
{
	public ItemBlockMachine(int par1)
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
			case 0: return "Pressure Furnace";
			case 1: return "Bucket Filler";
			case 2: return "Selfpowered Bucket Filler";
			case 3: return "Water Generator";
			case 4: return "Oil Generator";
		}
		return null;
	}
	
}
