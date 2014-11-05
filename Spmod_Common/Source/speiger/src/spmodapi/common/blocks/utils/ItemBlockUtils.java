package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import speiger.src.api.common.registry.helpers.SpmodMod;
import speiger.src.api.common.registry.helpers.SpmodModRegistry;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.SpmodAPI;

public class ItemBlockUtils extends ItemBlock
{
	String[] names = new String[] { "cobble.workbench", "exp.storage", "mob.machine", "entity.lurer", "Inventory Accesser"};
	
	public ItemBlockUtils(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}
	
}
