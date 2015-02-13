package speiger.src.spmodapi.common.blocks.utils;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.spmodapi.common.items.core.ItemBlockSpmod;

public class ItemBlockUtils extends ItemBlockSpmod
{
	
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
			case 0: return "Cobble Workbench";
			case 1: return "Exp Storage";
			case 2: return "Mob Machine";
			case 3: return "Entity Lurer";
			case 4: return "Inventory Accesser";
			case 5: return "Cobble Storage";
			default: return "No Name";
		}
	}
	
}
