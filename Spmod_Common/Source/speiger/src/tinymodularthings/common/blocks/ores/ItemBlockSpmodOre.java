package speiger.src.tinymodularthings.common.blocks.ores;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockSpmodOre extends ItemBlockTinyChest
{
	
	private String[] oreNames = new String[] { "oreCopper", "oreTin", "oreSilver", "oreLead", "oreBauxit", "oreIridium" };
	
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
	
}
