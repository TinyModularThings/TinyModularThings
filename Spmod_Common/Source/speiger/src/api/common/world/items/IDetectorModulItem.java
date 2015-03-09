package speiger.src.api.common.world.items;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import speiger.src.api.common.world.tiles.machine.IDetectorModul;

public interface IDetectorModulItem
{
	public IDetectorModul createDetectorModul(ItemStack par1, TileEntity detector);
}
