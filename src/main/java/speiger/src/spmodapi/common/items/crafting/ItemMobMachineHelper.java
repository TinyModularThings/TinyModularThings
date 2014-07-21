package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import speiger.src.api.util.SpmodMod;
import speiger.src.spmodapi.common.blocks.utils.MobMachine;
import speiger.src.spmodapi.common.items.SpmodItem;
import speiger.src.spmodapi.common.util.TileIconMaker;

public class ItemMobMachineHelper extends SpmodItem
{
	private MobMachine mobs;
	public ItemMobMachineHelper()
	{
		mobs = (MobMachine) TileIconMaker.getIconMaker().getTileEntityFromClass(MobMachine.class);
	}

	@Override
	public void registerItems(Item item, SpmodMod par0)
	{
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		
		return "";
	}
	
}
