package speiger.src.spmodapi.common.items.crafting;

import net.minecraftforge.oredict.OreDictionary;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.spmodapi.common.items.SpmodItem;

public class ItemBlueDye extends SpmodItem
{
	
	public ItemBlueDye(int par1)
	{
		super(par1);
		OreDictionary.registerOre("dyeBlue", this);
		this.setCreativeTab(APIUtils.tabCrafing);
	}
	
}
