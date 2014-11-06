package speiger.src.spmodapi.common.items.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.core.SpmodItem;

public class ItemBlueDye extends SpmodItem
{
	
	public ItemBlueDye(int par1)
	{
		super(par1);
		OreDictionary.registerOre("dyeBlue", this);
		this.setCreativeTab(APIUtils.tabCrafing);
	}

	@Override
	public String getName(ItemStack par1)
	{
		return "Blue Dye";
	}
	
}
