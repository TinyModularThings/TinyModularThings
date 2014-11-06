package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockMultiMineOre extends ItemBlockTinyChest
{
	private static HashMap<Integer, String> names = new HashMap<Integer, String>();
	
	public ItemBlockMultiMineOre(int par1)
	{
		super(par1);
	}
	
	public static void addName(Block par0, String name)
	{
		if (name == null || par0 == null)
		{
			return;
		}
		names.put(par0.blockID, name);
		
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return new BlockStack(getBlockID(), meta);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return names.get(getBlockID());
	}
	
}
