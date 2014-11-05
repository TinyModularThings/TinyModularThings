package speiger.src.tinymodularthings.common.blocks.storage;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;
import speiger.src.tinymodularthings.common.lib.TinyModularThingsLib;

public class ItemBlockStorage extends ItemBlockTinyChest
{
	String[] names = new String[] { "tinychest", "tinytank", "advtinychest", "advTinyTank" };
	
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
	
}
