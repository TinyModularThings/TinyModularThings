package speiger.src.spmodapi.common.fluids.hemp;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.spmodapi.common.config.ModObjects.APIItems;

public class ItemBlockFluidHempResin extends ItemBlock
{

	public ItemBlockFluidHempResin(Block par1)
	{
		super(par1);
	}

	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return APIItems.hempResin.getItemStackDisplayName(new ItemStack(APIItems.hempResin));
	}
	
	
	
	
	
}
