package speiger.src.tinymodularthings.common.blocks.ores;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockMultiMineOre extends ItemBlockTinyChest
{
	private static HashMap<Block, String> names = new HashMap<Block, String>();
	
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
		names.put(par0, name);
		
	}
	
}
