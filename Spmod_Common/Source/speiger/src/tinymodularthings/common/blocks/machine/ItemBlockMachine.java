package speiger.src.tinymodularthings.common.blocks.machine;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.TinyModularThings;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockMachine extends ItemBlockTinyChest
{
	
	String[] names = new String[] { "pressureFurnace", "basicBucketFiller", "selfpoweredBucketfiller", "water.generator", "Oil Generator"};
	
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
	
}
