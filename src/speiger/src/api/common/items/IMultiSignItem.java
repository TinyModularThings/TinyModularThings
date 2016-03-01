package speiger.src.api.common.items;

import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public interface IMultiSignItem
{
	public Material getMaterial(ItemStack signItem);
	
	public SoundType getSound(ItemStack signItem);
}
