package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;

public class ItemBlockFlower extends ItemBlockSpmod
{
	
	public ItemBlockFlower(int par1)
	{
		super(par1);
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
		return "Indigo Dye";
	}
	
	
	
}
