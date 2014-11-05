package speiger.src.spmodapi.common.blocks.deko;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.SpmodAPI;

public class ItemBlockFlower extends ItemBlock
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
	
	
}
