package speiger.src.api.hopper;

import net.minecraft.item.ItemStack;

public interface IHopperUpgradeItem
{
	public HopperUpgrade getUpgrade(ItemStack par1, IHopper par2);
}
