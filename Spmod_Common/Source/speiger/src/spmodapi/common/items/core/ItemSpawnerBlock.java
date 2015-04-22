package speiger.src.spmodapi.common.items.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemSpawnerBlock extends ItemBlock
{

	public ItemSpawnerBlock()
	{
		super(Block.mobSpawner.blockID - 256);
		this.setHasSubtypes(true);
	}
	
}
