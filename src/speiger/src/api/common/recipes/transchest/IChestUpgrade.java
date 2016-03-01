package speiger.src.api.common.recipes.transchest;

import net.minecraft.block.Block;

public interface IChestUpgrade
{
	public Block getBlock();
	
	public int[] getMetas();
	
	public int getSlotCountForMeta(int meta);
}
