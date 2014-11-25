package speiger.src.tinymodularthings.common.blocks.crafting;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.blocks.BlockStack;
import speiger.src.tinymodularthings.common.items.core.ItemBlockTinyChest;

public class ItemBlockCrafting extends ItemBlockTinyChest
{

	public ItemBlockCrafting(int par1)
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
			case 0: return "Advanced Workbench";
			case 1: return "Oredictionary Crafter";
			case 2: return "Advanced Crafting Station";
			case 3: return "Deconstructor";
			default: return "";
		}
	}
	
	
	
}
