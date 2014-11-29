package speiger.src.spmodapi.common.blocks.gas;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;

public class ItemBlockAnimalUtils extends ItemBlockSpmod
{
	
	public ItemBlockAnimalUtils(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
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
			case 0: return "Basic AnimalChunkLoader";
		}
		return null;
	}
	
}
