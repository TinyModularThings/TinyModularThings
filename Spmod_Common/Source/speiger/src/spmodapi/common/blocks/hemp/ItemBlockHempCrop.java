package speiger.src.spmodapi.common.blocks.hemp;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;

public class ItemBlockHempCrop extends ItemBlockSpmod
{
	
	public ItemBlockHempCrop(int par1)
	{
		super(par1);
	}

	@Override
	public BlockStack getBlockToPlace(int meta)
	{
		return null;
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Hemp Crop";
	}
	
}
