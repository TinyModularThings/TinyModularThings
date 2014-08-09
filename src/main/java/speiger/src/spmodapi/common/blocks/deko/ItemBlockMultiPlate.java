package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockMultiPlate extends ItemBlock
{
	
	public ItemBlockMultiPlate(Block par1)
	{
		super(par1);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		return "MultiPlate";
	}

	
	
}
