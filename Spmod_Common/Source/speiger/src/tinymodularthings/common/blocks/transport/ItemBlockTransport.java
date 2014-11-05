package speiger.src.tinymodularthings.common.blocks.transport;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockTransport extends ItemBlockTinyChest
{
	
	public ItemBlockTransport(int par1)
	{
		super(par1);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
}
