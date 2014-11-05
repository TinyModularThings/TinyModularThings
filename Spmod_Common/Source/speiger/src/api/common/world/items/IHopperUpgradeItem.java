package speiger.src.api.common.world.items;

import net.minecraft.item.ItemStack;
import speiger.src.api.common.world.tiles.interfaces.HopperUpgrade;
import speiger.src.api.common.world.tiles.interfaces.IHopper;

public interface IHopperUpgradeItem
{
	public HopperUpgrade getUpgrade(ItemStack par1, IHopper par2);
}
