package speiger.src.spmodapi.common.fluids.potion;

import net.minecraft.item.ItemStack;
import speiger.src.api.language.LanguageRegister;
import speiger.src.api.util.SpmodMod;
import speiger.src.api.util.SpmodModRegistry;
import speiger.src.spmodapi.common.config.ModObjects.APIUtils;
import speiger.src.tinymodularthings.common.items.core.TinyItem;

public class ItemPotionSteam extends TinyItem
{
	
	public ItemPotionSteam(int par1)
	{
		super(par1);
		this.setHasSubtypes(true);
	}

	@Override
	public void registerItems(int id, SpmodMod par0)
	{
		if(SpmodModRegistry.areModsEqual(par0, getMod()))
		{
			return;
		}
		LanguageRegister.getLanguageName(APIUtils.fluidPotionSteam, "steam.potion", par0);
	}
	
	@Override
	public String getDisplayName(ItemStack par1, SpmodMod par0)
	{
		return LanguageRegister.getLanguageName(APIUtils.fluidPotionSteam, "steam.potion", par0);
	}
	
}
